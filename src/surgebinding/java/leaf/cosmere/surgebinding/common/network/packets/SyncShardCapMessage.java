package leaf.cosmere.surgebinding.common.network.packets;

import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.surgebinding.common.capabilities.IShard;
import leaf.cosmere.surgebinding.common.capabilities.ShardData;
import leaf.cosmere.surgebinding.common.network.SurgebindingPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class SyncShardCapMessage implements ICosmerePacket
{
	private final CompoundTag nbt;
	private final int slot;

	public SyncShardCapMessage(CompoundTag nbt, int slot) {
		this.nbt = nbt;
		this.slot = slot;
	}


	public static SyncShardCapMessage decode(FriendlyByteBuf buf) {
		return new SyncShardCapMessage(buf.readNbt(), buf.readInt());
	}


	@Override
	public void handle(NetworkEvent.Context context)
	{

		context.enqueueWork(() -> {
			Player player = Minecraft.getInstance().player;
			if (player != null) {
				ItemStack stack = player.getInventory().getItem(slot);
				stack.getCapability(ShardData.SHARD_DATA)
						.ifPresent(cap -> cap.deserializeNBT(nbt));
			}
		});
		context.setPacketHandled(true);
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{

	}
}
