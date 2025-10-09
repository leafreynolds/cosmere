/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 * File updated ~ 2 - 5 - 2025 ~ SoaringEaqle
 */

package leaf.cosmere.client;

import com.mojang.blaze3d.platform.InputConstants;
import leaf.cosmere.api.Activator;
import leaf.cosmere.client.settings.KeyConflictContext;
import leaf.cosmere.common.Cosmere;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Vector;

import static leaf.cosmere.api.Constants.Strings.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Cosmere.MODID, bus = Bus.MOD)
public class Keybindings
{

	public static ArrayList<Activator> activators = new ArrayList<leaf.cosmere.api.Activator>();

	public static KeyMapping MANIFESTATION_MENU;
	public static KeyMapping MANIFESTATIONS_DEACTIVATE;
	public static KeyMapping MANIFESTATION_NEXT;
	public static KeyMapping MANIFESTATION_PREVIOUS;
	public static KeyMapping MANIFESTATION_USE_ACTIVE;

	public static KeyMapping MANIFESTATION_MODE_INCREASE;
	public static KeyMapping MANIFESTATION_MODE_DECREASE;

	public static KeyMapping ACTIVATE_POWER_SAVE;
	public static KeyMapping SAVE_POWER_SAVE;
	@SubscribeEvent
	public static void register(RegisterKeyMappingsEvent event)
	{
		event.register((MANIFESTATION_MENU = new KeyMapping(KEY_MANIFESTATION_MENU, GLFW.GLFW_KEY_G, KEYS_CATEGORY)));
		event.register(MANIFESTATIONS_DEACTIVATE = new KeyMapping(KEY_DEACTIVATE_ALL_POWERS, GLFW.GLFW_KEY_C, KEYS_CATEGORY));

		event.register(MANIFESTATION_NEXT = new KeyMapping(KEY_MANIFESTATION_NEXT, GLFW.GLFW_KEY_V, KEYS_CATEGORY));
		event.register(MANIFESTATION_PREVIOUS = createKeybinding(KEY_MANIFESTATION_PREVIOUS, KeyModifier.SHIFT, GLFW.GLFW_KEY_V, KEYS_CATEGORY));

		event.register(MANIFESTATION_USE_ACTIVE = new KeyMapping(KEY_MANIFESTATION_USE_ACTIVE, GLFW.GLFW_KEY_RIGHT_CONTROL, KEYS_CATEGORY));

		event.register(MANIFESTATION_MODE_INCREASE = new KeyMapping(KEY_MANIFESTATION_MODE_INCREASE, GLFW.GLFW_KEY_KP_ADD, KEYS_CATEGORY));
		event.register(MANIFESTATION_MODE_DECREASE = new KeyMapping(KEY_MANIFESTATION_MODE_DECREASE, GLFW.GLFW_KEY_KP_SUBTRACT, KEYS_CATEGORY));

		event.register(ACTIVATE_POWER_SAVE = new KeyMapping(KEY_ACTIVATE, GLFW.GLFW_KEY_R, KEYS_CATEGORY));
		event.register(SAVE_POWER_SAVE = new KeyMapping(KEY_SAVE_ACTIVATOR, GLFW.GLFW_KEY_T, KEYS_CATEGORY));

	}

	public static KeyMapping createKeybinding(String description, KeyModifier keyModifier, int keyCode, String category)
	{
		return new KeyMapping(description, KeyConflictContext.DEFAULT, keyModifier, InputConstants.Type.KEYSYM.getOrCreate(keyCode), category);
	}

	public static KeyMapping getKey(int i)
	{
		return Minecraft.getInstance().options.keyHotbarSlots[i];
	}

}
