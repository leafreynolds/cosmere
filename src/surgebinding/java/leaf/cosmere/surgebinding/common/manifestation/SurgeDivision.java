/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.manifestation;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.config.SurgebindingConfigs;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
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
		if (!event.getEntity().getMainHandItem().isEmpty())
		{
			return;
		}

		final BlockPos blockPos = event.getHitVec().getBlockPos();

		SpiritwebCapability.get(event.getEntity()).ifPresent(iSpiritweb ->
		{
			if (iSpiritweb.hasManifestation(SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.DIVISION).get()) && SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.DIVISION).getManifestation().isActive(iSpiritweb))
			{
				SpiritwebCapability playerSpiritweb = (SpiritwebCapability) iSpiritweb;
				SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) playerSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);

				if (submodule.adjustStormlight(-20, true))
				{
					if (event.getLevel() instanceof ServerLevel serverLevel)
					{
						event.getLevel().destroyBlock(blockPos, true);
					}
				}
			}
		});
	}
}
