/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.manifestation;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class SurgeTransformation extends SurgebindingManifestation
{
	public SurgeTransformation(Roshar.Surges surge)
	{
		super(surge);
	}

	//soulcasting, changing one thing into another


	@Override
	public int modeMax(ISpiritweb data)
	{
		return this.getMode(data) * 2;
	}

	public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event)
	{
		BlockPos blockPos = event.getPos();
		SpiritwebCapability.get(event.getEntity()).ifPresent(iSpiritweb ->
		{
			SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) iSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
			if (SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.TRANSFORMATION).getManifestation() instanceof SurgebindingManifestation sg &&
					sg.isActive(iSpiritweb))
			{
				int mode = sg.getMode(iSpiritweb);
				int cost = mode == 8 ? -150 : -50;
				if (submodule.adjustStormlight(cost, true))
				{
					switch (mode)
					{
						case 1:
							event.getLevel().setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
							break;
						case 2:
							event.getLevel().setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
							if (event.getLevel() instanceof ServerLevel serverLevel)
							{
								serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, 0, 0, 0, 0);
							}
							break;
						case 3:
							event.getLevel().setBlock(blockPos, Blocks.FIRE.defaultBlockState(), 3);
							break;
						case 4:
							event.getLevel().setBlock(blockPos, Blocks.QUARTZ_BLOCK.defaultBlockState(), 3);
							break;
						case 5:
							event.getLevel().setBlock(blockPos, Blocks.OAK_LEAVES.defaultBlockState(), 3);
							break;
						case 6:
							event.getLevel().setBlock(blockPos, Blocks.WATER.defaultBlockState(), 3);
							break;
						case 7:
							event.getLevel().setBlock(blockPos, Blocks.ICE.defaultBlockState(), 3);
							break;
						case 8:
							event.getLevel().setBlock(blockPos, Blocks.IRON_BLOCK.defaultBlockState(), 3);
							break;
						case 9:
							event.getLevel().setBlock(blockPos, Blocks.STONE.defaultBlockState(), 3);
							break;
						case 10:
							event.getLevel().setBlock(blockPos, Blocks.NETHERRACK.defaultBlockState(), 3);
							break;

					}
				}
			}
		});
	}


}
