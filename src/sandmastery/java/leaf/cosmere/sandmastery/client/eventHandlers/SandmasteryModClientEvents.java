/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
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
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Sandmastery.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
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

	@SubscribeEvent
	public static void registerMenuScreens(RegisterMenuScreensEvent event)
	{
		event.register((MenuType<SandPouchContainerMenu>) SandmasteryMenuTypes.SAND_POUCH.get(), SandPouchContainerScreen::new);
		event.register((MenuType<SandSpreaderMenu>) SandmasteryMenuTypes.SAND_SPREADER.get(), SandSpreaderScreen::new);
		CosmereAPI.logger.info("Sandmastery registered menutypes!");
	}

	@SubscribeEvent
	public static void RegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerEntityRenderer(SandmasteryEntityTypes.SAND_PROJECTILE.get(), ThrownItemRenderer::new);
	}
}
