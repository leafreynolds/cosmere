/*
 * File created ~ 22 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.items;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.capabilities.HemalurgyItemCapabilities;
import leaf.cosmere.hemalurgy.common.config.HemalurgyConfigs;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyAttributes;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import static leaf.cosmere.common.registry.CosmereDamageTypesRegistry.SPIKED;

//curio behaviour shared by spike items (ICurioItem) and data-map spikes (SpikeCurio)
public final class SpikeCurioLogic
{
	private SpikeCurioLogic()
	{
	}

	public static boolean canUnequip(SlotContext context, ItemStack stack)
	{
		boolean hasBindingCurse = EnchantmentHelper.has(stack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE);
		return (!hasBindingCurse || (context.entity() instanceof Player player && player.isCreative()));
	}

	/**
	 * generate new map of attributes for when used as a curio item.
	 */
	public static Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(IHemalurgicInfo spike, SlotContext slotContext, ItemStack stack)
	{
		Multimap<Holder<Attribute>, AttributeModifier> attributeModifiers = LinkedHashMultimap.create();

		Metals.MetalType metalType = spike.getSpikeMetalType(stack);
		if (metalType == null)
		{
			return attributeModifiers;
		}

		//add hemalurgic attributes, if any.
		spike.getHemalurgicAttributes(attributeModifiers, stack, metalType);

		// add spiritweb buffs, if any
		UUID hemalurgicIdentity = spike.getHemalurgicIdentity(stack);
		if (hemalurgicIdentity != null)
		{
			int spiritwebIntegrity = -1;
			if (slotContext.identifier().equals("linchpin"))
			{
				// linchpin always gives a bonus (+3 default)
				spiritwebIntegrity += HemalurgyConfigs.SERVER.LINCHPIN_SPIKE_SPIRITWEB_BONUS.get();
				Manifestation aPewter = CosmereAPI.manifestationRegistry().get(ResourceLocation.fromNamespaceAndPath("allomancy", Metals.MetalType.PEWTER.getName()));
				if (aPewter != null && attributeModifiers.containsKey(aPewter.getAttribute()))
				{
					// pewter gives an additional bonus (+3 default)
					spiritwebIntegrity += HemalurgyConfigs.SERVER.ALLOMANTIC_PEWTER_SPIRITWEB_BONUS.get();
				}
				Manifestation fGold = CosmereAPI.manifestationRegistry().get(ResourceLocation.fromNamespaceAndPath("feruchemy", Metals.MetalType.GOLD.getName()));
				if (fGold != null && attributeModifiers.containsKey(fGold.getAttribute()))
				{
					// f-gold gives an extra bonus (+6 default)
					spiritwebIntegrity += HemalurgyConfigs.SERVER.FERUCHEMICAL_GOLD_SPIRITWEB_BONUS.get();
				}
			}

			attributeModifiers.put(HemalurgyAttributes.SPIRITWEB_INTEGRITY.getHolder(),
					new AttributeModifier(
							ResourceLocation.fromNamespaceAndPath(Hemalurgy.MODID, "spiritweb_integrity_" + hemalurgicIdentity),
							spiritwebIntegrity,
							AttributeModifier.Operation.ADD_VALUE
					));
		}

		return attributeModifiers;
	}

	public static boolean canEquip(IHemalurgicInfo spike, SlotContext slotContext, ItemStack stack)
	{
		//do not allow players to wear two spikes of the same metal empowered by the same killed entity UUID
		if (slotContext.entity() instanceof Player player)
		{
			final UUID stackWeWantToEquipUUID = spike.getHemalurgicIdentity(stack);

			Metals.MetalType stackMetal = spike.getSpikeMetalType(stack);

			if (stackWeWantToEquipUUID != null)
			{
				Predicate<ItemStack> spikePredicate = stackToFind ->
				{
					final IHemalurgicInfo foundSpike = HemalurgyItemCapabilities.getSpike(stackToFind);

					if (foundSpike == null)
					{
						return false;
					}

					final UUID foundSpikeUUID = foundSpike.getHemalurgicIdentity(stackToFind);
					final Metals.MetalType testStackMetal = foundSpike.getSpikeMetalType(stackToFind);

					final boolean matchingSpikeIdentity = testStackMetal == stackMetal
							&& foundSpikeUUID != null
							&& foundSpikeUUID.compareTo(stackWeWantToEquipUUID) == 0;

					final boolean onlyOneIronAllowed = stackMetal == Metals.MetalType.IRON && stackMetal == testStackMetal;

					return matchingSpikeIdentity || onlyOneIronAllowed;
				};


				final Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player);

				if (curiosInventory.isEmpty())
				{
					return false;
				}

				ICuriosItemHandler curiosInv = curiosInventory.get();

				return curiosInv.findFirstCurio(spikePredicate).isEmpty();
			}
		}
		return true;
	}

	public static void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack)
	{
		//todo better logic.
		boolean isEquipping = prevStack == null || stack.getItem() != prevStack.getItem();

		if (isEquipping)
		{
			//then do hemalurgy spike logic
			//hurt the user
			//spiritweb attributes are handled in metalmind
			slotContext.entity().hurt(SPIKED.source(slotContext.entity().level()), 4);
		}
	}

	public static void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack)
	{
		//only damage if removing the spike. We can ignore replacing the spike with another spike.
		boolean isUnequipping = newStack.isEmpty() || !newStack.is(stack.getItem());
		if (isUnequipping)
		{
			slotContext.entity().hurt(SPIKED.source(slotContext.entity().level()), 4);
		}
	}
}
