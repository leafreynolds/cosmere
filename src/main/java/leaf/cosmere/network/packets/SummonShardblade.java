/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.network.packets;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.items.ShardbladeItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static leaf.cosmere.constants.Constants.Translations.POWER_INACTIVE;

public class SummonShardblade
{

	public SummonShardblade()
	{
	}

	public SummonShardblade(FriendlyByteBuf buffer)
	{
	}

	public static void handle(SummonShardblade message, Supplier<NetworkEvent.Context> ctx)
	{
		NetworkEvent.Context context = ctx.get();
		ServerPlayer sender = context.getSender();
		MinecraftServer server = sender.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
		{
			var nbt = cap.getNBT();
			var doot = nbt.getCompound("shardblades");

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

	public void encode(FriendlyByteBuf buf)
	{

	}

}
