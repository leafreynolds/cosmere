/*
 * File updated ~ 15 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.HashMap;
import java.util.List;

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

		// data thread management
		{
			if (mode > 0 && !playerThreadMap.containsKey(uuid))
			{
				playerThreadMap.put(uuid, new BrassThread(data));
			}

			playerThreadMap.entrySet().removeIf(entry -> !entry.getValue().isRunning || AllomancyEntityThread.serverShutdown);
		}

		// data processing
		{
			// check if brass is off or compounding
			if (mode <= 0)
			{
				return;
			}

			// this is the only way to check if the player is still online, thanks forge devs
			if (data.getLiving().level.getServer().getPlayerList().getPlayer(data.getLiving().getUUID()) == null)
			{
				return;
			}

			//todo, replace x * mode with config based value
			double allomanticStrength = getStrength(data, false);

			//put on a different tick to zinc
			boolean isActiveTick = getActiveTick(data) % 2 == 0;
			if (isActiveTick)
			{
				List<LivingEntity> entitiesToAffect = playerThreadMap.get(uuid).requestEntityList();
				for (LivingEntity e : entitiesToAffect)
				{
					if (e instanceof Mob mob)
					{
						mob.setNoAi(mode == 3 && allomanticStrength > 15);

						switch (mode)
						{
							case 2:
								mob.setTarget(null);
							case 1:
								mob.setAggressive(false);
							default:
								//stop angry targets from attacking things
								e.setLastHurtByMob(null);

						}
					}
				}
			}
		}
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
			List<LivingEntity> entitiesToAffect;
			while (true)
			{
				if (serverShutdown)
				{
					break;
				}
				try
				{
					int range = getRange(data);
					if (lock.tryLock())
					{
						entitiesToAffect = EntityHelper.getLivingEntitiesInRange(data.getLiving(), range, true);
						setEntityList(entitiesToAffect);
						lock.unlock();
					}

					// sleep thread for 1 tick (50ms)
					Thread.sleep(50);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					break;
				}
			}
			isRunning = false;
		}
	}
}
