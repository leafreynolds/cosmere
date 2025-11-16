/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.network.packets;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.config.SurgebindingConfigs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class RequestStormlight implements ICosmerePacket
{


	public RequestStormlight()
	{
	}

	public RequestStormlight(FriendlyByteBuf buffer)
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
				if (ssm.isHerald())
				{
					//heralds had a direct line to honor's investiture
					ssm.setStormlight(SurgebindingConfigs.SERVER.PLAYER_MAX_STORMLIGHT.get());
				}
				else if (ssm.isOathed())
				{
					ssm.requestStormlight();
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
