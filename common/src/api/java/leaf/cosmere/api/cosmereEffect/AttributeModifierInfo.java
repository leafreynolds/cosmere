/*
 * File updated ~ 29 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.api.cosmereEffect;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class AttributeModifierInfo
{
	private final Attribute attribute;
	private double amountPerPointOfStrength;
	private AttributeModifier.Operation operation;

	public AttributeModifierInfo(Attribute attribute, double amountPerPointOfStrength, AttributeModifier.Operation operation)
	{
		this.attribute = attribute;
		this.amountPerPointOfStrength = amountPerPointOfStrength;
		this.operation = operation;
	}

	public Attribute getAttribute()
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
		compoundTag.putString("attribute_id", ForgeRegistries.ATTRIBUTES.getKey(attribute).toString());
		compoundTag.putDouble("amount", amountPerPointOfStrength);
		compoundTag.putInt("operation", operation.toValue());

		return compoundTag;
	}

	public static AttributeModifierInfo load(CompoundTag tag)
	{
		final ResourceLocation attributeID = new ResourceLocation(tag.getString("attribute_id"));
		//check if this attribute still exists in the registry. Maybe a mod got uninstalled
		if (ForgeRegistries.ATTRIBUTES.containsKey(attributeID))
		{
			Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(attributeID);
			double amount = tag.getDouble("amount");
			AttributeModifier.Operation op = AttributeModifier.Operation.fromValue(tag.getInt("operation"));

			return new AttributeModifierInfo(attribute, amount, op);
		}

		return null;
	}
}
