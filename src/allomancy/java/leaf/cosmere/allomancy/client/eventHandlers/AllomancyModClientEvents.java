/*
 * File updated ~ 17 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.client.eventHandlers;

import leaf.cosmere.allomancy.client.gui.CoinPouchContainerScreen;
import leaf.cosmere.allomancy.client.render.AllomancyLayerDefinitions;
import leaf.cosmere.allomancy.client.render.AllomancyRenderers;
import leaf.cosmere.allomancy.client.render.model.MistcloakModel;
import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.coinpouch.CoinPouchContainerMenu;
import leaf.cosmere.allomancy.common.registries.AllomancyEntityTypes;
import leaf.cosmere.allomancy.common.registries.AllomancyMenuTypes;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
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

@Mod.EventBusSubscriber(modid = Allomancy.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AllomancyModClientEvents
{

	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		AllomancyRenderers.register();
		CosmereAPI.logger.info("Allomancy client setup complete!");
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void registerContainers(RegisterEvent event)
	{
		event.register(Registries.MENU, helper ->
		{
			MenuScreens.register((MenuType<CoinPouchContainerMenu>) AllomancyMenuTypes.COIN_POUCH.get(), CoinPouchContainerScreen::new);
			CosmereAPI.logger.info("Allomancy registered menutypes!");
		});
	}

	@SubscribeEvent
	public static void RegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerEntityRenderer(AllomancyEntityTypes.COIN_PROJECTILE.get(), ThrownItemRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions evt)
	{
		//shardplate
		evt.registerLayerDefinition(AllomancyLayerDefinitions.MISTCLOAK, MistcloakModel::createBodyLayer);
	}
}
