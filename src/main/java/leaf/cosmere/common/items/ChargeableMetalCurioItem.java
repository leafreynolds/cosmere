/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.common.items;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.properties.PropTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class ChargeableMetalCurioItem extends ChargeableItemBase implements IHasMetalType, ICurioItem
{
	private final Metals.MetalType metalType;

	public ChargeableMetalCurioItem(Metals.MetalType metalType)
	{
		super(PropTypes.Items.ONE.get().rarity(metalType.getRarity()));
		this.metalType = metalType;
	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return this.metalType;
	}


	//@Override
	//public boolean showAttributesTooltip(String identifier, ItemStack stack)
	//{
	//	return false;
	//}

	@Override
	public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack)
	{
		return ICurioItem.super.getAttributesTooltip(tooltips, stack);
	}

	@Override
	public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack)
	{
		return makesPiglinsNeutral(stack, slotContext.entity());
	}

	@Override
	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer)
	{
		return this.metalType == Metals.MetalType.GOLD;
	}

	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack)
	{
		ICurioItem.super.onUnequip(slotContext, newStack, stack);

		//Clear any nicrosil feruchemy based powers
		if (this.metalType == Metals.MetalType.NICROSIL)
		{
			SpiritwebCapability.get(slotContext.entity()).ifPresent(data ->
			{
				for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
				{
					Attribute attribute = manifestation.getAttribute();
					if (attribute == null)
					{
						continue;
					}

					final AttributeInstance attributeInstance = data.getLiving().getAttribute(attribute);
					if (attributeInstance != null)
					{
						attributeInstance.removeModifier(Constants.NBT.FERU_NICROSIL_UUID);
					}
				}
			});
		}
	}
}
