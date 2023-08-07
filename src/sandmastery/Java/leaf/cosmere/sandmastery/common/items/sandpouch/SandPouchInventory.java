package leaf.cosmere.sandmastery.common.items.sandpouch;

import leaf.cosmere.sandmastery.common.items.SandPouchItem;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SandPouchInventory implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
	private final ISandPouchItemHandler inv = new SandpouchItemHandler(3)
	{
	};

	private final LazyOptional<ISandPouchItemHandler> opt = LazyOptional.of(() -> inv);

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		return SandPouchItem.POUCH_HANDLER.orEmpty(capability, opt);
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
