/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

import static leaf.cosmere.utils.helpers.EntityHelper.getLivingEntitiesInRange;

public class AllomancyBendalloy extends AllomancyBase
{
    public AllomancyBendalloy(Metals.MetalType metalType)
    {
        super(metalType);
    }

    @Override
    protected void performEffect(ISpiritweb data)
    {
        //Speeds Up Time
        {
            //tick entities around user
            if (data.getLiving().ticksExisted % 6 == 0)
            {
                int range = 3 * data.getMode(Manifestations.ManifestationTypes.ALLOMANCY, getPowerID());
                int x = (int) (data.getLiving().getPosX() + (data.getLiving().getPosXRandom(range * 2 + 1) - range));
                int z = (int) (data.getLiving().getPosZ() + (data.getLiving().getPosZRandom(range * 2 + 1) - range));

                for (int i = 4; i > -2; i--)
                {
                    int y = data.getLiving().getPosition().getY() + i;
                    BlockPos pos = new BlockPos(x, y, z);
                    World world = data.getLiving().world;

                    if (world.isAirBlock(pos))
                    {
                        continue;
                    }

                    BlockState state = world.getBlockState(pos);
                    state.randomTick((ServerWorld) world, pos, world.rand);

                    break;
                }

                //todo tick living entities?

                List<LivingEntity> entitiesToCheck = getLivingEntitiesInRange(data.getLiving(), range, true);

                for (LivingEntity e : entitiesToCheck)
                {
                    e.livingTick();
                }
            }
        }


    }


}
