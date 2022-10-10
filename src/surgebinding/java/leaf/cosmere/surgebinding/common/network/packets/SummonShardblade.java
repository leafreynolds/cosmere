/*
 * File updated ~ 10 - 10 - 2022 ~ Leaf
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
			var spiritwebTags = cap.getCompoundTag();
			var shardblades = CompoundNBTHelper.getOrCreate(spiritwebTags, "shardblades");

			final LivingEntity livingEntity = cap.getLiving();
			final ItemStack itemInHand = livingEntity.getItemInHand(InteractionHand.MAIN_HAND);


			//todo config value
			final int maxShardblades = 10;

			if (itemInHand.isEmpty())
			{
				for (int i = 0; i < maxShardblades; i++)
				{
					final String pKey = String.valueOf(i);
					if (shardblades.contains(pKey))
					{
						final CompoundTag test = shardblades.getCompound(pKey);
						ItemStack stack = ItemStack.of(test);
						livingEntity.setItemInHand(InteractionHand.MAIN_HAND, stack);
						shardblades.remove(pKey);

						//todo do we actually need to tell the player about changes to their spiritweb here?
						//cap.syncToClients(sender);
						break;
					}
				}
			}
			else if (itemInHand.getItem() instanceof ShardbladeItem shardbladeItem)
			{
				if (shardbladeItem.canSummonDismiss(sender))
				{

					for (int i = 0; i < maxShardblades; i++)
					{
						final String pKey = String.valueOf(i);
						if (!shardblades.contains(pKey))
						{
							shardblades.put(pKey, itemInHand.serializeNBT());
							livingEntity.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

							//todo do we actually need to tell the player about changes to their spiritweb here?
							//cap.syncToClients(sender);
							break;
						}
					}
				}
			}

		}));
		context.setPacketHandled(true);
	}

	@Override
	public void encode(FriendlyByteBuf buf)
	{

	}

}
