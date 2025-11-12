package leaf.cosmere.metallurgy.client.eventHandlers;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.metallurgy.client.gui.MetallurgyWorkbenchScreen;
import leaf.cosmere.metallurgy.common.Metallurgy;
import leaf.cosmere.metallurgy.common.menus.MetallurgyWorkbenchMenu;
import leaf.cosmere.metallurgy.common.registries.MetallurgyMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = Metallurgy.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MetallurgyModClientEvents
{
	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		event.enqueueWork(() ->
		{
			// Future client setup code here
		});

		CosmereAPI.logger.info("Metallurgy client setup complete!");
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void registerScreens(RegisterEvent event)
	{
		event.register(Registries.MENU, helper ->
		{
			MenuScreens.register((MenuType<MetallurgyWorkbenchMenu>) MetallurgyMenuTypes.METALLURGY_WORKBENCH.get(), MetallurgyWorkbenchScreen::new);
			CosmereAPI.logger.info("Metallurgy registered menu screens!");
		});
	}
}
