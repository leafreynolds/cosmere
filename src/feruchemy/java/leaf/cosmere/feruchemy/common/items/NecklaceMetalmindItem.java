/*
 * File updated ~ 4 - 3 - 2025 ~ Nova
 */

package leaf.cosmere.feruchemy.common.items;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.items.ChargeableMetalCurioItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

/**
 * Represents a necklace metalmind item that can store and tap Investiture.
 * Implements IFeruchemyInfo to handle Feruchemical attributes and charge manipulation.
 */
public class NecklaceMetalmindItem extends ChargeableMetalCurioItem implements IFeruchemyInfo
{

	/**
	 * Constructor for the NecklaceMetalmindItem.
	 * @param metalType The type of metal this metalmind is made from.
	 */
	public NecklaceMetalmindItem(Metals.MetalType metalType)
	{
		super(metalType);
	}

	/**
	 * Retrieves the attribute modifiers applied when the metalmind is worn.
	 * Only applies attributes if the item is Nicrosil, is currently tapped, and belongs to the entity using it.
	 * Yes, I know this is a lot of spaghetti, but it works so im not touching it.
	 *
	 * @param slotContext The context in which the item is equipped.
	 * @param uuid The UUID of the entity using the item.
	 * @param stack The item stack representing this metalmind.
	 * @return A map of attributes and their corresponding modifiers.
	 */
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack)
	{
		Multimap<Attribute, AttributeModifier> attributeModifiers = LinkedHashMultimap.create();

		// Check if the metalmind is Nicrosil, is currently tapped, and is usable by the entity.
		if (isNicrosilMetalmind(getMetalType()) && isTapped(stack) && canTap(stack, slotContext.entity().getUUID()))
		{
			Metals.MetalType metalType = getMetalType();

			// If the stack implements IFeruchemyInfo, apply Feruchemical attributes.
			if (stack.getItem() instanceof IFeruchemyInfo)
			{
				((IFeruchemyInfo) stack.getItem()).addFeruchemicalAttributes(attributeModifiers, stack, metalType);
			}
		}

		return attributeModifiers;
	}

	/**
	 * Checks if the metalmind currently has stored Investiture available for tapping.
	 * @param stack The metalmind item stack.
	 * @return True if the item has charge left, false otherwise.
	 */
	private boolean isTapped(ItemStack stack)
	{
		return getFeruchemicalCharge(stack) > 0;
	}

	/**
	 * Determines if the entity has permission to tap the metalmind.
	 * A metalmind can be tapped if it has no recorded identity or if the UUID matches the entity's.
	 *
	 * @param stack The metalmind item stack.
	 * @param entityUUID The UUID of the entity trying to tap the metalmind.
	 * @return True if the entity can tap from this metalmind, false otherwise.
	 */
	private boolean canTap(ItemStack stack, UUID entityUUID)
	{
		UUID feruchemicalIdentity = getFeruchemicalIdentity(stack);
		return feruchemicalIdentity == null || feruchemicalIdentity.equals(entityUUID);
	}

	/**
	 * Stores a specific manifestation of Investiture into the metalmind.
	 * This function only works if the metalmind is made of Nicrosil.
	 *
	 * @param stack The metalmind item stack.
	 * @param manifestation The power being stored.
	 * @param level The intensity of the stored power.
	 * @param identity The UUID of the entity storing the power.
	 */
	@Override
	public void storePower(ItemStack stack, Manifestation manifestation, double level, UUID identity)
	{
		if (isNicrosilMetalmind(getMetalType()))
		{
			setFeruchemicalStrength(stack, manifestation, level);
			setFeruchemicalIdentity(stack, identity);
		}
	}

	/**
	 * Taps stored Investiture from the metalmind, reducing its charge.
	 * Only works if the metalmind is made of Nicrosil.
	 *
	 * @param stack The metalmind item stack.
	 * @param manifestation The power being tapped.
	 * @param level The amount of power being extracted.
	 */
	@Override
	public float getMaxChargeModifier()
	{
		return (6f / 9f);
	}


	@Override
	public void tapPower(ItemStack stack, Manifestation manifestation, double level)
	{
		if (isNicrosilMetalmind(getMetalType()))
		{
			double currentCharge = getFeruchemicalCharge(stack);

			// Reduce charge by the requested level, ensuring it does not go below zero.
			if (currentCharge >= level)
			{
				setFeruchemicalCharge(stack, currentCharge - level);
			}
			else
			{
				setFeruchemicalCharge(stack, 0);
			}

			// If the charge is zero, remove all attached powers.
			if (getFeruchemicalCharge(stack) == 0)
			{
				clearStoredPowers(stack);
			}
		}
	}

	/**
	 * Clears all stored powers from the metalmind.
	 * @param stack The metalmind item stack.
	 */
	private void clearStoredPowers(ItemStack stack)
	{
		if (stack.getTag() != null)
		{
			for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
			{
				setFeruchemicalStrength(stack, manifestation, 0);
				Attribute regAttribute = manifestation.getAttribute();
				if (regAttribute != null)
				{
					stack.getOrCreateTag().remove(regAttribute.getDescriptionId());
				}
			}
			setFeruchemicalIdentity(stack, null);
			stack.getOrCreateTag().remove("StoredInvestiture");
		}
	}

	/**
	 * Adds Feruchemical attribute modifiers to the item based on stored Investiture.
	 * This method applies the stored powers as attribute bonuses, allowing the user to benefit from them.
	 * My terrible and quite possibly broken implementation of Nicrosil feruchemy as it should be.
	 *
	 * @param attributeModifiers The attribute modifier map where bonuses will be added.
	 * @param stack The metalmind item stack.
	 * @param metalType The type of metal, which determines the kind of power stored.
	 */
	@Override
	public void addFeruchemicalAttributes(Multimap<Attribute, AttributeModifier> attributeModifiers, ItemStack stack, Metals.MetalType metalType)
	{
		// Only apply attributes if the metalmind is Nicrosil, which stores Investiture itself.
		if (isNicrosilMetalmind(metalType))
		{
			UUID feruchemicalIdentity = getFeruchemicalIdentity(stack);

			// Loop through all available manifestations of Investiture.
			for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
			{
				String path = manifestation.getName();
				double feruchemicalStrength = getFeruchemicalStrength(stack, manifestation);

				// If Investiture has been stored, apply its effects as an attribute modifier.
				if (feruchemicalStrength > 0)
				{
					Attribute regAttribute = manifestation.getAttribute();
					if (regAttribute != null)
					{
						attributeModifiers.put(
								regAttribute,
								new AttributeModifier(
										feruchemicalIdentity,
										String.format("Feruchemical-%s: %s", path, feruchemicalIdentity),
										feruchemicalStrength,
										AttributeModifier.Operation.ADDITION));
					}
				}
			}
		}
	}
}
