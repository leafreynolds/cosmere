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
		return 10;
	}

	public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event)
	{
		BlockPos blockPos = event.getPos();
		SpiritwebCapability.get(event.getEntity()).ifPresent(iSpiritweb->
		{
			SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) iSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
			SurgebindingManifestation surge = (SurgebindingManifestation) SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.TRANSFORMATION).getManifestation();
			if(iSpiritweb.hasManifestation(surge) && surge.isActive(iSpiritweb)){
				if(submodule.adjustStormlight(-50,true))
				{
					switch (surge.getMode(iSpiritweb))
					{
						case 1:
							event.getLevel().setBlock(blockPos, Blocks.AIR.defaultBlockState(), 1);
							break;
						case 2:
							event.getLevel().setBlock(blockPos, Blocks.AIR.defaultBlockState(), 1);
							event.getLevel().addParticle(ParticleTypes.LARGE_SMOKE,blockPos.getX(),blockPos.getY(),blockPos.getZ(),0,0,0);
							break;
						case 3:
							event.getLevel().setBlock(blockPos, Blocks.FIRE.defaultBlockState(), 1);
							break;
						case 4:
							event.getLevel().setBlock(blockPos, Blocks.QUARTZ_BLOCK.defaultBlockState(), 1);
							break;
						case 5:
							event.getLevel().setBlock(blockPos, Blocks.OAK_LEAVES.defaultBlockState(), 1);
							break;
						case 6:
							event.getLevel().setBlock(blockPos, Blocks.WATER.defaultBlockState(), 1);
							break;
						case 7:
							event.getLevel().setBlock(blockPos, Blocks.ICE.defaultBlockState(), 1);
							break;
						case 8:
							submodule.adjustStormlight(-100,true);
							event.getLevel().setBlock(blockPos, Blocks.IRON_BLOCK.defaultBlockState(), 1);
							break;
						case 9:
							event.getLevel().setBlock(blockPos, Blocks.STONE.defaultBlockState(), 1);
							break;
						case 10:
							event.getLevel().setBlock(blockPos, Blocks.NETHERRACK.defaultBlockState(), 1);
							break;

					}
				}
			}
		});
	}


}
