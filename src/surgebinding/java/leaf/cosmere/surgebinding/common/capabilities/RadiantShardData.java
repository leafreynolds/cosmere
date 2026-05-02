/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 *
 * Capability/LazyOptional/INBTSerializable removed. Per-stack state now lives directly on
 * the ItemStack via `DataComponents.CUSTOM_DATA` through `StackNBTHelper`. Construct a
 * stack-bound view with `RadiantShardData.of(stack)` and use it like a plain DTO.
 */

package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.helpers.StackNBTHelper;
import net.minecraft.world.item.ItemStack;

public class RadiantShardData implements IRadiantShardData
{
	protected static final String NBT_RADIANT_ORDER = "radiantOrder";
	protected static final String NBT_IS_LIVING = "isLiving";

	protected final ItemStack stack;

	public RadiantShardData(ItemStack stack)
	{
		this.stack = stack;
	}

	public static RadiantShardData of(ItemStack stack)
	{
		return new RadiantShardData(stack);
	}

	@Override
	public Roshar.RadiantOrder getOrder()
	{
		int id = StackNBTHelper.getInt(stack, NBT_RADIANT_ORDER, -1);
		if (id < 0)
		{
			return null;
		}
		return Roshar.RadiantOrder.valueOf(id).orElse(null);
	}

	@Override
	public boolean isLiving()
	{
		return StackNBTHelper.getBoolean(stack, NBT_IS_LIVING, false);
	}

	@Override
	public void setOrder(Roshar.RadiantOrder order)
	{
		if (order == null)
		{
			StackNBTHelper.removeEntry(stack, NBT_RADIANT_ORDER);
		}
		else
		{
			StackNBTHelper.setInt(stack, NBT_RADIANT_ORDER, order.getID());
		}
	}

	@Override
	public void setLiving(boolean living)
	{
		StackNBTHelper.setBoolean(stack, NBT_IS_LIVING, living);
	}
}
