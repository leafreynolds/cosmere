/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.example.client;

import com.mojang.blaze3d.platform.InputConstants;
import leaf.cosmere.client.settings.KeyConflictContext;
import leaf.cosmere.example.common.Example;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Example.MODID, bus = Bus.MOD)
public class ExampleKeybindings
{

	//public static KeyMapping EXAMPLE_KEYBINDING;

	@SubscribeEvent
	public static void register(RegisterKeyMappingsEvent event)
	{
		//event.register((EXAMPLE_KEYBINDING = new KeyMapping("name", GLFW.GLFW_KEY_G, KEYS_CATEGORY)));

	}

	public static KeyMapping createKeybinding(String description, KeyModifier keyModifier, int keyCode, String category)
	{
		return new KeyMapping(description, KeyConflictContext.DEFAULT, keyModifier, InputConstants.Type.KEYSYM.getOrCreate(keyCode), category);
	}
}
