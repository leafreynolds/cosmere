/*
 * File updated ~ 26 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.manifestation;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class SurgeProgression extends SurgebindingManifestation
{
	public SurgeProgression(Roshar.Surges surge)
	{
		super(surge);
	}


	//alter growth and healing
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
	{
		if (event.getTarget() instanceof LivingEntity eventTarget)
		{
			final float eventTargetHealth = eventTarget.getHealth();
			final float eventTargetMaxHealth = eventTarget.getMaxHealth();
			int healthMissing = Mth.floor(eventTargetMaxHealth - eventTargetHealth);

			if (healthMissing > 0)
			{
				SpiritwebCapability.get(event.getEntity()).ifPresent(iSpiritweb ->
				{
					if (iSpiritweb.hasManifestation(SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.PROGRESSION).get()))
					{
						SpiritwebCapability playerSpiritweb = (SpiritwebCapability) iSpiritweb;
						SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) playerSpiritweb.spiritwebSubmodules.get(Manifestations.ManifestationTypes.SURGEBINDING);

						//todo config
						final int stormlightHealingCostMultiplier = 20;
						if (submodule.adjustStormlight(-(healthMissing * stormlightHealingCostMultiplier), true))
						{
							heal(eventTarget, eventTargetMaxHealth);
						}
						else
						{
							final int affordableHealth = submodule.getStormlight() / stormlightHealingCostMultiplier;
							if (submodule.adjustStormlight(-submodule.getStormlight(), true))
							{
								heal(eventTarget, eventTargetHealth + affordableHealth);
							}
						}
					}
				});
			}
		}
	}

	public static void heal(LivingEntity livingEntity, float setHealthTo)
	{
		livingEntity.setHealth(setHealthTo);

		for (int i = 0; i < 20; ++i)
		{
			double xSpeed = livingEntity.getRandom().nextGaussian() * 0.02D;
			double ySpeed = livingEntity.getRandom().nextGaussian() * 0.02D;
			double zSpeed = livingEntity.getRandom().nextGaussian() * 0.02D;

			livingEntity.level.addParticle(ParticleTypes.HAPPY_VILLAGER,
					livingEntity.getX(1.0D) - xSpeed * 10.0D,
					livingEntity.getRandomY() - ySpeed * 10.0D,
					livingEntity.getRandomZ(1.0D) - zSpeed * 10.0D,
					xSpeed,
					ySpeed,
					zSpeed);
		}

		//this gets very annoying quick
		/*livingEntity.level.playSound(
				(Player) null,
				livingEntity.getX(),
				livingEntity.getY(),
				livingEntity.getZ(),
				SoundEvents.PLAYER_LEVELUP,
				livingEntity.getSoundSource(),
				0.25F,
				1.0F);*/
	}

	//bonemeal crops
	public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event)
	{
		final BlockPos blockPos = event.getHitVec().getBlockPos();
		final BlockState blockState = event.getLevel().getBlockState(blockPos);
		if (blockState.getBlock() instanceof BonemealableBlock bonemealableBlock)
		{
			SpiritwebCapability.get(event.getEntity()).ifPresent(iSpiritweb ->
			{
				if (iSpiritweb.hasManifestation(SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.PROGRESSION).get()))
				{
					SpiritwebCapability playerSpiritweb = (SpiritwebCapability) iSpiritweb;
					SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) playerSpiritweb.spiritwebSubmodules.get(Manifestations.ManifestationTypes.SURGEBINDING);

					//todo config bonemeal cost
					final int stormlightHealingCostMultiplier = 20;
					if (submodule.adjustStormlight(-stormlightHealingCostMultiplier, true))
					{
						if (event.getLevel() instanceof ServerLevel serverLevel)
						{
							bonemealableBlock.performBonemeal(serverLevel, serverLevel.random, blockPos, blockState);
						}
						else
						{
							BoneMealItem.addGrowthParticles(event.getLevel(), blockPos, 0);
						}
					}
				}
			});
		}
	}
}
