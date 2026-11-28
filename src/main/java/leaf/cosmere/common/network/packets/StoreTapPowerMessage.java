/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.common.network.packets;

import io.netty.buffer.ByteBuf;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.charge.IHoldsPowers;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.common.util.CosmereAttributeUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record StoreTapPowerMessage(
		Holder<Attribute> attribute,
		int attributeStrength,
		int itemSlot,
		int attributeSlot,
		boolean isStore,
		boolean isCurio) implements ICosmerePacket
{
	public static final CustomPacketPayload.Type<StoreTapPowerMessage> TYPE =
			new CustomPacketPayload.Type<>(Cosmere.rl("store_tap_power"));

	public static final StreamCodec<ByteBuf, StoreTapPowerMessage> STREAM_CODEC =
			StreamCodec.of(
					(buf, msg) ->
					{
						ByteBufCodecs.STRING_UTF8.encode(buf, msg.attribute.getRegisteredName());
						ByteBufCodecs.VAR_INT.encode(buf, msg.attributeStrength);
						ByteBufCodecs.VAR_INT.encode(buf, msg.itemSlot);
						ByteBufCodecs.VAR_INT.encode(buf, msg.attributeSlot);
						ByteBufCodecs.BOOL.encode(buf, msg.isStore);
						ByteBufCodecs.BOOL.encode(buf, msg.isCurio);
					},
					buf -> new StoreTapPowerMessage(
							CosmereAttributeUtils.getAttributeByDescriptionId(ByteBufCodecs.STRING_UTF8.decode(buf)),
							ByteBufCodecs.VAR_INT.decode(buf),
							ByteBufCodecs.VAR_INT.decode(buf),
							ByteBufCodecs.VAR_INT.decode(buf),
							ByteBufCodecs.BOOL.decode(buf),
							ByteBufCodecs.BOOL.decode(buf)
					)
			);

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
					// Storing
					if (isStore)
					{
						ItemStack itemStack = CosmereAttributeUtils.getPowerItem(sender, itemSlot, isCurio);
						if (itemStack.getItem() instanceof IHoldsPowers item)
						{
							if (item.trySetAttunedPlayer(itemStack, sender))
							{
								CosmereAttributeUtils.removeBaseAttribute(sender, attribute);
								item.addPower(itemStack, attribute, attributeStrength, attributeSlot);
							}
						}
					}
					// Tapping
					else
					{
						ItemStack itemStack = CosmereAttributeUtils.getPowerItem(sender, itemSlot, isCurio);
						if (itemStack.getItem() instanceof IHoldsPowers item)
						{
							if (item.getPlayerIsAttuned(itemStack, sender))
							{
								if (attribute.value() instanceof RangedAttribute rangedAttribute)
								{
									CosmereAttributeUtils.grantBaseAttribute(sender, rangedAttribute, attributeStrength);
									item.removePower(itemStack, attribute.value());
								}
							}
						}
					}
					cap.syncToClients(sender);
				}));
	}
}
