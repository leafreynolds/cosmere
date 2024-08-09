/*
 * File updated ~ 9 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.util.List;
import java.util.Optional;

public class FeruchemyBronze extends FeruchemyManifestation
{
	public FeruchemyBronze(Metals.MetalType metalType)
	{
		super(metalType);
		MinecraftForge.EVENT_BUS.addListener(this::sleepCheck);
	}

	@Override
	public int modeMin(ISpiritweb data)
	{
		return -1;
	}

	@Override
	public int modeMax(ISpiritweb data)
	{
		return 1;
	}

	@Override
	public void applyEffectTick(ISpiritweb data)
	{
		super.applyEffectTick(data);

		//can't store or tap any more
		switch (getMode(data))
		{
			case -1:
				resetSleepTimers(data);
				break;
			case 1:
				trySleep(data);
				break;
			default:
				//remove active effects.
				//let the current effect run out.
				break;
		}
	}

	private void resetSleepTimers(ISpiritweb data)
	{
		ServerPlayer player = (ServerPlayer) data.getLiving();
		//ServerStatsCounter serverstatscounter = player.getStats();
		player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));

	}

	private void trySleep(ISpiritweb data)
	{
		ServerPlayer player = (ServerPlayer) data.getLiving();

		if (player.isSleeping())
		{
			return;
		}


		boolean canSleep = canSleep(player);

		if (!canSleep)
		{
			//todo add message to player saying can't sleep
			data.setMode(this, 0);
			return;
		}


		player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
		if (player.isPassenger())
		{
			player.stopRiding();
		}

		try
		{
			player.setPose(Pose.SLEEPING);
            /*
            Method setPose = ObfuscationReflectionHelper.findMethod(Entity.class, "func_213301_b", Pose.class);
            setPose.invoke(player, Pose.SLEEPING);
            */
		}
		catch (Exception e)
		{
		}
		player.setSleepingPos(player.blockPosition());
		player.setDeltaMovement(Vec3.ZERO);
		player.hasImpulse = true;

		try
		{
			//sleepCounter
			ObfuscationReflectionHelper.setPrivateValue(Player.class, player, 0, "f_36110_");
		}
		catch (ObfuscationReflectionHelper.UnableToFindFieldException e)
		{
			try
			{
				//sleepCounter
				ObfuscationReflectionHelper.setPrivateValue(Player.class, player, 0, "sleepCounter");
			}
			catch (ObfuscationReflectionHelper.UnableToFindFieldException unableToFindFieldException)
			{
			}
		}

		if (player.level() instanceof ServerLevel serverLevel)
		{
			serverLevel.updateSleepingPlayerList();
		}


		player.awardStat(Stats.SLEEP_IN_BED);
		CriteriaTriggers.SLEPT_IN_BED.trigger(player);
	}

	private boolean canSleep(Player player)
	{
		Player.BedSleepingProblem ret = ForgeEventFactory.onPlayerSleepInBed(player, Optional.empty());
		if (ret != null)
		{
			return false;
		}

		if (player.isSleeping() || !player.isAlive())
		{
			return false;//(PlayerEntity.SleepResult.OTHER_PROBLEM);
		}

		if (!player.level().dimensionType().natural())
		{
			return false;//(PlayerEntity.SleepResult.NOT_POSSIBLE_HERE);
		}
		if (player.level().isDay())
		{
			return false;//(PlayerEntity.SleepResult.NOT_POSSIBLE_NOW);
		}

		if (!ForgeEventFactory.fireSleepingTimeCheck(player, Optional.empty()))
		{
			return false;//(PlayerEntity.SleepResult.NOT_POSSIBLE_NOW);
		}

		if (!player.isCreative())
		{
			Vec3 vector3d = player.position();
			List<Monster> list = player.level().getEntitiesOfClass(
					Monster.class,
					new AABB(
							vector3d.x() - 8D,
							vector3d.y() - 5D,
							vector3d.z() - 8D,
							vector3d.x() + 8D,
							vector3d.y() + 5D,
							vector3d.z() + 8D),
					(entity) -> entity.isPreventingPlayerRest(player));

			return list.isEmpty(); //(PlayerEntity.SleepResult.NOT_SAFE);
		}

		return true;
	}

	public void sleepCheck(SleepingLocationCheckEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			SpiritwebCapability.get(event.getEntity()).ifPresent(iSpiritweb ->
			{
				if (isActive(iSpiritweb))
				{
					event.setResult(Event.Result.ALLOW);
				}
			});

		}
	}
}
