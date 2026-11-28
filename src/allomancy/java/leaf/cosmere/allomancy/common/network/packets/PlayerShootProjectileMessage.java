/*
 * File updated ~ 2026-04-25 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.allomancy.common.network.packets;

import io.netty.buffer.ByteBuf;
import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PlayerShootProjectileMessage() implements ICosmerePacket
{
	public static final CustomPacketPayload.Type<PlayerShootProjectileMessage> TYPE =
			new CustomPacketPayload.Type<>(Allomancy.rl("player_shoot_projectile"));

	public static final StreamCodec<ByteBuf, PlayerShootProjectileMessage> STREAM_CODEC =
			StreamCodec.unit(new PlayerShootProjectileMessage());

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}

	@Override
	public void handle(IPayloadContext context)
	{
		if (!(context.player() instanceof ServerPlayer sender))
		{
			return;
		}
		context.enqueueWork(() ->
				SpiritwebCapability.get(sender).ifPresent((cap) ->
				{
					for (int i = 0; i < sender.getInventory().getContainerSize(); i++)
					{
						ItemStack bag = sender.getInventory().getItem(i);
						if (!bag.isEmpty() && bag.is(AllomancyItems.COIN_POUCH.get()))
						{
							AllomancyItems.COIN_POUCH.get().shoot(sender, bag);
							return;
						}
					}
				}));
	}
}
