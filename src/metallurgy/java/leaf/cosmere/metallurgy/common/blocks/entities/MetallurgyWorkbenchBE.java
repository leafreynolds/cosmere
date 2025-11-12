package leaf.cosmere.metallurgy.common.blocks.entities;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.metallurgy.common.items.AlloyFragmentItem;
import leaf.cosmere.metallurgy.common.items.MetallurgistChiselItem;
import leaf.cosmere.metallurgy.common.menus.MetallurgyWorkbenchMenu;
import leaf.cosmere.metallurgy.common.registries.MetallurgyBlockEntities;
import leaf.cosmere.metallurgy.common.registries.MetallurgyItems;
import leaf.cosmere.metallurgy.common.util.AlloyComposition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class MetallurgyWorkbenchBE extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            // Slot 0: Input - accepts any item
            if (slot == 0) {
                return true; // TODO: Add config file based whitelist
            }
            // Slot 1: Output - output only
            if (slot == 1) {
                return false;
            }
            // Slot 2: Chisel - only accepts chisel items
            if (slot == 2) {
                return stack.getItem() instanceof MetallurgistChiselItem;
            }
            return false;
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            if (index == 0) {
                return cutPercentage;
            } else if (index == 1) {
                return hasCutLineSet ? 1 : 0;
            }
            return 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                cutPercentage = value;
            } else if (index == 1) {
                hasCutLineSet = value != 0;
            }
        }

        @Override
        public int getCount() {
            return 2; // cutPercentage and hasCutLineSet
        }
    };

    private int cutPercentage = 50;
    private boolean hasCutLineSet = false;

    public MetallurgyWorkbenchBE(BlockPos pPos, BlockState pBlockState) {
        super(MetallurgyBlockEntities.METALLURGY_WORKBENCH.get(), pPos, pBlockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.metallurgy.metallurgy_workbench");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new MetallurgyWorkbenchMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("cutPercentage", cutPercentage);
        tag.putBoolean("hasCutLineSet", hasCutLineSet);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        cutPercentage = tag.getInt("cutPercentage");
        hasCutLineSet = tag.getBoolean("hasCutLineSet");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    /**
     * Performs the cutting operation:
     * - Requires a chisel in slot 2
     * - Takes the input item from slot 0
     * - Creates two fragment items based on the cut percentage
     * - Places one fragment in the output slot
     * - Leaves the other fragment in the input slot
     * - Damages the chisel by 1 durability
     * 
     * @param percentage The percentage for the first fragment (5-95)
     */
    public void performCut(int percentage) {
        ItemStack inputStack = itemHandler.getStackInSlot(0);
        ItemStack outputStack = itemHandler.getStackInSlot(1);
        ItemStack chiselStack = itemHandler.getStackInSlot(2);

        if (inputStack.isEmpty() || !outputStack.isEmpty()) {
            return;
        }

        if (chiselStack.isEmpty() || !(chiselStack.getItem() instanceof MetallurgistChiselItem)) {
            CosmereAPI.logger.debug("Cannot cut: no chisel present");
            return;
        }

        percentage = Math.max(5, Math.min(95, percentage));

        ResourceLocation inputItemId = ForgeRegistries.ITEMS.getKey(inputStack.getItem());
        if (inputItemId == null) {
            CosmereAPI.logger.warn("Failed to get registry name for item: {}", inputStack);
            return;
        }

        double currentPercentage;
        if (inputStack.getItem() instanceof AlloyFragmentItem) {
            currentPercentage = AlloyComposition.getFragmentPercentage(inputStack);
        } else {
            currentPercentage = 1.0;
        }

        double fragment1Percentage = currentPercentage * (percentage / 100.0);
        double fragment2Percentage = currentPercentage * ((100 - percentage) / 100.0);

        if (fragment1Percentage < 0.05 || fragment2Percentage < 0.05)
            return;

        ItemStack fragment1 = new ItemStack(MetallurgyItems.ALLOY_FRAGMENT.get());
        ItemStack fragment2 = new ItemStack(MetallurgyItems.ALLOY_FRAGMENT.get());

        Map<String, Double> composition;
        if (AlloyComposition.hasComposition(inputStack)) {
            composition = AlloyComposition.getComposition(inputStack);
        } else {
            composition = new HashMap<>();
            composition.put(inputItemId.toString(), 1.0);
        }

        AlloyComposition.setComposition(fragment1, composition);
        AlloyComposition.setFragmentPercentage(fragment1, fragment1Percentage);

        AlloyComposition.setComposition(fragment2, composition);
        AlloyComposition.setFragmentPercentage(fragment2, fragment2Percentage);

        if (AlloyComposition.getContamination(inputStack) > 0.0) {
            double contamination = AlloyComposition.getContamination(inputStack);
            AlloyComposition.setContamination(fragment1, contamination);
            AlloyComposition.setContamination(fragment2, contamination);
        }

        itemHandler.setStackInSlot(0, fragment1);
        itemHandler.setStackInSlot(1, fragment2);

        if (chiselStack.isDamageableItem()) {
            chiselStack.setDamageValue(chiselStack.getDamageValue() + 1);
            if (chiselStack.getDamageValue() >= chiselStack.getMaxDamage()) {
                itemHandler.setStackInSlot(2, ItemStack.EMPTY);
            }
        }

        setChanged();
    }
}
