package leaf.cosmere.sandmastery.common.network.packets;

import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.sandmastery.client.gui.DiemTrialScreen;
import leaf.cosmere.sandmastery.common.Sandmastery;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.Optional;

public record OpenDiemTrialScreenMessage(BlockPos stone, int nextRank, int ribbons, int cooldownSeconds, List<Entry> trials) implements ICosmerePacket
{
	// one trial on the screen. unavailable is the reason it can't be attempted here, if any.
	public record Entry(ResourceLocation id, int timeSeconds, boolean canScout, Optional<Component> unavailable)
	{
		public static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(
				ResourceLocation.STREAM_CODEC, Entry::id,
				ByteBufCodecs.VAR_INT, Entry::timeSeconds,
				ByteBufCodecs.BOOL, Entry::canScout,
				ByteBufCodecs.optional(ComponentSerialization.TRUSTED_STREAM_CODEC), Entry::unavailable,
				Entry::new);
	}

	public static final CustomPacketPayload.Type<OpenDiemTrialScreenMessage> TYPE =
			new CustomPacketPayload.Type<>(Sandmastery.rl("open_diem_trial_screen"));

	public static final StreamCodec<RegistryFriendlyByteBuf, OpenDiemTrialScreenMessage> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, OpenDiemTrialScreenMessage::stone,
			ByteBufCodecs.VAR_INT, OpenDiemTrialScreenMessage::nextRank,
			ByteBufCodecs.VAR_INT, OpenDiemTrialScreenMessage::ribbons,
			ByteBufCodecs.VAR_INT, OpenDiemTrialScreenMessage::cooldownSeconds,
			Entry.STREAM_CODEC.apply(ByteBufCodecs.list()), OpenDiemTrialScreenMessage::trials,
			OpenDiemTrialScreenMessage::new);

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}

	@Override
	public void handle(IPayloadContext context)
	{
		context.enqueueWork(() -> DiemTrialScreen.open(this));
	}
}
