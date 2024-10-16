/*
 * File updated ~ 15 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.client.AllomancyKeybindings;
import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.network.packets.EntityAllomancyActivateMessage;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.helpers.PlayerHelper;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.common.config.CosmereConfigs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.*;

public class AllomancyZinc extends AllomancyManifestation
{
	public static final HashMap<String, ZincThread> playerThreadMap = new HashMap<>();

	public AllomancyZinc(Metals.MetalType metalType)
	{
		super(metalType);
	}

	//Inflames Emotions
	//make hostiles target you but also make non-hostiles target hostiles?
	@Override
	public void applyEffectTick(ISpiritweb data)
	{
		if (data.getLiving().level().isClientSide())
		{
			if (isActiveTick(data))
			{
				boolean isSingleTarget = AllomancyKeybindings.ALLOMANCY_RIOT.isDown() || (Keybindings.MANIFESTATION_USE_ACTIVE.isDown() && data.getSelectedManifestation().equals(getManifestation()));
				int singleTargetEntityId = 0;

				if (isSingleTarget)
				{
					HitResult ray = PlayerHelper.pickWithRange(data.getLiving(), (int) Math.floor(getRange(data) * CosmereConfigs.SERVER_CONFIG.EMOTIONAL_POWERS_SINGLE_TARGET_RANGE_MULTIPLIER.get()));
					if (ray instanceof EntityHitResult entityHitResult)
					{
						singleTargetEntityId = entityHitResult.getEntity().getId();
					}
				}

				Allomancy.packetHandler().sendToServer(new EntityAllomancyActivateMessage(Metals.MetalType.ZINC, isSingleTarget, singleTargetEntityId));
			}
		}
		else
		{
			performEffectServer(data);
		}
	}

	private void performEffectServer(ISpiritweb data)
	{
		int mode = getMode(data);
		String uuid = data.getLiving().getStringUUID();

		boolean isSingleTarget = playerThreadMap.get(data.getLiving().getStringUUID()).isSingleTarget;

		// data processing
		{
			// this is the only way to check if the player is still online, thanks forge devs
			if (data.getLiving().level().getServer().getPlayerList().getPlayer(data.getLiving().getUUID()) == null)
			{
				return;
			}

			if (playerThreadMap.get(uuid) == null)
			{
				playerThreadMap.put(uuid, new ZincThread(data));
			}
			List<LivingEntity> entitiesToAffect = new ArrayList<>();

			if (isSingleTarget)
			{
				if (data.getLiving().level().getEntity(playerThreadMap.get(uuid).singleTargetEntityID) instanceof LivingEntity entity)
				{
					entitiesToAffect.add(entity);
				}
			}
			else
			{
				entitiesToAffect = playerThreadMap.get(uuid).requestEntityList();
			}

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

			if (!isSingleTarget)
			{
				playerThreadMap.get(uuid).releaseEntityList();
			}
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
				playerThreadMap.put(uuid, new ZincThread(data));
			}

			playerThreadMap.entrySet().removeIf(entry -> !entry.getValue().isRunning || AllomancyEntityThread.serverShutdown || entry.getValue() == null);
		}

		return super.tick(data);
	}

	public class ZincThread extends AllomancyEntityThread
	{
		public boolean isSingleTarget = false;
		public int singleTargetEntityID = 0;

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
