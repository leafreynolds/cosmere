/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.registries.SurgebindingDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class RadiantShardData implements IRadiantShardData
{


	protected Roshar.RadiantOrder order;
	protected boolean living;

	protected CompoundTag nbt;

	protected final ItemStack stack;

	protected boolean unseeded = true;

	public RadiantShardData(ItemStack stack)
	{
		this.stack = stack;
		this.nbt = new CompoundTag();
	}

	//stacks can't hold attachments in 1.21, so shard data lives on the shard_data component
	public static <T extends RadiantShardData> T load(ItemStack stack, T data)
	{
		if (stack == null || stack.isEmpty())
		{
			return data;
		}

		CompoundTag stored = stack.get(SurgebindingDataComponents.SHARD_DATA.get());
		if (stored != null && !stored.isEmpty())
		{
			//the component's tag is shared, so deserialize into a copy
			data.deserializeNBT(null, stored.copy());
			data.unseeded = false;
		}
		return data;
	}

	protected void randomiseAppearance()
	{
	}

	//server only
	public void seed()
	{
		save();
	}

	protected void save()
	{
		if (stack == null || stack.isEmpty())
		{
			return;
		}

		if (unseeded)
		{
			randomiseAppearance();
			unseeded = false;
		}

		stack.set(SurgebindingDataComponents.SHARD_DATA.get(), serializeNBT(null).copy());
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
		save();
	}

	@Override
	public void setLiving(boolean living)
	{
		this.living = living;
		save();
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider)
	{
		if (order != null)
		{
			this.nbt.putInt("radiantOrder", order.getID());
		}

		this.nbt.putBoolean("isLiving", living);

		return nbt;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag)
	{
		this.nbt = compoundTag;
		if (nbt.contains("radiantOrder"))
		{
			this.order = Roshar.RadiantOrder.valueOf(nbt.getInt("radiantOrder")).get();
		}
		this.living = nbt.getBoolean("isLiving");
	}
}
