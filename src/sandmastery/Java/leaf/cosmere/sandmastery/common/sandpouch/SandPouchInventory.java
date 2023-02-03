package leaf.cosmere.sandmastery.common.sandpouch;

import leaf.cosmere.sandmastery.common.items.SandPouchItem;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocksRegistry;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SandPouchInventory  implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final int size = 3;

    private enum MODES {
        ADD,
        REMOVE,
        NO_CHANGE
    }
    private final IItemHandler inv = new ItemStackHandler(size) {
        protected int layers;
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
            return slot == 0 ? !stack.isEmpty() && SandPouchItem.SUPPORTED_ITEMS.test(stack) : false;
        }

        @Override
        public void setStackInSlot(int slot, @NotNull ItemStack stack)
        {
            validateSlotIndex(slot);
            ItemStack prev = this.stacks.get(slot);
            int prevCount = prev.isEmpty() ? 0 : prev.getCount();
            int newCount = stack.isEmpty() ? 0 : stack.getCount();
            MODES mode = null;

            if(prevCount < newCount) mode = MODES.ADD;
            else if(newCount < prevCount) mode = MODES.REMOVE;
            else mode = MODES.NO_CHANGE;

            this.stacks.set(slot, stack);
            onContentsChanged(slot, Math.abs(prevCount - newCount), mode);
        }

        protected void onContentsChanged(int slot, int count, MODES mode)
        {
            ItemStack changedStack = getStackInSlot(slot);
            Item sandBlock = SandmasteryBlocksRegistry.TALDAIN_SAND.asItem();
            Item sandLayer = SandmasteryBlocksRegistry.TALDAIN_SAND_LAYER.asItem();

            switch(slot) {
                case 0:
                    if (mode != MODES.ADD) break; // This slot is input, and can accept both, don't update if it's removed
                    if (changedStack.getItem() == sandBlock) {
                        layers += 8 * count; // Blocks are worth 8 layers
                    } else if (changedStack.getItem() == sandLayer) {
                        layers += count;
                    }
                    break;
                case 1:
                    if(mode != MODES.REMOVE) break; // this slot is output only, no item filter as only one item can ever be here
                    layers -= 8 * count; // Blocks are worth 8 layers
                    break;
                case 2:
                    if(mode != MODES.REMOVE) break; // this slot is output only, no item filter as only one item can ever be here
                    layers -= count;
                    break;
            }

            updateSlots();

        }

        private void updateSlots() {
            int numBlocks = (int) Math.floor(layers / 8);
            ItemStack blocksInSlot = new ItemStack(SandmasteryBlocksRegistry.TALDAIN_SAND);
            blocksInSlot.setCount(numBlocks);

            ItemStack layersInSlot = new ItemStack(SandmasteryBlocksRegistry.TALDAIN_SAND_LAYER);
            layersInSlot.setCount(layers);
            this.stacks.set(0, ItemStack.EMPTY);
            this.stacks.set(1, blocksInSlot);
            this.stacks.set(2, layersInSlot);
        }

        @Override
        @NotNull
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
        {
            if (stack.isEmpty())
                return ItemStack.EMPTY;

            if (!isItemValid(slot, stack))
                return stack;

            validateSlotIndex(slot);

            ItemStack existing = this.stacks.get(slot);

            int limit = getStackLimit(slot, stack);

            if (!existing.isEmpty())
            {
                if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                    return stack;

                limit -= existing.getCount();
            }

            if (limit <= 0)
                return stack;

            boolean reachedLimit = stack.getCount() > limit;

            if (!simulate)
            {
                if (existing.isEmpty())
                {
                    this.stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
                }
                else
                {
                    existing.grow(reachedLimit ? limit : stack.getCount());
                }
                onContentsChanged(slot);
            }

            return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount()- limit) : ItemStack.EMPTY;
        }

        @Override
        @NotNull
        public ItemStack extractItem(int slot, int amount, boolean simulate)
        {
            if (amount == 0)
                return ItemStack.EMPTY;

            validateSlotIndex(slot);

            ItemStack existing = this.stacks.get(slot);

            if (existing.isEmpty())
                return ItemStack.EMPTY;

            int toExtract = Math.min(amount, existing.getMaxStackSize());

            if (existing.getCount() <= toExtract)
            {
                if (!simulate)
                {
                    this.stacks.set(slot, ItemStack.EMPTY);
                    onContentsChanged(slot, existing.getCount(), MODES.REMOVE);
                    return existing;
                }
                else
                {
                    return existing.copy();
                }
            }
            else
            {
                if (!simulate)
                {
                    this.stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                    onContentsChanged(slot, toExtract, MODES.REMOVE);
                }

                return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
            }
        }

        @Override
        public CompoundTag serializeNBT()
        {
            CompoundTag nbt = super.serializeNBT();
            nbt.putInt("Layers", layers);
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt)
        {
            setLayers(nbt.contains("Layers") ? nbt.getInt("Layers") : 0);
            super.deserializeNBT(nbt);
            updateSlots();
        }

        protected void setLayers(int layers) {
            this.layers = layers;
        }
    };

    private final LazyOptional<IItemHandler> opt = LazyOptional.of(() -> inv);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return ForgeCapabilities.ITEM_HANDLER.orEmpty(capability, opt);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = ((ItemStackHandler) inv).serializeNBT();
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ((ItemStackHandler) inv).deserializeNBT(nbt);
    }

}
