package leaf.cosmere.sandmastery.common.items;

import leaf.cosmere.common.items.ChargeableItemBase;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.sandmastery.common.itemgroups.SandmasteryItemGroups;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nonnull;

public class SandJarItem extends ChargeableItemBase {
    public SandJarItem() {
        super(PropTypes.Items.ONE.get().tab(SandmasteryItemGroups.ITEMS));
    }

    @Override
    public int getMaxCharge(ItemStack itemStack) {
        return Mth.floor(100);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack)
    {
        return true;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if(pLevel.isClientSide()) return;
        if(MiscHelper.checkIfNearbyInvestiture((ServerLevel) pLevel, pEntity.blockPosition())) { adjustCharge(pStack, 1); }
    }

    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> stacks)
    {
        if (allowedIn(tab))
        {
            stacks.add(new ItemStack(this));

            ItemStack halfPower = new ItemStack(this);
            setCharge(halfPower, getMaxCharge(halfPower)/2);
            stacks.add(halfPower);

            ItemStack fullPower = new ItemStack(this);
            setCharge(fullPower, getMaxCharge(fullPower));
            stacks.add(fullPower);
        }
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack)
    {
        return false;
    }

}
