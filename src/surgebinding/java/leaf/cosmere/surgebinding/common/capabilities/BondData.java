package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.Constants;
import leaf.cosmere.surgebinding.common.registries.SurgebindingDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class BondData implements IBondData
{


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

	//stacks can't hold attachments in 1.21, so the bond lives on the bond_data component
	public static BondData load(ItemStack stack)
	{
		BondData data = new BondData(stack);
		if (stack == null || stack.isEmpty())
		{
			return data;
		}

		CompoundTag stored = stack.get(SurgebindingDataComponents.BOND_DATA.get());
		if (stored != null && !stored.isEmpty())
		{
			//the component's tag is shared, so deserialize into a copy
			data.deserializeNBT(null, stored.copy());
		}
		return data;
	}

	protected void save()
	{
		if (stack != null && !stack.isEmpty())
		{
			//hand the component a copy, never this instance's working tag
			stack.set(SurgebindingDataComponents.BOND_DATA.get(), serializeNBT(null).copy());
		}
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
		save();
	}

	@Override
	public void setEmptyBond()
	{
		this.bond = null;
		save();
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
		save();
	}

	@Override
	public void resetBondTicks()
	{
		bondTicks = 0;
		save();
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider)
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
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag)
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
