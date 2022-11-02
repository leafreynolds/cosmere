/*
 * File updated ~ 2 - 11 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.client;

import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.lwjgl.glfw.GLFW;

import static leaf.cosmere.api.Constants.Strings.KEYS_CATEGORY;
import static leaf.cosmere.api.Constants.Strings.KEY_SHARDBLADE;

// Really only has its own file to more nicely reference keybindings.
// Otherwise, could have lived in mod client events
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Surgebinding.MODID, bus = Bus.MOD)
public class SurgebindingKeybindings
{
	public static KeyMapping SHARDBLADE;

	@SubscribeEvent
	public static void register(RegisterKeyMappingsEvent event)
	{
		event.register(SHARDBLADE = new KeyMapping(KEY_SHARDBLADE, GLFW.GLFW_KEY_X, KEYS_CATEGORY));
	}

}
