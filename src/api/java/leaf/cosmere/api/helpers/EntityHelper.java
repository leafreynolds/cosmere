/*
 * File updated ~ 12 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api.helpers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class EntityHelper
{
	public static List<LivingEntity> getLivingEntitiesInRange(LivingEntity livingEntity, int range, boolean includeSelf)
	{
		AABB areaOfEffect = new AABB(livingEntity.blockPosition()).inflate(range, range, range);
		return livingEntity.level.getEntitiesOfClass(LivingEntity.class, areaOfEffect, e -> includeSelf || e != livingEntity);
	}

	public static List<Entity> getEntitiesInRange(Entity entity, int range, boolean includeSelf)
	{
		AABB areaOfEffect = new AABB(entity.blockPosition()).inflate(range, range, range);
		return entity.level.getEntitiesOfClass(Entity.class, areaOfEffect, e -> includeSelf || e != entity);
	}


}
