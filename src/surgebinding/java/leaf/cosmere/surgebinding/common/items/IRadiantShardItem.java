package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.capabilities.RadiantShardData;
import net.minecraft.world.item.ItemStack;

public interface IRadiantShardItem
{
	RadiantShardData getShardData(ItemStack stack);

	default Roshar.RadiantOrder getOrder(ItemStack stack)
	{
		return getShardData(stack).getOrder();
	}

	default boolean isLiving(ItemStack stack)
	{
		return getShardData(stack).isLiving();
	}

	default ItemStack randomizedLootData(ItemStack stack)
	{
		RadiantShardData data = getShardData(stack);
		data.setLiving(false);
		int i = (int) (Math.random() * 10);
		data.setOrder(Roshar.RadiantOrder.valueOf(i).get());
		return stack;
	}

	default ItemStack buildData(ItemStack stack, Roshar.RadiantOrder order, boolean isLiving)
	{
		RadiantShardData data = getShardData(stack);
		data.setLiving(isLiving);
		if (order != null)
		{
			data.setOrder(order);
		}
		return stack;
	}
}
