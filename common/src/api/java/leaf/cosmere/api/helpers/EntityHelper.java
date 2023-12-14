/*
 * File updated ~ 26 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.api.helpers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
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

	public static double getAttributeValue(LivingEntity entity, Attribute attribute)
	{
		return getAttributeValue(entity, attribute, 0);
	}

	public static double getAttributeValue(LivingEntity entity, Attribute attribute, double defaultStrength)
	{
		final AttributeMap attributes = entity.getAttributes();
		if (attributes.hasAttribute(attribute))
		{
			return attributes.getValue(attribute);
		}

		return defaultStrength;
	}
}
