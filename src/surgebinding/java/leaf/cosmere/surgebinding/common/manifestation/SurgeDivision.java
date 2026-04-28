/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.manifestation;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class SurgeDivision extends SurgebindingManifestation
{
	public SurgeDivision(Roshar.Surges surge)
	{
		super(surge);
	}

	//power over destruction and decay
	public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event)
	{
		final BlockPos blockPos = event.getHitVec().getBlockPos();

		SpiritwebCapability.get(event.getEntity()).ifPresent(iSpiritweb ->
		{
			if (SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.DIVISION).getManifestation() instanceof SurgebindingManifestation sg &&
					sg.isActive(iSpiritweb) &&
					event.getEntity().getMainHandItem().isEmpty())
			{
				SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) iSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
				if (iSpiritweb.getLiving().isShiftKeyDown())
				{
					if (submodule.adjustStormlight(-15, true))
					{
						if (event.getLevel() instanceof ServerLevel serverLevel)
						{
							Direction direc = event.getHitVec().getDirection();
							BlockPos targetBlock = new BlockPos(blockPos.getX() + direc.getStepX(), blockPos.getY() + direc.getStepY(), blockPos.getZ() + direc.getStepZ());
							serverLevel.setBlock(targetBlock, Blocks.FIRE.defaultBlockState(), 3);
						}
					}
				}
				else
				{
					if (submodule.adjustStormlight(-15, true))
					{
						if (event.getLevel() instanceof ServerLevel serverLevel)
						{
							serverLevel.destroyBlock(blockPos, false);
							serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 2, 0, 0, 0, 0.1);
						}
					}
				}
			}
		});
	}

	public static void onLivingAttackEvent(LivingAttackEvent event)
	{
		LivingEntity target = event.getEntity();
		if (event.getSource().getEntity() instanceof Player player && !event.getSource().is(DamageTypeTags.IS_PROJECTILE) && player.getMainHandItem().isEmpty())
		{
			SpiritwebCapability.get(player).ifPresent(iSpiritweb ->
			{
				if (SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.DIVISION).getManifestation() instanceof SurgebindingManifestation sg &&
						sg.isActive(iSpiritweb))
				{
					SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) iSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
					if (submodule.adjustStormlight(-40, true))
					{
						target.addEffect(EffectsHelper.getNewEffect(MobEffects.WITHER, 1, 80));
					}
				}
			});
		}
	}

}
