package leaf.cosmere.feruchemy.client;


import leaf.cosmere.api.Activator;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyManifestation;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static leaf.cosmere.api.Constants.Strings.KEYS_ACTIVATE_CATEGORY;
import static leaf.cosmere.api.Constants.Strings.KEY_FERUCHEMY;
import static leaf.cosmere.feruchemy.common.registries.FeruchemyManifestations.FERUCHEMY_POWERS;

@EventBusSubscriber(value = Dist.CLIENT, modid = Feruchemy.MODID)
public class FeruchemyKeybindings
{
	public static final Map<Metals.MetalType, KeyMapping> FERUCHEMY_POWER =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.filter(Metals.MetalType::hasAssociatedManifestation)
					.collect(Collectors.toMap(
							Function.identity(),
							metalType ->
									new KeyMapping(KEY_FERUCHEMY + metalType.getName(), GLFW.GLFW_KEY_UNKNOWN, KEYS_ACTIVATE_CATEGORY)
					));

	@SubscribeEvent
	public static void register(RegisterKeyMappingsEvent event)
	{
		for (Metals.MetalType metalType : FERUCHEMY_POWER.keySet())
		{
			KeyMapping key = FERUCHEMY_POWER.get(metalType);
			FeruchemyManifestation manifest = FERUCHEMY_POWERS.get(metalType).getManifestation();
			event.register(key);
			Activator entry = new Activator(key, manifest);
			entry.setCategory("feruchemy");
			Keybindings.activators.add(entry);
		}
	}
}
