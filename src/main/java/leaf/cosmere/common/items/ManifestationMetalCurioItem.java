/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.common.items;

import leaf.cosmere.api.*;
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

public class ManifestationMetalCurioItem extends BaseItem implements IHasMetalType, ICurioItem, IHasManifestations
{
	private final Metals.MetalType metalType;

	public ManifestationMetalCurioItem(Metals.MetalType metalType)
	{
		super(PropTypes.Items.ONE.get().rarity(metalType.getRarity()));
		this.metalType = metalType;
	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return this.metalType;
	}


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
	}

	@Override
	public int getMaxCapacity()
	{
		return 0;
	}
}
