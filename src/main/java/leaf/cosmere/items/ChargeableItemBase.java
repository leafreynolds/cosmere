/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.charge.IChargeable;
import leaf.cosmere.utils.helpers.TextHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public abstract class ChargeableItemBase extends BaseItem implements IChargeable
{
	public ChargeableItemBase(Item.Properties prop)
	{
		super(prop);
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> stacks)
	{
		if (allowdedIn(tab))
		{
			stacks.add(new ItemStack(this));

			ItemStack fullPower = new ItemStack(this);
			setCharge(fullPower, getMaxCharge(fullPower));
			stacks.add(fullPower);
		}
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, Level world)
	{
		return Integer.MAX_VALUE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
	{
		String attunedPlayerName = getAttunedPlayerName(stack);
		UUID attunedPlayer = getAttunedPlayer(stack);
		if (attunedPlayer != null)
		{
			tooltip.add(TextHelper.createText(attunedPlayerName));
		}
		tooltip.add(TextHelper.createText(String.format("%s/%s", getCharge(stack), getMaxCharge(stack))).withStyle(ChatFormatting.GRAY));
	}

	@Override
	public boolean isBarVisible(ItemStack stack)
	{
		if (stack.getItem() instanceof IChargeable)
		{
			IChargeable item = (IChargeable) stack.getItem();
			return item.getCharge(stack) > 1;
		}
		return false;
	}

	@Override
	public int getBarWidth(ItemStack stack)
	{
		if (stack.getItem() instanceof IChargeable)
		{
			IChargeable item = (IChargeable) stack.getItem();

			int maxCharge = item.getMaxCharge(stack);
			int charge = item.getCharge(stack);

			return getBarWidth(charge, maxCharge);
		}

		return 13;
	}

	@Override
	public boolean isFoil(ItemStack stack)
	{
		if (stack.getItem() instanceof IChargeable)
		{
			IChargeable item = (IChargeable) stack.getItem();
			return item.getCharge(stack) > 0;
		}
		return false;
	}
}
