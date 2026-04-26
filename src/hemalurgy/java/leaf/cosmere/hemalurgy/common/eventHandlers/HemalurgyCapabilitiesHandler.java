/*
 * File updated ~ 30 - 7 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.eventHandlers;

import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.capabilities.world.HemalurgyWorldCapability;
import leaf.cosmere.hemalurgy.common.capabilities.world.IHemalurgyWorldCap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = Hemalurgy.MODID, bus = EventBusSubscriber.Bus.GAME)
public class HemalurgyCapabilitiesHandler
{
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event)
	{
		Level level = event.getLevel();
		if (level.isClientSide())
		{
			return;
		}

		// Koloss patrols only spawn in the overworld
		if (!level.dimension().equals(Level.OVERWORLD))
		{
			return;
		}

		MinecraftServer server = level.getServer();
		if (server != null && server.getPlayerList().getPlayerCount() == 0)
		{
			return;
		}

		HemalurgyWorldCapability.get(level).ifPresent(IHemalurgyWorldCap::tick);
	}
}
