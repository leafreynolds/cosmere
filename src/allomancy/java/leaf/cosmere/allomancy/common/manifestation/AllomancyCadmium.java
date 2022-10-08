/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class AllomancyCadmium extends AllomancyManifestation
{
	public AllomancyCadmium(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		//Speeds Up Time for everything around the user, implying the user is slower
		{
			//tick entities around user
			if (data.getLiving().tickCount % 6 == 0)
			{
				int range = getRange(data);
				int x = (int) (data.getLiving().getX() + (data.getLiving().getRandomX(range * 2 + 1) - range));
				int z = (int) (data.getLiving().getZ() + (data.getLiving().getRandomZ(range * 2 + 1) - range));

				for (int i = 4; i > -2; i--)
				{
					int y = data.getLiving().blockPosition().getY() + i;
					BlockPos pos = new BlockPos(x, y, z);
					Level world = data.getLiving().level;

					if (world.isEmptyBlock(pos))
					{
						continue;
					}

					BlockState state = world.getBlockState(pos);
					state.randomTick((ServerLevel) world, pos, world.random);

					break;
				}

				//todo tick living entities?

				List<LivingEntity> entitiesToCheck = EntityHelper.getLivingEntitiesInRange(data.getLiving(), range, true);

				for (LivingEntity e : entitiesToCheck)
				{
					e.aiStep();
				}
			}
		}


	}


}
