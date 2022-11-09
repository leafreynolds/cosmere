package leaf.cosmere.sandmastery.common.items;

import leaf.cosmere.common.items.ChargeableItemBase;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.sandmastery.common.itemgroups.SandmasteryItemGroups;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocksRegistry;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import leaf.cosmere.sandmastery.common.sandpouch.SandPouchContainerMenu;
import leaf.cosmere.sandmastery.common.sandpouch.SandPouchInventory;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import leaf.cosmere.sandmastery.common.utils.SandmasteryConstants;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class SandPouchItem extends ChargeableItemBase {
    public SandPouchItem() {
        super(PropTypes.Items.ONE.get().tab(SandmasteryItemGroups.ITEMS));
    }
    private SandPouchInventory sandPouchInventory;

    public static final Predicate<ItemStack> SUPPORTED_ITEMS = (itemStack) ->
    {
        if(itemStack.getItem() == SandmasteryBlocksRegistry.TALDAIN_SAND.asItem()) return true;
        if(itemStack.getItem() == SandmasteryBlocksRegistry.TALDAIN_SAND_LAYER.asItem()) return true;
        return false;
    };

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        MiscHelper.chargeItemFromInvestiture(pStack, pLevel, pEntity, getMaxCharge(pStack));
    }

    @Override
    public int getMaxCharge(ItemStack itemStack) {
        int res = 0;
        IItemHandler inv = itemStack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
        if(inv == null) return res;
        for(int i = 0; i < SandPouchInventory.size; i++) {
            ItemStack stack = inv.getStackInSlot(i);
            res += MiscHelper.getChargeFromItemStack(stack);
        }
        return res;
    }

    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> stacks)
    {
        if (allowedIn(tab))
        {
            stacks.add(new ItemStack(this));
        }
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack)
    {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack pouchStack = player.getItemInHand(interactionHand);
        if (interactionHand == InteractionHand.MAIN_HAND) {
            if(!player.level.isClientSide() && player instanceof ServerPlayer) {
                MenuProvider container = new SimpleMenuProvider((windowID, playerInv, plyer) ->
                        new SandPouchContainerMenu(windowID, playerInv, pouchStack), pouchStack.getHoverName());
                NetworkHooks.openScreen((ServerPlayer) player, container, buf -> buf.writeBoolean(true));

                System.out.println();
            }
        }
        return InteractionResultHolder.consume(pouchStack);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag oldCapNbt)
    {
        this.sandPouchInventory = new SandPouchInventory();
        //sandPouchInventory.deserializeNBT(oldCapNbt); // todo check if this breaks things?
        return this.sandPouchInventory;
    }
}
