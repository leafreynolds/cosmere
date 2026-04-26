/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.items;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.items.ChargeableMetalCurioItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class RingMetalmindItem extends ChargeableMetalCurioItem
{
	public RingMetalmindItem(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public float getMaxChargeModifier()
	{
		return (4f / 9f);
	}
}
