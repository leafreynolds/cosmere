package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.Constants;
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

public class BondData implements ICapabilityProvider, IBondData
{
	public static final Capability<IBondData> BOND_DATA = CapabilityManager.get(new CapabilityToken<>()
	{
	});

	private final LazyOptional<IBondData> opt = LazyOptional.of(() -> this);

	protected UUID bond;
	protected String bondedName;
	protected int bondTicks;

	protected CompoundTag nbt;

	protected final ItemStack stack;

	public BondData(ItemStack stack)
	{
		this.stack = stack;
		this.nbt = new CompoundTag();
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		return BOND_DATA.orEmpty(cap, opt);
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap)
	{
		return ICapabilityProvider.super.getCapability(cap);
	}

	@Override
	public UUID getBondedEntity()
	{
		return bond;
	}

	@Override
	public String getBondedName()
	{
		return bondedName;
	}

	@Override
	public boolean isBonded()
	{
		return bond != null;
	}

	@Override
	public void setBondedEntity(LivingEntity entity)
	{
		this.bond = entity.getUUID();
		this.bondedName = entity.getName().getString();
	}

	@Override
	public void setEmptyBond()
	{
		this.bond = null;
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

	@Override
	public CompoundTag serializeNBT()
	{
		if (bond != null)
		{
			this.nbt.putUUID(Constants.NBT.ATTUNED_PLAYER, bond);
			this.nbt.putString(Constants.NBT.ATTUNED_PLAYER_NAME, bondedName);
		}

		this.nbt.putInt("bondTicks", bondTicks);

		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag compoundTag)
	{
		this.nbt = compoundTag;

		if (nbt.contains(Constants.NBT.ATTUNED_PLAYER))
		{
			this.bond = nbt.getUUID(Constants.NBT.ATTUNED_PLAYER);
			this.bondedName = nbt.getString(Constants.NBT.ATTUNED_PLAYER_NAME);
		}

		this.bondTicks = nbt.getInt("bondTicks");
	}
}
