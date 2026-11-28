/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.client;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.manifestation.AllomancyManifestation;
import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.Activator;
import leaf.cosmere.api.Metals;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.client.PowerKeyMappings;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import java.util.HashMap;
import java.util.Map;

import static leaf.cosmere.allomancy.common.registries.AllomancyManifestations.ALLOMANCY_POWERS;

@EventBusSubscriber(value = Dist.CLIENT, modid = Allomancy.MODID)
public class AllomancyKeybindings
{
	public static KeyMapping ALLOMANCY_STEEL_PUSH;
	public static KeyMapping ALLOMANCY_IRON_PULL;
	public static KeyMapping ALLOMANCY_SOOTHE;
	public static KeyMapping ALLOMANCY_RIOT;

	public static final Map<Metals.MetalType, KeyMapping> ALLOMANCY_POWER = new HashMap<>();

	@SubscribeEvent
	public static void register(RegisterKeyMappingsEvent event)
	{
		ALLOMANCY_STEEL_PUSH = PowerKeyMappings.register(event, AllomancyManifestations.KEY_STEEL_PUSH);
		ALLOMANCY_IRON_PULL = PowerKeyMappings.register(event, AllomancyManifestations.KEY_IRON_PULL);
		ALLOMANCY_SOOTHE = PowerKeyMappings.register(event, AllomancyManifestations.KEY_SOOTHE);
		ALLOMANCY_RIOT = PowerKeyMappings.register(event, AllomancyManifestations.KEY_RIOT);

		AllomancyManifestations.KEY_ACTIVATORS.forEach((metalType, def) ->
		{
			KeyMapping key = PowerKeyMappings.register(event, def);
			ALLOMANCY_POWER.put(metalType, key);
			AllomancyManifestation manifest = ALLOMANCY_POWERS.get(metalType).getManifestation();
			Activator entry = new Activator(key, manifest);
			entry.setCategory("allomancy");
			Keybindings.activators.add(entry);
		});
	}

}
