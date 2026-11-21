/*
 * File updated ~ 2026-04-25 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.allomancy.common.network.packets;

import io.netty.buffer.ByteBuf;
import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.manifestation.AllomancyBrass;
import leaf.cosmere.allomancy.common.manifestation.AllomancyZinc;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record EntityAllomancyActivateMessage(int metalId, boolean isSingleTarget, int singleTargetEntityID) implements ICosmerePacket
{
	public static final CustomPacketPayload.Type<EntityAllomancyActivateMessage> TYPE =
			new CustomPacketPayload.Type<>(Allomancy.rl("entity_allomancy_activate"));

	public static final StreamCodec<ByteBuf, EntityAllomancyActivateMessage> STREAM_CODEC =
			StreamCodec.composite(
					ByteBufCodecs.VAR_INT, EntityAllomancyActivateMessage::metalId,
					ByteBufCodecs.BOOL, EntityAllomancyActivateMessage::isSingleTarget,
					ByteBufCodecs.VAR_INT, EntityAllomancyActivateMessage::singleTargetEntityID,
					EntityAllomancyActivateMessage::new);

	public EntityAllomancyActivateMessage(Metals.MetalType metalType, boolean isSingleTarget, int singleTargetEntityID)
	{
		this(metalType.getID(), isSingleTarget, singleTargetEntityID);
	}

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
					Metals.MetalType metalType = Metals.MetalType.valueOf(metalId).get();
					switch (metalType)
					{
						case BRASS:
							AllomancyBrass.BrassThread brassThread = AllomancyBrass.playerThreadMap.get(sender.getStringUUID());
							brassThread.isSingleTarget = isSingleTarget;
							if (isSingleTarget)
							{
								brassThread.singleTargetEntityID = singleTargetEntityID;
							}
							break;
						case ZINC:
							AllomancyZinc.ZincThread zincThread = AllomancyZinc.playerThreadMap.get(sender.getStringUUID());
							zincThread.isSingleTarget = isSingleTarget;
							if (isSingleTarget)
							{
								zincThread.singleTargetEntityID = singleTargetEntityID;
							}
							break;
						default:
							break;
					}
				}));
	}
}
