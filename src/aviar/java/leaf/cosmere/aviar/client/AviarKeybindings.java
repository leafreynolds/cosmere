/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.client;

import com.mojang.blaze3d.platform.InputConstants;
import leaf.cosmere.aviar.common.Aviar;
import leaf.cosmere.client.settings.KeyConflictContext;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyModifier;

@EventBusSubscriber(value = Dist.CLIENT, modid = Aviar.MODID)
public class AviarKeybindings
{

	//public static KeyMapping AVIAR_KEYBINDING;

	@SubscribeEvent
	public static void register(RegisterKeyMappingsEvent event)
	{
		//event.register((AVIAR_KEYBINDING = new KeyMapping("name", GLFW.GLFW_KEY_G, KEYS_CATEGORY)));

	}

	public static KeyMapping createKeybinding(String description, KeyModifier keyModifier, int keyCode, String category)
	{
		return new KeyMapping(description, KeyConflictContext.DEFAULT, keyModifier, InputConstants.Type.KEYSYM.getOrCreate(keyCode), category);
	}
}
