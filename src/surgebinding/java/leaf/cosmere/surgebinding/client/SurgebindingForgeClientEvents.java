package leaf.cosmere.surgebinding.client;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.network.packets.DispatchStormlight;
import leaf.cosmere.surgebinding.common.network.packets.RequestStormlight;
import leaf.cosmere.surgebinding.common.network.packets.SummonShardblade;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = Surgebinding.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class SurgebindingForgeClientEvents
{
	@SubscribeEvent
	public static void onKey(InputEvent.Key event)
	{
		final LocalPlayer player = Minecraft.getInstance().player;

		if (player == null)
		{
			return;
		}

		SpiritwebCapability.get(player).ifPresent(spiritweb ->
		{
			if (isKeyPressed(event, SurgebindingKeybindings.SHARDBLADE))
			{
				Surgebinding.packetHandler().sendToServer(new SummonShardblade());
			}

			if (isKeyPressed(event, SurgebindingKeybindings.DISPATCH_STORMLIGHT))
			{
				Surgebinding.packetHandler().sendToServer(new DispatchStormlight());
			}
			if (isKeyPressed(event, SurgebindingKeybindings.REQUEST_STORMLIGHT))
			{
				Surgebinding.packetHandler().sendToServer(new RequestStormlight());
			}
		});
	}

	private static boolean isKeyPressed(InputEvent.Key event, KeyMapping keyBinding)
	{
		return event.getKey() == keyBinding.getKey().getValue() && keyBinding.consumeClick();
	}
}
