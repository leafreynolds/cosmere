/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.client.eventHandlers;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Taldain;
import leaf.cosmere.sandmastery.client.gui.SandPouchContainerScreen;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.registries.SandmasteryMenuTypes;
import leaf.cosmere.sandmastery.common.sandpouch.SandPouchContainerMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegisterEvent;

import java.util.Locale;

@Mod.EventBusSubscriber(modid = Sandmastery.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SandmasteryClientSetup
{


	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		event.enqueueWork(() ->
		{

		});


		CosmereAPI.logger.info("Sandmastery client setup complete!");
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void registerContainers(RegisterEvent event)
	{
		event.register(Registry.MENU_REGISTRY, helper ->
		{
			MenuScreens.register((MenuType<SandPouchContainerMenu>) SandmasteryMenuTypes.SAND_POUCH.get(), SandPouchContainerScreen::new);
			CosmereAPI.logger.info("Sandmastery registered menutypes!");
		});
	}

	@SubscribeEvent
	public static void RegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
	}

	//special thank you to the chisels and bits team who have an example of how to register other sprites
	@SubscribeEvent
	public static void registerIconTextures(TextureStitchEvent.Pre event)
	{
		final TextureAtlas map = event.getAtlas();
		if (!map.location().equals(InventoryMenu.BLOCK_ATLAS))
		{
			return;
		}

		event.addSprite(Sandmastery.rl("icon/sandmastery"));

		for (final Taldain.Mastery manifestation : Taldain.Mastery.values())
		{
			String abilityToLower = manifestation.toString().toLowerCase(Locale.ROOT);
			event.addSprite(Sandmastery.rl("icon/sandmastery/"+abilityToLower));
		}
	}

}
