/*
 * File created ~ 25 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.helpers;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class EntityHelper
{
    public static List<LivingEntity> getEntitiesInRange(LivingEntity livingEntity, int range, boolean includeSelf)
    {
        AxisAlignedBB areaOfEffect = new AxisAlignedBB(livingEntity.getPosition());
        areaOfEffect = areaOfEffect.expand(range,range,range);

        List<LivingEntity> entitiesToApplyEffect = livingEntity.world.getEntitiesWithinAABB(LivingEntity.class, areaOfEffect);

        if (!includeSelf && entitiesToApplyEffect.contains(livingEntity))
        {
            entitiesToApplyEffect.remove(livingEntity);
        }

        return entitiesToApplyEffect;
    }
}
