/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.network.packets;

import io.netty.buffer.ByteBuf;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.config.SurgebindingConfigs;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class RequestStormlight implements ICosmerePacket
{
	public static final CustomPacketPayload.Type<RequestStormlight> TYPE =
			new CustomPacketPayload.Type<>(Surgebinding.rl("request_stormlight"));

	public static final StreamCodec<ByteBuf, RequestStormlight> STREAM_CODEC =
			StreamCodec.unit(new RequestStormlight());

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}



	public RequestStormlight()
	{
	}

	@Override
	public void handle(IPayloadContext context)
	{
		if (!(context.player() instanceof ServerPlayer sender))
		{
			return;
		}
		context.enqueueWork(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
		{
			SurgebindingSpiritwebSubmodule ssm = SurgebindingSpiritwebSubmodule.getSubmodule(cap);

			if (ssm != null)
			{
				if (ssm.isHerald())
				{
					//heralds had a direct line to honor's investiture
					ssm.setStormlight(SurgebindingConfigs.SERVER.PLAYER_MAX_STORMLIGHT.get());
				}
				else if (ssm.isOathed())
				{
					ssm.requestStormlight();
				}
				else if(SurgebindingManifestations.SURGEBINDING_POWERS.values().stream()
						.anyMatch((manifestation -> cap.hasManifestation(manifestation.getManifestation()))))
				{
					ssm.requestStormlight();
				}
			}

		}));
	}


}
