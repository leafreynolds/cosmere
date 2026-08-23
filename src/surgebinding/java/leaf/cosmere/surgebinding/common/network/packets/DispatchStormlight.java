/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.network.packets;

import io.netty.buffer.ByteBuf;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class DispatchStormlight implements ICosmerePacket
{
	public static final CustomPacketPayload.Type<DispatchStormlight> TYPE =
			new CustomPacketPayload.Type<>(Surgebinding.rl("dispatch_stormlight"));

	public static final StreamCodec<ByteBuf, DispatchStormlight> STREAM_CODEC =
			StreamCodec.unit(new DispatchStormlight());

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}


	public DispatchStormlight()
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
				if (ssm.getStormlight() != 0)
				{
					if (ssm.isOathed()
						|| ssm.isHerald()
						|| SurgebindingManifestations.SURGEBINDING_POWERS.values().stream()
							.anyMatch((manifestation -> cap.hasManifestation(manifestation.getManifestation()))))
					{
						ssm.dispatchStormlight();
					}
				}
			}

		}));
	}


}
