/*
 * File updated ~ 29 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.api.cosmereEffect;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AttributeModifierInfo
{
	private final Holder<Attribute> attribute;
	private double amountPerPointOfStrength;
	private AttributeModifier.Operation operation;

	public AttributeModifierInfo(Holder<Attribute> attribute, double amountPerPointOfStrength, AttributeModifier.Operation operation)
	{
		this.attribute = attribute;
		this.amountPerPointOfStrength = amountPerPointOfStrength;
		this.operation = operation;
	}

	public Holder<Attribute> getAttribute()
	{
		return attribute;
	}

	public double getAmount()
	{
		return amountPerPointOfStrength;
	}

	public AttributeModifier.Operation getOperation()
	{
		return operation;
	}

	public void update(double strength, AttributeModifier.Operation operation)
	{
		this.amountPerPointOfStrength = strength;
		this.operation = operation;
	}

	public Tag save(CompoundTag compoundTag)
	{
		compoundTag.putString("attribute_id", BuiltInRegistries.ATTRIBUTE.getKey(attribute.value()).toString());
		compoundTag.putDouble("amount", amountPerPointOfStrength);
		compoundTag.putString("operation", operation.name());

		return compoundTag;
	}

	public static AttributeModifierInfo load(CompoundTag tag)
	{
		final ResourceLocation attributeID = ResourceLocation.tryParse(tag.getString("attribute_id"));
		//check if this attribute still exists in the registry. Maybe a mod got uninstalled
		if (BuiltInRegistries.ATTRIBUTE.containsKey(attributeID))
		{
			Holder<Attribute> attribute = BuiltInRegistries.ATTRIBUTE.getHolder(attributeID).get();
			double amount = tag.getDouble("amount");
			AttributeModifier.Operation op = AttributeModifier.Operation.valueOf(tag.getString("operation"));

			return new AttributeModifierInfo(attribute, amount, op);
		}

		return null;
	}
}
