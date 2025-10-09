/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.client;

import leaf.cosmere.api.Activator;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.manifestation.SurgebindingManifestation;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static leaf.cosmere.api.Constants.Strings.*;
import static leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations.SURGEBINDING_POWERS;

// Really only has its own file to more nicely reference keybindings.
// Otherwise, could have lived in mod client events
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Surgebinding.MODID, bus = Bus.MOD)
public class SurgebindingKeybindings
{
	public static KeyMapping SHARDBLADE;
	public static KeyMapping REQUEST_STORMLIGHT;
	public static KeyMapping DISPATCH_STORMLIGHT;

	public static final Map<Roshar.Surges, KeyMapping> SURGEBINDING_POWER =
			Arrays.stream(EnumUtils.SURGES)
					.collect(Collectors.toMap(
							Function.identity(),
							surge ->
									new KeyMapping(KEY_STORMLIGHT + surge.getName(), GLFW.GLFW_KEY_UNKNOWN, KEYS_ACTIVATE_CATEGORY)
					));

	@SubscribeEvent
	public static void register(RegisterKeyMappingsEvent event)
	{

		event.register(SHARDBLADE = new KeyMapping(KEY_SHARDBLADE, GLFW.GLFW_KEY_X, KEYS_CATEGORY));

		for (Roshar.Surges surge : SURGEBINDING_POWER.keySet())
		{
			KeyMapping key = SURGEBINDING_POWER.get(surge);
			SurgebindingManifestation manifest = (SurgebindingManifestation) SURGEBINDING_POWERS.get(surge).getManifestation();
			event.register(key);
			Activator entry = new Activator(key, manifest);
			entry.setCategory("stormlight");
			Keybindings.activators.add(entry);
		}

		event.register(SHARDBLADE = new KeyMapping(KEY_SHARDBLADE, GLFW.GLFW_KEY_X, "keys.surgebinding.main"));
		event.register(REQUEST_STORMLIGHT = new KeyMapping(KEY_REQUEST_STORMLIGHT, GLFW.GLFW_KEY_Z, "keys.surgebinding.main"));
		event.register(DISPATCH_STORMLIGHT = new KeyMapping(KEY_DISPATCH_STORMLIGHT, GLFW.GLFW_KEY_Y, "keys.surgebinding.main"));

	}

}
