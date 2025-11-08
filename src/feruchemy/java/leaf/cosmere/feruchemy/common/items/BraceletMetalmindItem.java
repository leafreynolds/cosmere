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

public class BraceletMetalmindItem extends ChargeableMetalCurioItem
{
	public BraceletMetalmindItem(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public float getMaxChargeModifier()
	{
		return (5f / 9f);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
	{
		super.appendHoverText(stack, worldIn, tooltip, flagIn);

		if (this.getMetalType() == Metals.MetalType.NICROSIL && !(stack.getItem() instanceof BandsOfMourningItem))
		{
			CompoundTag nbt = stack.getOrCreateTagElement("StoredInvestiture");

			tooltip.add(Component.empty());
			tooltip.add(Component.literal("When tapping:").withStyle(ChatFormatting.GOLD));

			for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
			{
				Attribute attribute = manifestation.getAttribute();
				final String attributeRegistryName = manifestation.getRegistryName().toString();
				if (!CompoundNBTHelper.verifyExistance(nbt, attributeRegistryName) || attribute == null)
				{
					continue;
				}

				int strength = CompoundNBTHelper.getInt(
						nbt,
						attributeRegistryName,
						0);

				tooltip.add(Component.literal("+" + strength + " ").append(Component.translatable(manifestation.getTranslationKey())).withStyle(ChatFormatting.BLUE));

			}
		}
	}

}
