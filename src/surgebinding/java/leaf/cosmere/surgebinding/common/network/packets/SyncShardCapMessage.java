package leaf.cosmere.surgebinding.common.network.packets;

import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.surgebinding.common.capabilities.RadiantShardData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class SyncShardCapMessage implements ICosmerePacket
{
	private final CompoundTag nbt;
	private final int slot;

	public SyncShardCapMessage(CompoundTag nbt, int slot)
	{
		this.nbt = nbt;
		this.slot = slot;
	}


	public static SyncShardCapMessage decode(FriendlyByteBuf buf)
	{
		return new SyncShardCapMessage(buf.readNbt(), buf.readInt());
	}


	@Override
	public void handle(NetworkEvent.Context context)
	{
		context.enqueueWork(() ->
		{
			Minecraft mc = Minecraft.getInstance();
			if (mc.player == null)
			{
				return;
			}

			ItemStack stack = mc.player.getInventory().getItem(slot);
			stack.getCapability(RadiantShardData.RADIANT_SHARD_DATA)
					.ifPresent(cap -> cap.deserializeNBT(nbt));
		});
		context.setPacketHandled(true);
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeNbt(this.nbt);
		buffer.writeInt(this.slot);
	}
}
