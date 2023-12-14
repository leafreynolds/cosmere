/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.network.packets;

import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PlayerShootProjectileMessage implements ICosmerePacket
{
	public PlayerShootProjectileMessage()
	{
		//empty
	}

	@Override
	public void encode(FriendlyByteBuf buf)
	{
		//empty
	}

	public static PlayerShootProjectileMessage decode(FriendlyByteBuf buf)
	{
		return new PlayerShootProjectileMessage();
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		ServerPlayer player = context.getSender();
		MinecraftServer server = player.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(player).ifPresent((cap) ->
		{
			for (int i = 0; i < player.getInventory().getContainerSize(); i++)
			{
				ItemStack bag = player.getInventory().getItem(i);
				if (!bag.isEmpty() && bag.is(AllomancyItems.COIN_POUCH.get()))
				{
					AllomancyItems.COIN_POUCH.get().shoot(player, bag);

					return;
				}
			}
		}));
	}

}
