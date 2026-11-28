/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.surgebinding.client;

import leaf.cosmere.api.Activator;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.client.PowerKeyMappings;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.manifestation.SurgebindingManifestation;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

import static leaf.cosmere.api.Constants.Strings.KEY_SHARDBLADE;
import static leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations.KEYS_SURGEBINDING_CATEGORY;
import static leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations.SURGEBINDING_POWERS;

// Really only has its own file to more nicely reference keybindings.
// Otherwise, could have lived in mod client events
@EventBusSubscriber(value = Dist.CLIENT, modid = Surgebinding.MODID)
public class SurgebindingKeybindings
{
	public static KeyMapping SHARDBLADE;
	public static KeyMapping REQUEST_STORMLIGHT;
	public static KeyMapping DISPATCH_STORMLIGHT;

	public static final Map<Roshar.Surges, KeyMapping> SURGEBINDING_POWER = new HashMap<>();

	@SubscribeEvent
	public static void register(RegisterKeyMappingsEvent event)
	{
		SurgebindingManifestations.KEY_ACTIVATORS.forEach((surge, def) ->
		{
			KeyMapping key = PowerKeyMappings.register(event, def);
			SURGEBINDING_POWER.put(surge, key);
			SurgebindingManifestation manifest = (SurgebindingManifestation) SURGEBINDING_POWERS.get(surge).getManifestation();
			Activator entry = new Activator(key, manifest);
			entry.setCategory(KEYS_SURGEBINDING_CATEGORY);
			Keybindings.activators.add(entry);
		});

		// shardblade summoning isn't tied to a manifestation, so it stays a normal keybind
		event.register(SHARDBLADE = new KeyMapping(KEY_SHARDBLADE, GLFW.GLFW_KEY_X, KEYS_SURGEBINDING_CATEGORY));
		REQUEST_STORMLIGHT = PowerKeyMappings.register(event, SurgebindingManifestations.KEY_REQUEST_STORMLIGHT_DEF);
		DISPATCH_STORMLIGHT = PowerKeyMappings.register(event, SurgebindingManifestations.KEY_DISPATCH_STORMLIGHT_DEF);
	}

}
