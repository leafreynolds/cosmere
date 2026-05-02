/*
 * File updated ~ 2026-05-02 ~ Leaf
 */

package leaf.cosmere.common.util;

import leaf.cosmere.api.Manifestations.ManifestationTypes;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.Roshar;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

public class CosmereAttributeUtils
{
	public static Attribute getAttribute(ManifestationTypes manifestationType, int powerId)
	{
		switch (manifestationType)
		{
			case ALLOMANCY:
			case FERUCHEMY:
				return BuiltInRegistries.ATTRIBUTE.get(ResourceLocation.fromNamespaceAndPath(
						manifestationType.getName(),
						Metals.MetalType.valueOf(powerId).get().getName()));
			case SURGEBINDING:
				return BuiltInRegistries.ATTRIBUTE.get(ResourceLocation.fromNamespaceAndPath(
						manifestationType.getName(),
						Roshar.Surges.valueOf(powerId).get().getName()
				));
			case SANDMASTERY:
				return BuiltInRegistries.ATTRIBUTE.get(ResourceLocation.fromNamespaceAndPath(
						manifestationType.getName(),
						"ribbons"
				));
			case AVIAR:
				return BuiltInRegistries.ATTRIBUTE.get(ResourceLocation.fromNamespaceAndPath(
						manifestationType.getName(),
						"hostile_life_sense"
				));
			default:
				return null;
		}
	}

    public static int getAttributePowerId(Attribute attribute)
    {
        switch (getManifestationType(attribute))
        {
            case ALLOMANCY:
            case FERUCHEMY:
                String metalName = attribute.getDescriptionId().split("\\.")[2];
                return Metals.MetalType.valueOf(metalName.toUpperCase()).getID();
            case SURGEBINDING:
                String surgeName = attribute.getDescriptionId().split("\\.")[2];
                return Roshar.Surges.valueOf(surgeName.toUpperCase()).getID();
            default:
                return 0;
        }
    }

	public static Attribute getAttributeByDescriptionId(String id)
	{
		String[] attributeSections = id.split("\\.");
		return BuiltInRegistries.ATTRIBUTE.get(ResourceLocation.fromNamespaceAndPath(
				attributeSections[1],
				attributeSections[2]
		));
	}

	public static ManifestationTypes getManifestationType(Attribute attribute)
	{
		if(attribute == null) return ManifestationTypes.NONE;
		String modId = attribute.getDescriptionId().split("\\.")[1];

		for (ManifestationTypes manifestationType : ManifestationTypes.values())
		{
			if (manifestationType.getName().equals(modId))
			{
				return manifestationType;
			}
		}
		return ManifestationTypes.NONE;
	}

	public static void grantBaseAttribute(LivingEntity livingEntity, RangedAttribute attribute, int strength)
	{
		int currentStrength = 0;
		AttributeInstance entityAttributeInstance = livingEntity.getAttribute(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute));
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

	public static void removeBaseAttribute(LivingEntity livingEntity, Attribute attribute)
	{
		AttributeInstance entityAttributeInstance = livingEntity.getAttribute(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute));
		if (entityAttributeInstance == null)
		{
			return;
		}
		entityAttributeInstance.setBaseValue(0);
	}

    public static ItemStack getPowerItem(Player player, int itemSlot, boolean isCurio)
    {
        if(isCurio)
        {
            Optional<ICuriosItemHandler> curiosItemHandler = CuriosApi.getCuriosInventory(player);
            if (curiosItemHandler.isPresent())
            {
                return curiosItemHandler.get().getEquippedCurios().getStackInSlot(itemSlot);
            }
            else
            {
                return ItemStack.EMPTY;
            }
        }
        else
        {
            Inventory playerInventory = player.getInventory();
            return playerInventory.getItem(itemSlot);
        }
    }
}
