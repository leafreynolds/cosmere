package leaf.cosmere.sandmastery.client;

import leaf.cosmere.client.PowerKeyMappings;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.registries.SandmasteryManifestations;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = Sandmastery.MODID)
public class SandmasteryKeybindings
{

	public static KeyMapping SANDMASTERY_LAUNCH;
	public static KeyMapping SANDMASTERY_ELEVATE;
	public static KeyMapping SANDMASTERY_PROJECTILE;
	public static KeyMapping SANDMASTERY_PLATFORM;

	@SubscribeEvent
	public static void register(RegisterKeyMappingsEvent event)
	{
		SANDMASTERY_LAUNCH = PowerKeyMappings.register(event, SandmasteryManifestations.KEY_LAUNCH);
		SANDMASTERY_ELEVATE = PowerKeyMappings.register(event, SandmasteryManifestations.KEY_ELEVATE);
		SANDMASTERY_PROJECTILE = PowerKeyMappings.register(event, SandmasteryManifestations.KEY_PROJECTILE);
		SANDMASTERY_PLATFORM = PowerKeyMappings.register(event, SandmasteryManifestations.KEY_PLATFORM);
	}
}
