/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.manifestation;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class SurgeCohesion extends SurgebindingManifestation
{
	public SurgeCohesion(Roshar.Surges surge)
	{
		super(surge);
	}


	// alter objects at a molecular level?
	// moving through stone?

	public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event){
		final BlockPos blockPos = event.getHitVec().getBlockPos();
		BlockState blockState = event.getLevel().getBlockState(blockPos);
		blockState.getBlock();
		SpiritwebCapability.get(event.getEntity()).ifPresent(iSpiritweb ->
		{
			if(iSpiritweb.hasManifestation(SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.COHESION).get()) &&
				SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.COHESION).getManifestation().isActive(iSpiritweb))
			{
				SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) iSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
				if (!iSpiritweb.getLiving().isShiftKeyDown())
				{
					if (submodule.adjustStormlight(-15, true))
					{
						if (event.getLevel() instanceof ServerLevel serverLevel)
						{
							serverLevel.destroyBlock(blockPos, true);
						}
					}
				}
				else{

				}
			}
		});
	}

}
