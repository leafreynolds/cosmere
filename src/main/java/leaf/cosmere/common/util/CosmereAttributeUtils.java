package leaf.cosmere.common.util;

import leaf.cosmere.api.Manifestations.ManifestationTypes;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.Roshar;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.ForgeRegistries;

public class CosmereAttributeUtils
{
	public static Attribute getAttribute(ManifestationTypes manifestationType, int powerId)
	{
		switch (manifestationType)
		{
			case ALLOMANCY:
			case FERUCHEMY:
				return ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(
						manifestationType.getName(),
						Metals.MetalType.valueOf(powerId).get().getName()));
			case SURGEBINDING:
				return ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(
						manifestationType.getName(),
						Roshar.Surges.valueOf(powerId).get().getName()
				));
			case SANDMASTERY:
				return ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(
						manifestationType.getName(),
						"ribbons"
				));
			case AVIAR:
				return ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(
						manifestationType.getName(),
						"hostile_life_sense"
				));
			default:
				return null;
		}
	}

	public static void grantBaseAttribute(LivingEntity livingEntity, RangedAttribute attribute, int strength)
	{
		int currentStrength = 0;
		AttributeInstance entityAttributeInstance = livingEntity.getAttribute(attribute);
		if (entityAttributeInstance == null)
		{
			return;
		}

		currentStrength = (int) entityAttributeInstance.getValue();

		// Let's ensure not to exceed the base value if it's out of range,
		// even if it will get sanitized
		int newStrength = strength + currentStrength;
		if (newStrength < attribute.getMinValue())
		{
			newStrength = (int) attribute.getMinValue();
		}
		else if (newStrength > attribute.getMaxValue())
		{
			newStrength = (int) attribute.getMaxValue();
		}

		entityAttributeInstance.setBaseValue(newStrength);
	}
}
