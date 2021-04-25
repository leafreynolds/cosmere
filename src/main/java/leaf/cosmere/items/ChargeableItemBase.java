/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.charge.IChargeable;
import leaf.cosmere.helpers.TextHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
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
    public void fillItemGroup(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> stacks)
    {
        if (isInGroup(tab))
        {
            stacks.add(new ItemStack(this));

            ItemStack fullPower = new ItemStack(this);
            setCharge(fullPower, getMaxCharge(fullPower));
            stacks.add(fullPower);
        }
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world)
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        if (stack.getItem() instanceof IChargeable)
        {
            IChargeable item = (IChargeable) stack.getItem();
            return item.getCharge(stack) > 1;
        }
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        String attunedPlayerName = getAttunedPlayerName(stack);
        UUID attunedPlayer = getAttunedPlayer(stack);
        if (attunedPlayer != null)
        {
            tooltip.add(TextHelper.createText(attunedPlayerName));
        }
        tooltip.add(TextHelper.createText(String.format("%s/%s", getCharge(stack), getMaxCharge(stack))).mergeStyle(TextFormatting.GRAY));
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        if (stack.getItem() instanceof IChargeable)
        {
            IChargeable item = (IChargeable) stack.getItem();

            int maxCharge = item.getMaxCharge(stack);
            int charge = item.getCharge(stack);

            return (double) (maxCharge - charge) / (double) maxCharge;
        }

        return 1d;
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        if (stack.getItem() instanceof IChargeable)
        {
            IChargeable item = (IChargeable) stack.getItem();
            return item.getCharge(stack) > 0;
        }
        return false;
    }
}
