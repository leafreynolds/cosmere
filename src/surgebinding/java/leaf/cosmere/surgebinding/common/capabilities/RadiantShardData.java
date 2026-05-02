package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.Roshar;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RadiantShardData implements ICapabilityProvider, IRadiantShardData
{
	public static final Capability<IRadiantShardData> RADIANT_SHARD_DATA = CapabilityManager.get(new CapabilityToken<>()
	{
	});


	private final LazyOptional<IRadiantShardData> opt = LazyOptional.of(() -> this);
	protected Roshar.RadiantOrder order;
	protected boolean living;

	protected CompoundTag nbt;

	protected final ItemStack stack;

	public RadiantShardData(ItemStack stack)
	{
		this.stack = stack;
		this.nbt = new CompoundTag();
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		return RADIANT_SHARD_DATA.orEmpty(cap, opt);
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap)
	{
		return ICapabilityProvider.super.getCapability(cap);
	}

	@Override
	public Roshar.RadiantOrder getOrder()
	{
		return order;
	}

	@Override
	public boolean isLiving()
	{
		return living;
	}

	@Override
	public void setOrder(Roshar.RadiantOrder order)
	{
		this.order = order;
	}

	@Override
	public void setLiving(boolean living)
	{
		this.living = living;
	}

	@Override
	public CompoundTag serializeNBT()
	{
		if (order != null)
		{
			this.nbt.putInt("radiantOrder", order.getID());
		}

		this.nbt.putBoolean("isLiving", living);

		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag compoundTag)
	{
		this.nbt = compoundTag;
		if (nbt.contains("radiantOrder"))
		{
			this.order = Roshar.RadiantOrder.valueOf(nbt.getInt("radiantOrder")).get();
		}
		this.living = nbt.getBoolean("isLiving");
	}
}
