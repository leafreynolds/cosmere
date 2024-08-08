/*
 * File updated ~ 7 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.client.eventHandlers;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.sandmastery.client.gui.SandPouchContainerScreen;
import leaf.cosmere.sandmastery.client.gui.SandSpreaderScreen;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.blocks.entities.SandSpreader.SandSpreaderMenu;
import leaf.cosmere.sandmastery.common.items.sandpouch.SandPouchContainerMenu;
import leaf.cosmere.sandmastery.common.registries.SandmasteryEntityTypes;
import leaf.cosmere.sandmastery.common.registries.SandmasteryMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = Sandmastery.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SandmasteryModClientEvents
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
		event.register(Registries.MENU, helper ->
		{
			MenuScreens.register((MenuType<SandPouchContainerMenu>) SandmasteryMenuTypes.SAND_POUCH.get(), SandPouchContainerScreen::new);
			MenuScreens.register((MenuType<SandSpreaderMenu>) SandmasteryMenuTypes.SAND_SPREADER.get(), SandSpreaderScreen::new);
			CosmereAPI.logger.info("Sandmastery registered menutypes!");
		});
	}

	@SubscribeEvent
	public static void RegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerEntityRenderer(SandmasteryEntityTypes.SAND_PROJECTILE.get(), ThrownItemRenderer::new);
	}

/*	//special thank you to the chisels and bits team who have an example of how to register other sprites
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
			event.addSprite(Sandmastery.rl("icon/sandmastery/" + abilityToLower));
		}
	}*/

}
