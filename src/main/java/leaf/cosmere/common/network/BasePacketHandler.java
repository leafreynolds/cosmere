package leaf.cosmere.common.network;

import leaf.cosmere.api.Version;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.Function;

public abstract class BasePacketHandler
{

	protected static SimpleChannel createChannel(ResourceLocation name, Version version)
	{
		String protocolVersion = version.toString();
		return NetworkRegistry.ChannelBuilder.named(name)
				.clientAcceptedVersions(protocolVersion::equals)
				.serverAcceptedVersions(protocolVersion::equals)
				.networkProtocolVersion(() -> protocolVersion)
				.simpleChannel();
	}

	protected abstract SimpleChannel getChannel();

	public abstract void initialize();

	private int index = 0;

	protected <MSG extends ICosmerePacket> void registerClientToServer(Class<MSG> type, Function<FriendlyByteBuf, MSG> decoder)
	{
		registerMessage(type, decoder, NetworkDirection.PLAY_TO_SERVER);
	}

	protected <MSG extends ICosmerePacket> void registerServerToClient(Class<MSG> type, Function<FriendlyByteBuf, MSG> decoder)
	{
		registerMessage(type, decoder, NetworkDirection.PLAY_TO_CLIENT);
	}

	private <MSG extends ICosmerePacket> void registerMessage(Class<MSG> type, Function<FriendlyByteBuf, MSG> decoder, NetworkDirection networkDirection)
	{
		getChannel().registerMessage(index++, type, ICosmerePacket::encode, decoder, ICosmerePacket::handle, Optional.of(networkDirection));
	}


	/**
	 * Sends a packet to the server.<br>
	 * Must be called Client side.
	 */
	public void sendToServer(Object msg)
	{
		getChannel().sendToServer(msg);
	}

	/**
	 * Send a packet to a specific player.<br>
	 * Must be called Server side.
	 */
	public void sendTo(Object msg, ServerPlayer player)
	{
		if (!(player instanceof FakePlayer))
		{
			getChannel().send(PacketDistributor.PLAYER.with(() -> player), msg);
		}
	}

	public void sendPacketToAll(Object packet)
	{
		getChannel().send(PacketDistributor.ALL.noArg(), packet);
	}


	public void sendToAllAround(Object mes, ResourceKey<Level> dim, BlockPos pos, int radius)
	{
		getChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), radius, dim)), mes);
	}

	public void sendToAllInWorld(Object mes, ServerLevel world)
	{
		getChannel().send(PacketDistributor.DIMENSION.with(world::dimension), mes);
	}

	public void sendToTrackingTE(Object mes, BlockEntity te)
	{
		if (te != null && !te.getLevel().isClientSide)
		{
			getChannel().send(PacketDistributor.TRACKING_CHUNK.with(() -> te.getLevel().getChunkAt(te.getBlockPos())), mes);
		}
	}
}