/*
 * File created ~ 4 - 3 - 2025 ~ Nova
 */

package leaf.cosmere.feruchemy.common.items;

import com.google.common.collect.Multimap;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

/**
 * Interface defining Feruchemical item behavior, allowing storage and tapping of investiture.
 */
public interface IFeruchemyInfo {

	/**
	 * Stores a specific power into a metalmind.
	 * @param stack The metalmind item.
	 * @param manifestation The power being stored.
	 * @param level The strength of the stored power.
	 * @param identity The UUID of the user storing the power.
	 */
	void storePower(ItemStack stack, Manifestation manifestation, double level, UUID identity);

	/**
	 * Taps a stored power from a metalmind.
	 * @param stack The metalmind item.
	 * @param manifestation The power being tapped.
	 * @param level The amount of power being extracted.
	 */
	void tapPower(ItemStack stack, Manifestation manifestation, double level);

	/**
	 * Adds Feruchemical attributes to an item, modifying player stats.
	 * @param attributeModifiers The map of attributes and their corresponding modifiers.
	 * @param stack The item to which attributes are applied.
	 * @param metalType The type of metal determining attribute effects.
	 */
	void addFeruchemicalAttributes(Multimap<Attribute, AttributeModifier> attributeModifiers, ItemStack stack, Metals.MetalType metalType);

	/**
	 * Retrieves the strength of a stored Feruchemical power from an item.
	 * @param stack The metalmind item.
	 * @param manifestation The power being checked.
	 * @return The stored strength of the power.
	 */
	default double getFeruchemicalStrength(ItemStack stack, Manifestation manifestation) {
		return 0;
	}

	/**
	 * Retrieves the strength of a Feruchemical charge based on metal type.
	 * @param stack The metalmind item.
	 * @param metalType The metal type associated with the stored power.
	 * @return The stored strength of the power.
	 */
	default double getFeruchemicalStrength(ItemStack stack, Metals.MetalType metalType) {
		return 0;
	}

	/**
	 * Retrieves the UUID of the entity that stored investiture in the metalmind.
	 * @param stack The metalmind item.
	 * @return The UUID of the original user, or null if not set.
	 */
	default UUID getFeruchemicalIdentity(ItemStack stack) {
		return null;
	}

	/**
	 * Sets the strength of a stored Feruchemical power in an item.
	 * @param stack The metalmind item.
	 * @param manifestation The power being modified.
	 * @param val The new strength value.
	 */
	default void setFeruchemicalStrength(ItemStack stack, Manifestation manifestation, double val) {
	}

	/**
	 * Sets the identity (UUID) of the entity that stored investiture in the metalmind.
	 * @param stack The metalmind item.
	 * @param identity The UUID of the user storing power.
	 */
	default void setFeruchemicalIdentity(ItemStack stack, UUID identity) {
	}

	/**
	 * Retrieves the total Feruchemical charge stored in the metalmind.
	 * @param stack The metalmind item.
	 * @return The amount of stored charge.
	 */
	default double getFeruchemicalCharge(ItemStack stack) {
		return 0;
	}

	/**
	 * Sets the total Feruchemical charge in the metalmind.
	 * @param stack The metalmind item.
	 * @param charge The new charge value.
	 */
	default void setFeruchemicalCharge(ItemStack stack, double charge) {
	}

	/**
	 * Determines if the given metal type is Nicrosil.
	 * Nicrosil is unique because it stores investiture itself rather than physical or cognitive attributes.
	 * @param metalType The metal type being checked.
	 * @return True if the metal is Nicrosil, false otherwise.
	 */
	default boolean isNicrosilMetalmind(Metals.MetalType metalType) {
		return metalType == Metals.MetalType.NICROSIL;
	}
}
