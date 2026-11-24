package leaf.cosmere.sandmastery.common.network.packets;

import io.netty.buffer.ByteBuf;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.sandmastery.client.gui.CheckpointBarHUD;
import leaf.cosmere.sandmastery.common.Sandmastery;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

// server -> client: the checkpoints to show on the checkpoint bar. An empty list hides the bar.
public record DiemCheckpointsMessage(List<Vec3> points, int next) implements ICosmerePacket
{
	public static final CustomPacketPayload.Type<DiemCheckpointsMessage> TYPE =
			new CustomPacketPayload.Type<>(Sandmastery.rl("diem_checkpoints"));

	private static final StreamCodec<ByteBuf, Vec3> VEC3 = StreamCodec.composite(
			ByteBufCodecs.DOUBLE, Vec3::x,
			ByteBufCodecs.DOUBLE, Vec3::y,
			ByteBufCodecs.DOUBLE, Vec3::z,
			Vec3::new);

	public static final StreamCodec<ByteBuf, DiemCheckpointsMessage> STREAM_CODEC = StreamCodec.composite(
			VEC3.apply(ByteBufCodecs.list()), DiemCheckpointsMessage::points,
			ByteBufCodecs.VAR_INT, DiemCheckpointsMessage::next,
			DiemCheckpointsMessage::new);

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}

	@Override
	public void handle(IPayloadContext context)
	{
		context.enqueueWork(() -> CheckpointBarHUD.setCheckpoints(points, next));
	}
}
