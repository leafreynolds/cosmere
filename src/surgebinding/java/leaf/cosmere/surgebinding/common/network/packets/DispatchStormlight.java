package leaf.cosmere.surgebinding.common.network.packets;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static net.minecraft.network.codec.ByteBufCodecs.BOOL;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record DispatchStormlight() implements ICosmerePacket
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

	@Override
	public void handle(IPayloadContext context)
	{
		if (!(context.player() instanceof ServerPlayer sender))
		{
			return;
		}
		context.enqueueWork(() -> SpiritwebCapability.get(sender).ifPresent(cap ->
		{
			SurgebindingSpiritwebSubmodule ssm = SurgebindingSpiritwebSubmodule.getSubmodule(cap);
			if (ssm != null && ssm.getStormlight() != 0 && (ssm.isOathed() || ssm.isHerald()))
			{
				ssm.dispatchStormlight();
			}
		}));
	}
}
