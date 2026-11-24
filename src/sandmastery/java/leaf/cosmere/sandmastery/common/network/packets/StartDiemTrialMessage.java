package leaf.cosmere.sandmastery.common.network.packets;

import io.netty.buffer.ByteBuf;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.diem.trial.DiemTrials;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record StartDiemTrialMessage(ResourceLocation trialId, BlockPos stone, boolean scoutOnly) implements ICosmerePacket
{
	public static final CustomPacketPayload.Type<StartDiemTrialMessage> TYPE =
			new CustomPacketPayload.Type<>(Sandmastery.rl("start_diem_trial"));

	public static final StreamCodec<ByteBuf, StartDiemTrialMessage> STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC, StartDiemTrialMessage::trialId,
			BlockPos.STREAM_CODEC, StartDiemTrialMessage::stone,
			ByteBufCodecs.BOOL, StartDiemTrialMessage::scoutOnly,
			StartDiemTrialMessage::new);

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}

	@Override
	public void handle(IPayloadContext context)
	{
		if (context.player() instanceof ServerPlayer player)
		{
			context.enqueueWork(() -> DiemTrials.handleStartRequest(player, trialId, stone, scoutOnly));
		}
	}
}
