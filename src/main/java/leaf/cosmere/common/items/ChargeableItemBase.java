/*
 * File updated ~ 11 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.common.items;

import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.common.charge.IChargeable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public abstract class ChargeableItemBase extends BaseItem implements IChargeable
{
	public ChargeableItemBase(Properties prop)
	{
		super(prop);
	}

	public void addFilled(CreativeModeTab.Output output)
	{
		output.accept(new ItemStack(this));
		ItemStack fullPower = new ItemStack(this);
		setCharge(fullPower, getMaxCharge(fullPower));
		output.accept(fullPower);
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
	public boolean isBarVisible(@NotNull ItemStack stack)
	{
		return getCharge(stack) > 1;
	}

	@Override
	public int getBarWidth(@NotNull ItemStack stack)
	{
		int maxCharge = getMaxCharge(stack);
		int charge = getCharge(stack);

		return getBarWidth(charge, maxCharge);
	}

	@Override
	public int getBarColor(@NotNull ItemStack stack)
	{
		return getBarColour(getCharge(stack), getMaxCharge(stack));
	}

	@Override
	public boolean isFoil(@NotNull ItemStack stack)
	{
		return getCharge(stack) > 0;
	}
}
