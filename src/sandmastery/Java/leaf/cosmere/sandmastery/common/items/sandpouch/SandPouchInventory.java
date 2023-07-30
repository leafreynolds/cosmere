package leaf.cosmere.sandmastery.common.items.sandpouch;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SandPouchInventory implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
    public static final int size = 3;
    private final IItemHandler inv = new SandpouchItemHandler(size)
    {
    };

    private final LazyOptional<IItemHandler> opt = LazyOptional.of(() -> inv);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
    {
        return ForgeCapabilities.ITEM_HANDLER.orEmpty(capability, opt);
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag tag = ((SandpouchItemHandler) inv).serializeNBT();
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        ((SandpouchItemHandler) inv).deserializeNBT(nbt);
    }

}
