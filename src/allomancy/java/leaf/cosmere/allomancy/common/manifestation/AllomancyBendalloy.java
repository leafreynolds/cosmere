/*
 * File updated ~ 15 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

public class AllomancyBendalloy extends AllomancyManifestation
{
	private static final HashMap<String, BendalloyThread> playerThreadMap = new HashMap<>();

	public AllomancyBendalloy(Metals.MetalType metalType)
	{
		super(metalType);
	}


	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		if (AllomancyEntityThread.serverShutdown)
		{
			//don't start up new threads if the server is shutting down
			return;
		}

		int mode = getMode(data);
		String uuid = data.getLiving().getStringUUID();

		// data thread management
		{
			if (mode > 0 && !playerThreadMap.containsKey(uuid))
			{
				playerThreadMap.put(uuid, new BendalloyThread(data));
			}

			playerThreadMap.entrySet().removeIf(entry -> !entry.getValue().isRunning || AllomancyEntityThread.serverShutdown || entry.getValue() == null);
		}

		// data processing
		{
			// check if bendalloy is off or compounding
			if (mode <= 0)
			{
				return;
			}

			// this is the only way to check if the player is still online, thanks forge devs
			if (data.getLiving().level.getServer().getPlayerList().getPlayer(data.getLiving().getUUID()) == null)
			{
				return;
			}
			//offload to power's tick
			boolean isActiveTick = getActiveTick(data) % 20 == 0;

			//Slows Down Time for the entities around the user
			if (isActiveTick)
			{
				if (playerThreadMap.get(uuid) == null)
				{
					playerThreadMap.put(uuid, new BendalloyThread(data));
				}
				List<LivingEntity> entitiesToAffect = playerThreadMap.get(uuid).requestEntityList();
				for (LivingEntity e : entitiesToAffect)
				{
					e.addEffect(EffectsHelper.getNewEffect(MobEffects.MOVEMENT_SLOWDOWN, mode));
				}


				//todo slow tile entities? not sure how to do that. cadmium just calls tick more often.
			}

		}
	}

	class BendalloyThread extends AllomancyEntityThread
	{
		public BendalloyThread(ISpiritweb data)
		{
			super(data);

			Thread t = new Thread(this, "bendalloy_thread_" + data.getLiving().getDisplayName().getString());
			t.start();
		}

		@Override
		public void run()
		{
			List<LivingEntity> newEntityList;
			while (true)
			{
				int mode = getMode(data);
				if (serverShutdown)
				{
					break;
				}

				if (mode <= 0)
				{
					break;
				}

				try
				{
					if (lock.tryLock())
					{
						int range = getRange(data);

						newEntityList = EntityHelper.getLivingEntitiesInRange(data.getLiving(), range, false);
						setEntityList(newEntityList);
						lock.unlock();
					}
					// sleep thread for 1 tick (50ms)
					Thread.sleep(50);
				}
				catch (Exception e)
				{
					CosmereAPI.logger.debug(Arrays.toString(e.getStackTrace()));

					if (e instanceof ConcurrentModificationException)
					{
						lock.unlock();
					}

					break;
				}
			}
			isRunning = false;
		}
	}
}
