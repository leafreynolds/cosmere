/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.client;

import leaf.cosmere.allomancy.common.Allomancy;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.lwjgl.glfw.GLFW;

import static leaf.cosmere.api.Constants.Strings.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Allomancy.MODID, bus = Bus.MOD)
public class AllomancyKeybindings
{
	public static KeyMapping ALLOMANCY_PUSH;
	public static KeyMapping ALLOMANCY_PULL;

	@SubscribeEvent
	public static void register(RegisterKeyMappingsEvent event)
	{
		event.register(ALLOMANCY_PUSH = new KeyMapping(KEY_ALLOMANCY_PUSH, GLFW.GLFW_KEY_TAB, KEYS_CATEGORY));
		event.register(ALLOMANCY_PULL = new KeyMapping(KEY_ALLOMANCY_PULL, GLFW.GLFW_KEY_R, KEYS_CATEGORY));
	}

}
