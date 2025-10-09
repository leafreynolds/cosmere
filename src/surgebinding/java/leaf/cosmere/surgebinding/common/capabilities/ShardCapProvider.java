package leaf.cosmere.surgebinding.common.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ShardCapProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
	private final LazyOptional<IShard> instance;
	private final IShard backend;

	public ShardCapProvider(Supplier<IShard> supplier)
	{
		this.backend = supplier.get();
		this.instance = LazyOptional.of(() -> backend);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		return cap == ShardData.SHARD_DATA ? instance.cast() : LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		return instance.map(IShard::serializeNBT).orElseGet(CompoundTag::new);
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		instance.ifPresent(cap -> cap.deserializeNBT(nbt));
	}
}
