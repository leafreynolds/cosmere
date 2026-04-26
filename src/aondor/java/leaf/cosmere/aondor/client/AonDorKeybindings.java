/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.aondor.client;

import com.mojang.blaze3d.platform.InputConstants;
import leaf.cosmere.aondor.common.AonDor;
import leaf.cosmere.client.settings.KeyConflictContext;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;

@EventBusSubscriber(value = Dist.CLIENT, modid = AonDor.MODID, bus = Bus.MOD)
public class AonDorKeybindings
{

	//public static KeyMapping AONDOR_KEYBINDING;

	@SubscribeEvent
	public static void register(RegisterKeyMappingsEvent event)
	{
		//event.register((AONDOR_KEYBINDING = new KeyMapping("name", GLFW.GLFW_KEY_G, KEYS_CATEGORY)));

	}

	public static KeyMapping createKeybinding(String description, KeyModifier keyModifier, int keyCode, String category)
	{
		return new KeyMapping(description, KeyConflictContext.DEFAULT, keyModifier, InputConstants.Type.KEYSYM.getOrCreate(keyCode), category);
	}
}
