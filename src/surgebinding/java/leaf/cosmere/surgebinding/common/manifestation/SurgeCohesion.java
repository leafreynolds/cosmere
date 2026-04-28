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
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SurgeCohesion extends SurgebindingManifestation
{
	public SurgeCohesion(Roshar.Surges surge)
	{
		super(surge);
	}


	// alter objects at a molecular level?
	// moving through stone?

	static List<Block> cohesive = new ArrayList<>(Arrays.asList(
			Blocks.STONE, Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE, Blocks.SMOOTH_STONE, Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS,
			Blocks.GRANITE, Blocks.POLISHED_GRANITE, Blocks.DIORITE, Blocks.POLISHED_DIORITE, Blocks.ANDESITE, Blocks.POLISHED_ANDESITE,
			Blocks.COBBLED_DEEPSLATE, Blocks.POLISHED_DEEPSLATE, Blocks.DEEPSLATE_BRICKS, Blocks.DEEPSLATE_TILES,
			Blocks.BRICKS, Blocks.MUD_BRICKS, Blocks.SANDSTONE, Blocks.SMOOTH_SANDSTONE, Blocks.CUT_SANDSTONE,
			Blocks.RED_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE, Blocks.PRISMARINE, Blocks.PRISMARINE_BRICKS, Blocks.DARK_PRISMARINE,
			Blocks.NETHER_BRICKS, Blocks.RED_NETHER_BRICKS, Blocks.BASALT, Blocks.BLACKSTONE, Blocks.POLISHED_BLACKSTONE, Blocks.POLISHED_BLACKSTONE_BRICKS,
			Blocks.END_STONE, Blocks.END_STONE_BRICKS, Blocks.PURPUR_BLOCK, Blocks.QUARTZ_BLOCK, Blocks.SMOOTH_QUARTZ));

	public static boolean isValidStoneBlock(Block blok)
	{
		for (Block b : cohesive)
		{
			if (b == blok)
			{
				return true;
			}
		}
		return false;
	}

	public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event)
	{
		final BlockPos blockPos = event.getHitVec().getBlockPos();
		Block block = event.getLevel().getBlockState(blockPos).getBlock();
		SpiritwebCapability.get(event.getEntity()).ifPresent(iSpiritweb ->
		{
			SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) iSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
			if (SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.COHESION).getManifestation() instanceof SurgebindingManifestation sg &&
					isValidStoneBlock(block))
			{
				if (sg.isActive(iSpiritweb) && event.getEntity().getMainHandItem().isEmpty())
				{
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
				}
				else
				{
					//To do Stonecutter effect
				}
			}
		});
	}

}
