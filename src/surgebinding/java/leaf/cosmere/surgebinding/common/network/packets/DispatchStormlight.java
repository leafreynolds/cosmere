/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.network.packets;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class DispatchStormlight implements ICosmerePacket
{

	public DispatchStormlight()
	{
	}

	public DispatchStormlight(FriendlyByteBuf buffer)
	{
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		ServerPlayer sender = context.getSender();
		MinecraftServer server = sender.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
		{
			SurgebindingSpiritwebSubmodule ssm = SurgebindingSpiritwebSubmodule.getSubmodule(cap);

			if (ssm != null)
			{
				if (ssm.getStormlight() != 0)
				{
					if (ssm.isOathed() || ssm.isHerald())
					{
						ssm.dispatchStormlight();
					}
				}
			}

		}));
		context.setPacketHandled(true);
	}


	@Override
	public void encode(FriendlyByteBuf buf)
	{

	}

}
