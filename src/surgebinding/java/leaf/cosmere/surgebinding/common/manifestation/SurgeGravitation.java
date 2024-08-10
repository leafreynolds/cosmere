/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.manifestation;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class SurgeGravitation extends SurgebindingManifestation
{
	public SurgeGravitation(Roshar.Surges surge)
	{
		super(surge);
	}

	//gravitational attraction


	public static void onLivingAttackEvent(LivingAttackEvent event)
	{
		if (event.getSource().getEntity() instanceof Player player && !event.getSource().is(DamageTypeTags.IS_PROJECTILE) && player.getMainHandItem().isEmpty())
		{
			SpiritwebCapability.get(player).ifPresent(iSpiritweb ->
			{
				//windrunners like Szeth could launch enemies into the air to die cruelly by fall damage
				if (iSpiritweb.hasManifestation(SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.GRAVITATION).get()))
				{

					SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) iSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);

					if (submodule.adjustStormlight(-20, true))
					{
						final LivingEntity entity = event.getEntity();
						CosmereAPI.logger.info("%s has launched %s into the sky".formatted(player.getName().getString(), entity.getName().getString()));

						entity.stopRiding();
						entity.setPos(event.getEntity().getX(), event.getEntity().getY() + 0.1d, event.getEntity().getZ());
						entity.setOnGround(false);
						entity.setJumping(true);

						entity.setDeltaMovement(0, 50, 0);


						entity.hurtMarked = true;
					}
				}
			});
		}


	}

	public static boolean canFly(LivingEntity entity)
	{
		boolean canFly = false;
		var spiritwebCapability = SpiritwebCapability.get(entity);
		if (spiritwebCapability.isPresent() && spiritwebCapability.resolve().get() instanceof SpiritwebCapability data)
		{

			canFly = SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.GRAVITATION).get().isActive(data);
		}

		if (entity instanceof Player player)
		{
			player.getAbilities().mayfly = player.isCreative() || canFly;
		}

		return canFly;
	}


	/**
	 * gravitation version of {@link FireworkRocketEntity#tick()}.
	 */
	public static boolean flyTick(LivingEntity entity)
	{
		if (!entity.isFallFlying() || !canFly(entity))
		{
			//if we aren't flying, everything else is irrelevant
			return false;
		}
		else
		{
			var spiritwebCapability = SpiritwebCapability.get(entity);
			if (spiritwebCapability.isPresent() && spiritwebCapability.resolve().get() instanceof SpiritwebCapability data)
			{
				Vec3 lookAngle = entity.getLookAngle();
				Vec3 deltaMovement = entity.getDeltaMovement();
				entity.setDeltaMovement(deltaMovement.add(lookAngle.x * 0.1D + (lookAngle.x * 1.5D - deltaMovement.x) * 0.5D, lookAngle.y * 0.1D + (lookAngle.y * 1.5D - deltaMovement.y) * 0.5D, lookAngle.z * 0.1D + (lookAngle.z * 1.5D - deltaMovement.z) * 0.5D));
				return true;
			}
		}

		return true;
	}
}
