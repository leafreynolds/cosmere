package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.eventHandlers.SurgebindingCapabilitiesHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ShardData implements ICapabilityProvider, IShard
{
	public static final Capability<IShard> SHARD_DATA = CapabilityManager.get(new CapabilityToken<>()
	{
	});


	private final LazyOptional<IShard> opt = LazyOptional.of(() -> this);
	protected Roshar.RadiantOrder order;
	protected boolean living;
	protected UUID bond;

	protected int bondTicks;

	protected CompoundTag nbt;

	protected final ItemStack stack;

	public ShardData(ItemStack stack)
	{
		this.stack = stack;
		this.nbt = new CompoundTag();
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		return SHARD_DATA.orEmpty(cap, opt);
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
	public UUID getBondedEntity()
	{
		return bond;
	}

	@Override
	public boolean isBonded()
	{
		return bond != null;
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
	public void setBondedEntity(LivingEntity entity)
	{
		this.bond = entity.getUUID();
	}

	@Override
	public int bondTicks()
	{
		return bondTicks;
	}

	@Override
	public void tickBondUp()
	{
		bondTicks++;
	}

	@Override
	public void resetBondTicks()
	{
		bondTicks = 0;
	}

	public void setEmptyBond()
	{
		this.bond = null;
	}

	@Override
	public CompoundTag serializeNBT()
	{
		if(order != null)
		{
			this.nbt.putInt("radiantOrder", order.getID());
		}

		this.nbt.putBoolean("isLiving", living);

		if (bond != null)
		{
			this.nbt.putUUID(Constants.NBT.ATTUNED_PLAYER, bond);
		}

		this.nbt.putInt("bondTicks", bondTicks);

		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag compoundTag)
	{
		this.nbt = compoundTag;
		if(nbt.contains("radiantOrder"))
		{
			this.order = Roshar.RadiantOrder.valueOf(nbt.getInt("radiantOrder")).get();
		}
		this.living = nbt.getBoolean("isLiving");
		if(nbt.contains(Constants.NBT.ATTUNED_PLAYER))
		{
			this.bond = nbt.getUUID(Constants.NBT.ATTUNED_PLAYER);
		}

		this.bondTicks = nbt.getInt("bondTicks");
	}



}
