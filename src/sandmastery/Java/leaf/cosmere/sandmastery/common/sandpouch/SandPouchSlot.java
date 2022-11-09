package leaf.cosmere.sandmastery.common.sandpouch;

import leaf.cosmere.sandmastery.common.items.SandPouchItem;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocksRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SandPouchSlot extends SlotItemHandler {

    private final int index;

    public SandPouchSlot(IItemHandlerModifiable inv, int index, int x, int y) {
        super((IItemHandler) inv, index, x, y);
        this.index = index;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return SandPouchItem.SUPPORTED_ITEMS.test(stack);
    }

    @Override
    public void setChanged() {
        container.setChanged();
    }

}
