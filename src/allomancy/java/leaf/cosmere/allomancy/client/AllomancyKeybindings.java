/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.client;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.manifestation.AllomancyManifestation;
import leaf.cosmere.api.Metals;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.api.Activator;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.lwjgl.glfw.GLFW;
import leaf.cosmere.api.EnumUtils;
import java.util.function.Function;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static leaf.cosmere.allomancy.common.registries.AllomancyManifestations.ALLOMANCY_POWERS;
import static leaf.cosmere.api.Constants.Strings.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Allomancy.MODID, bus = Bus.MOD)
public class AllomancyKeybindings
{
	public static KeyMapping ALLOMANCY_STEEL_PUSH;
	public static KeyMapping ALLOMANCY_IRON_PULL;
	public static KeyMapping ALLOMANCY_SOOTHE;
	public static KeyMapping ALLOMANCY_RIOT;

	
	public static final Map<Metals.MetalType,KeyMapping> ALLOMANCY_POWER =
			Arrays.stream(EnumUtils.METAL_TYPES)
				.filter(Metals.MetalType::hasAssociatedManifestation)
					.collect(Collectors.toMap(
						Function.identity(),
						metalType ->
							new KeyMapping(KEY_ALLOMANCY + metalType.getName(),GLFW.GLFW_KEY_UNKNOWN,KEYS_ACTIVATE_CATEGORY)
					));

	@SubscribeEvent
	public static void register(RegisterKeyMappingsEvent event)
	{
		event.register(ALLOMANCY_STEEL_PUSH = new KeyMapping(KEY_ALLOMANCY_STEEL_PUSH, GLFW.GLFW_KEY_TAB, KEYS_CATEGORY));
		event.register(ALLOMANCY_IRON_PULL = new KeyMapping(KEY_ALLOMANCY_IRON_PULL, GLFW.GLFW_KEY_R, KEYS_CATEGORY));
		event.register(ALLOMANCY_SOOTHE = new KeyMapping(KEY_ALLOMANCY_SOOTHE, GLFW.GLFW_KEY_UNKNOWN, KEYS_CATEGORY));
		event.register(ALLOMANCY_RIOT = new KeyMapping(KEY_ALLOMANCY_RIOT, GLFW.GLFW_KEY_UNKNOWN, KEYS_CATEGORY));

	

		for (Metals.MetalType metalType: ALLOMANCY_POWER.keySet())
		{
			KeyMapping key = ALLOMANCY_POWER.get(metalType);
			AllomancyManifestation manifest = ALLOMANCY_POWERS.get(metalType).getManifestation();
			event.register(key);
			Activator entry = new Activator(key, manifest);
			entry.setCategory("allomancy");
			Keybindings.activators.add(entry);
		}
	}

}
