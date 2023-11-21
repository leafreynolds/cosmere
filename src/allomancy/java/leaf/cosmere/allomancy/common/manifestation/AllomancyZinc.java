/*
 * File updated ~ 15 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

public class AllomancyZinc extends AllomancyManifestation
{
	private static final HashMap<String, ZincThread> playerThreadMap = new HashMap<>();

	public AllomancyZinc(Metals.MetalType metalType)
	{
		super(metalType);
	}

	//Inflames Emotions
	//make hostiles target you but also make non-hostiles target hostiles?
	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		int mode = getMode(data);
		String uuid = data.getLiving().getStringUUID();

		// data thread management
		{
			if (mode > 0 && !playerThreadMap.containsKey(uuid))
			{
				playerThreadMap.put(uuid, new ZincThread(data));
			}

			playerThreadMap.entrySet().removeIf(entry -> !entry.getValue().isRunning || AllomancyEntityThread.serverShutdown || entry.getValue() == null);
		}

		// data processing
		{
			// check if zinc is off or compounding
			if (mode <= 0)
			{
				return;
			}

			// this is the only way to check if the player is still online, thanks forge devs
			if (data.getLiving().level.getServer().getPlayerList().getPlayer(data.getLiving().getUUID()) == null)
			{
				return;
			}

			//put on a different tick to brass
			boolean isActiveTick = getActiveTick(data) % 2 == 0;
			if (isActiveTick)
			{
				if (playerThreadMap.get(uuid) == null)
				{
					playerThreadMap.put(uuid, new ZincThread(data));
				}
				List<LivingEntity> entitiesToAffect = playerThreadMap.get(uuid).requestEntityList();
				for (LivingEntity e : entitiesToAffect)
				{
					if (e instanceof Mob mob)
					{

						//mob.targetSelector.enableFlag(Goal.Flag.TARGET);
						mob.setNoAi(false);

						switch (mode)
						{
							case 3:
								if (mob.getTarget() == null)
								{
									LivingEntity attackTarget = entitiesToAffect.get(MathHelper.RANDOM.nextInt(entitiesToAffect.size()));
									mob.setTarget(attackTarget);
								}
							case 2:
								if (mob.getLastHurtByMob() == null)
								{
									mob.setLastHurtByMob(mob.getTarget() != null
									                     ? mob.getTarget()
									                     : entitiesToAffect.get(MathHelper.RANDOM.nextInt(entitiesToAffect.size())));
								}

							case 1:
							default:
								mob.setAggressive(true);
						}
					}
				}
				playerThreadMap.get(uuid).releaseEntityList();
			}
		}
	}

	class ZincThread extends AllomancyEntityThread
	{

		public ZincThread(ISpiritweb data)
		{
			super(data);

			Thread t = new Thread(this, "zinc_thread_" + data.getLiving().getDisplayName().getString());
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
