/*
 * File updated ~ 15 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.common.registries.AllomancyEffects;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.*;

public class AllomancyBrass extends AllomancyManifestation
{
	private static final HashMap<String, BrassThread> playerThreadMap = new HashMap<>();

	public AllomancyBrass(Metals.MetalType metalType)
	{
		super(metalType);
	}

	//Dampens Emotions
	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		int mode = getMode(data);
		String uuid = data.getLiving().getStringUUID();

		// data processing
		{
			// this is the only way to check if the player is still online, thanks forge devs
			if (data.getLiving().level.getServer().getPlayerList().getPlayer(data.getLiving().getUUID()) == null)
			{
				return;
			}

			//todo, replace x * mode with config based value
			double allomanticStrength = getStrength(data, false);

			if (playerThreadMap.get(uuid) == null)
			{
				playerThreadMap.put(uuid, new BrassThread(data));
			}

			List<LivingEntity> entitiesToAffect = playerThreadMap.get(uuid).requestEntityList();
			for (LivingEntity e : entitiesToAffect)
			{
				if (e instanceof Mob mob)
				{
					switch (mode)
					{
						case 2:
							if (allomanticStrength > 15 && !mob.isNoAi())
							{
								mob.addEffect(EffectsHelper.getNewEffect(
										AllomancyEffects.ALLOMANTIC_BRASS_STUN.getMobEffect(),
										0,      // no amplification system in place
										20*5
								));
							}
							mob.setTarget(null);
						case 1:
							mob.setAggressive(false);
						default://stop angry targets from attacking things
							mob.setLastHurtByMob(null);
					}
				}
			}
			playerThreadMap.get(uuid).releaseEntityList();
		}
	}

	@Override
	public void onModeChange(ISpiritweb data, int lastMode)
	{
		String uuid = data.getLiving().getStringUUID();
		if (getMode(data) <= 0)
		{
			playerThreadMap.remove(uuid);
		}

		super.onModeChange(data, lastMode);
	}

	@Override
	public boolean tick(ISpiritweb data)
	{
		// data thread management
		{
			String uuid = data.getLiving().getStringUUID();
			if (!playerThreadMap.containsKey(uuid))
			{
				playerThreadMap.put(uuid, new BrassThread(data));
			}

			playerThreadMap.entrySet().removeIf(entry -> !entry.getValue().isRunning || AllomancyEntityThread.serverShutdown || entry.getValue() == null);
		}

		return super.tick(data);
	}

	class BrassThread extends AllomancyEntityThread
	{

		public BrassThread(ISpiritweb data)
		{
			super(data);

			Thread t = new Thread(this, "brass_thread_" + data.getLiving().getDisplayName().getString());
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

						newEntityList = EntityHelper.getLivingEntitiesInRange(data.getLiving(), range, true);
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
