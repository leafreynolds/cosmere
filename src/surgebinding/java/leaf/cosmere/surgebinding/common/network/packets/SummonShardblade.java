/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.network.packets;

import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.surgebinding.common.items.ShardbladeItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class SummonShardblade implements ICosmerePacket
{
	public SummonShardblade()
	{
	}

	public SummonShardblade(FriendlyByteBuf buffer)
	{
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		ServerPlayer sender = context.getSender();
		MinecraftServer server = sender.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
		{
			var nbt = cap.getCompoundTag();
			var doot = CompoundNBTHelper.getOrCreate(nbt, "shardblades");

			final LivingEntity livingEntity = cap.getLiving();
			final ItemStack itemInHand = livingEntity.getItemInHand(InteractionHand.MAIN_HAND);
			if (itemInHand.isEmpty())
			{
				if (doot.contains("1"))
				{
					final CompoundTag test = doot.getCompound("1");
					ItemStack stack = ItemStack.of(test);

					livingEntity.setItemInHand(InteractionHand.MAIN_HAND, stack);

					doot.remove("1");

					cap.syncToClients(null);
				}
			}
			else if (itemInHand.getItem() instanceof ShardbladeItem)
			{
				doot.put("1", itemInHand.serializeNBT());
				livingEntity.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
				cap.syncToClients(null);
			}

		}));
		context.setPacketHandled(true);
	}

	@Override
	public void encode(FriendlyByteBuf buf)
	{

	}

}
