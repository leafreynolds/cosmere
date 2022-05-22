/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.network.packets;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.registry.ItemsRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerShootProjectileMessage
{
	public PlayerShootProjectileMessage()
	{
		//empty
	}

	public static void encode(PlayerShootProjectileMessage mes, FriendlyByteBuf buf)
	{
		//empty
	}

	public static PlayerShootProjectileMessage decode(FriendlyByteBuf buf)
	{
		return new PlayerShootProjectileMessage();
	}

	public static void handle(PlayerShootProjectileMessage mes, Supplier<NetworkEvent.Context> cont)
	{

		NetworkEvent.Context context = cont.get();
		ServerPlayer player = context.getSender();
		MinecraftServer server = player.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(player).ifPresent((cap) ->
		{
			for (int i = 0; i < player.getInventory().getContainerSize(); i++)
			{
				ItemStack bag = player.getInventory().getItem(i);
				if (!bag.isEmpty() && bag.is(ItemsRegistry.COIN_POUCH.get()))
				{
					ItemsRegistry.COIN_POUCH.get().shoot(player, bag);

					return;
				}
			}
		}));
		cont.get().setPacketHandled(true);
	}

}
