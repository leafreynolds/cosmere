package leaf.cosmere.sandmastery.client;

import leaf.cosmere.sandmastery.common.Sandmastery;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static leaf.cosmere.api.Constants.Strings.*;
import static leaf.cosmere.api.Constants.Strings.KEYS_CATEGORY;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Sandmastery.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SandmasteryKeybindings {
    public static KeyMapping SANDMASTERY_USE;

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event)
    {
        event.register(SANDMASTERY_USE = new KeyMapping(KEY_SANDMASTERY_USE, GLFW.GLFW_KEY_RIGHT_CONTROL, KEYS_CATEGORY));
    }
}
