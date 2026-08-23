package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BondableRadiantShardData extends RadiantShardData implements IBondData
{

	protected UUID bond;
	protected String bondedName;
	protected int bondTicks;

	public BondableRadiantShardData(ItemStack stack)
	{
		super(stack);
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
		super.serializeNBT(provider);

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
		super.deserializeNBT(provider, compoundTag);

		if (nbt.contains(Constants.NBT.ATTUNED_PLAYER))
		{
			this.bond = nbt.getUUID(Constants.NBT.ATTUNED_PLAYER);
			this.bondedName = nbt.getString(Constants.NBT.ATTUNED_PLAYER_NAME);
		}

		this.bondTicks = nbt.getInt("bondTicks");
	}
}
