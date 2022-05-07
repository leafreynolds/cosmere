/*
 * File created ~ 27 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.feruchemy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Metals;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
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

public class FeruchemyBronze extends FeruchemyBase
{
	public FeruchemyBronze(Metals.MetalType metalType)
	{
		super(metalType);
		MinecraftForge.EVENT_BUS.addListener(this::sleepCheck);
	}

	@Override
	public int modeMin(ISpiritweb data)
	{
		return 0;
	}

	@Override
	public int modeMax(ISpiritweb data)
	{
		return 1;
	}

	@Override
	public void tick(ISpiritweb data)
	{
		//don't check every tick.
		LivingEntity livingEntity = data.getLiving();

		if (!(livingEntity instanceof ServerPlayer) || livingEntity.tickCount % 20 != 0)
		{
			return;
		}

		int mode = data.getMode(manifestationType, metalType.getID());

		//can't store or tap any more
		if (mode == 0)
		{
			//remove active effects.
			//let the current effect run out.
			return;
		}

		trySleep(data);

	}

	private void trySleep(ISpiritweb data)
	{
		ServerPlayer player = (ServerPlayer) data.getLiving();

		boolean canSleep = canSleep(player);

		if (!canSleep)
		{
			//todo add message to player saying can't sleep
			data.setMode(this.manifestationType, this.metalType.getID(), 0);
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
			ObfuscationReflectionHelper.setPrivateValue(Player.class, player, 0, "field_71076_b");
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

		if (player.level instanceof ServerLevel)
		{
			((ServerLevel) player.level).updateSleepingPlayerList();
		}


		player.awardStat(Stats.SLEEP_IN_BED);
		CriteriaTriggers.SLEPT_IN_BED.trigger(player);

		data.setMode(this.manifestationType, this.metalType.getID(), 0);

	}

	private boolean canSleep(Player player)
	{
		Player.BedSleepingProblem ret = net.minecraftforge.event.ForgeEventFactory.onPlayerSleepInBed(player, Optional.empty());
		if (ret != null)
		{
			return false;
		}

		if (player.isSleeping() || !player.isAlive())
		{
			return false;//(PlayerEntity.SleepResult.OTHER_PROBLEM);
		}

		if (!player.level.dimensionType().natural())
		{
			return false;//(PlayerEntity.SleepResult.NOT_POSSIBLE_HERE);
		}
		if (player.level.isDay())
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
			List<Monster> list = player.level.getEntitiesOfClass(
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
		if (event.getEntityLiving() instanceof Player)
		{
			SpiritwebCapability.get(event.getEntityLiving()).ifPresent(iSpiritweb ->
			{
				if (iSpiritweb.hasManifestation(this.manifestationType, this.metalType.getID()))
				{
					event.setResult(Event.Result.ALLOW);
				}
			});

		}
	}
}
