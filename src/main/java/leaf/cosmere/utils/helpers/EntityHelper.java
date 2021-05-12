/*
 * File created ~ 25 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class EntityHelper
{
    public static List<LivingEntity> getLivingEntitiesInRange(LivingEntity livingEntity, int range, boolean includeSelf)
    {
        AxisAlignedBB areaOfEffect = new AxisAlignedBB(livingEntity.getPosition());
        areaOfEffect = areaOfEffect.expand(range, range, range);

        List<LivingEntity> entitiesFound = livingEntity.world.getEntitiesWithinAABB(LivingEntity.class, areaOfEffect);

        if (!includeSelf && entitiesFound.contains(livingEntity))
        {
            entitiesFound.remove(livingEntity);
        }

        return entitiesFound;
    }

    public static List<Entity> getEntitiesInRange(Entity entity, int range, boolean includeSelf)
    {
        AxisAlignedBB areaOfEffect = new AxisAlignedBB(entity.getPosition());
        areaOfEffect = areaOfEffect.expand(range, range, range);

        List<Entity> entitiesFound = entity.world.getEntitiesWithinAABB(Entity.class, areaOfEffect);

        if (!includeSelf && entitiesFound.contains(entity))
        {
            entitiesFound.remove(entity);
        }

        return entitiesFound;
    }
}
