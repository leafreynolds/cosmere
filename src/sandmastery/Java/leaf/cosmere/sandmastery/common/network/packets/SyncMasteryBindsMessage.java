/*
 * File updated ~ 10 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.network.packets;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class SyncMasteryBindsMessage implements ICosmerePacket
{
	public int flags;

	public SyncMasteryBindsMessage(int flags)
	{
		this.flags = flags;
	}

	@Override
	public void encode(FriendlyByteBuf buf)
	{
		buf.writeInt(flags);
	}

	public static SyncMasteryBindsMessage decode(FriendlyByteBuf buf)
	{
		return new SyncMasteryBindsMessage(buf.readInt());
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		ServerPlayer sender = context.getSender();
		MinecraftServer server = sender.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
		{
			SpiritwebCapability spiritweb = (SpiritwebCapability) cap;
			SandmasterySpiritwebSubmodule sb = (SandmasterySpiritwebSubmodule) spiritweb.getSubmodule(Manifestations.ManifestationTypes.SANDMASTERY);
			sb.updateFlags(this.flags);
		}));
	}

}
