package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.capabilities.DynamicShardbladeData;
import leaf.cosmere.surgebinding.common.capabilities.ShardData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public interface IShardItem
{
	ShardData getShardData(ItemStack stack);

	default Roshar.RadiantOrder getOrder(ItemStack stack)
	{
		return getShardData(stack).getOrder();
	}

	default boolean isLiving(ItemStack stack)
	{
		return getShardData(stack).isLiving();
	}

	default LivingEntity getBond(ItemStack stack)
	{
		ShardData data = getShardData(stack);
		UUID id = data.getBondedEntity();
		if(id != null)
		{
			Level level = Minecraft.getInstance().level;

			if(id.equals(Constants.NBT.UNKEYED_UUID))
			{
				return null;
			}

			return level.getPlayerByUUID(id);

		}
		else
		{
			return null;
		}
	}

	default boolean isBondedTo(ItemStack stack, LivingEntity player)
	{
		return getBond(stack).is(player);
	}

	boolean canSummonDismiss(LivingEntity player, ItemStack stack);

	void bond(ItemStack stack, Player entity);

	void releaseBond(ItemStack stack);


	default ItemStack randomizedLootData(ItemStack stack)
	{
		ShardData data = getShardData(stack);
		data.setLiving(false);
		int i = (int)(Math.random()*10);
		data.setOrder(Roshar.RadiantOrder.valueOf(i).get());
		return stack;
	}


	default ItemStack buildData(ItemStack stack, Roshar.RadiantOrder order, boolean isLiving, Player bonded)
	{
		ShardData data = getShardData(stack);
		data.setLiving(isLiving);
		if(bonded != null)
		{
			data.setBondedEntity(bonded);
		}
		if(order != null)
		{
			data.setOrder(order);
		}
		return stack;
	}

	int bondTime();


}
