/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api.helpers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class EntityHelper
{
	public static List<LivingEntity> getLivingEntitiesInRange(LivingEntity selfEntity, int range, boolean includeSelf)
	{
		AABB areaOfEffect = new AABB(selfEntity.blockPosition());
		areaOfEffect = areaOfEffect.inflate(range, range, range);

		List<LivingEntity> entitiesFound = selfEntity.level.getEntitiesOfClass(LivingEntity.class, areaOfEffect);

		if (!includeSelf)
		{
			//removes self entity if it exists in the list
			//otherwise list unchanged
			entitiesFound.remove(selfEntity);
		}

		return entitiesFound;
	}

	public static List<Entity> getEntitiesInRange(Entity entity, int range, boolean includeSelf)
	{
		AABB areaOfEffect = new AABB(entity.blockPosition()).inflate(range, range, range);
		return entity.level.getEntitiesOfClass(Entity.class, areaOfEffect, e -> includeSelf || e != entity);
	}


}
