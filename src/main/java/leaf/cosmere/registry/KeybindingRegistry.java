/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.registry;

import leaf.cosmere.Cosmere;
import leaf.cosmere.client.settings.KeyConflictContext;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

import static leaf.cosmere.constants.Constants.Strings.*;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Cosmere.MODID, bus = Bus.MOD)
public class KeybindingRegistry
{

    public static KeyBinding MANIFESTATION_MENU;
    public static KeyBinding MANIFESTATION_TOGGLE;
    public static KeyBinding MANIFESTATION_MODE_NEXT;
    public static KeyBinding MANIFESTATION_MODE_PREVIOUS;

    public static KeyBinding ALLOMANCY_PUSH;
    public static KeyBinding ALLOMANCY_PULL;

    @SubscribeEvent
    public static void register(FMLClientSetupEvent event)
    {
        ClientRegistry.registerKeyBinding(MANIFESTATION_MENU = new KeyBinding(KEY_MANIFESTATION_MENU, GLFW.GLFW_KEY_G, KEYS_CATEGORY));
        ClientRegistry.registerKeyBinding(MANIFESTATION_TOGGLE = new KeyBinding(KEY_MANIFESTATION_TOGGLE, GLFW.GLFW_KEY_C, KEYS_CATEGORY));
        ClientRegistry.registerKeyBinding(MANIFESTATION_MODE_NEXT = new KeyBinding(KEY_MANIFESTATION_MODE_NEXT, GLFW.GLFW_KEY_V, KEYS_CATEGORY));
        ClientRegistry.registerKeyBinding(MANIFESTATION_MODE_PREVIOUS = createKeybinding(KEY_MANIFESTATION_MODE_PREVIOUS, KeyModifier.SHIFT, GLFW.GLFW_KEY_V, KEYS_CATEGORY));

        ClientRegistry.registerKeyBinding(ALLOMANCY_PUSH = new KeyBinding(KEY_ALLOMANCY_PUSH, GLFW.GLFW_KEY_TAB, KEYS_CATEGORY));
        ClientRegistry.registerKeyBinding(ALLOMANCY_PULL = new KeyBinding(KEY_ALLOMANCY_PULL, GLFW.GLFW_KEY_R, KEYS_CATEGORY));
    }

    public static KeyBinding createKeybinding(String description, KeyModifier keyModifier, int keyCode, String category)
    {
        return new KeyBinding(description, KeyConflictContext.DEFAULT, keyModifier, InputMappings.Type.KEYSYM.getOrCreate(keyCode), category);
    }
}
