/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.feruchemy.common.capabilities.FeruchemySpiritwebSubmodule;
import leaf.cosmere.feruchemy.common.config.FeruchemyConfigs;
import leaf.cosmere.feruchemy.common.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Feruchemy.MODID)
public class Feruchemy implements IModModule
{
	public static final String MODID = "feruchemy";

	public static Feruchemy instance;

	public final Version versionNumber;

	public Feruchemy(IEventBus modBus, ModContainer modContainer)
	{
		Cosmere.addModule(instance = this);

		FeruchemyConfigs.registerConfigs(modContainer);

		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);
		FeruchemyItems.ITEMS.register(modBus);
		FeruchemyAttributes.ATTRIBUTES.register(modBus);
		FeruchemyManifestations.MANIFESTATIONS.register(modBus);
		FeruchemyLootFunctions.LOOT_FUNCTIONS.register(modBus);
		FeruchemyEffects.EFFECTS.register(modBus);
		FeruchemyCreativeTabs.CREATIVE_TABS.register(modBus);

		versionNumber = new Version(modContainer);
	}

	public static ResourceLocation rl(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(Feruchemy.MODID, path);
	}

	public void commonSetup(FMLCommonSetupEvent event)
	{
		CosmereAPI.logger.info("Cosmere: Feruchemy module Version {} initializing...", versionNumber);
	}

	@Override
	public Version getVersion()
	{
		return versionNumber;
	}

	@Override
	public String getName()
	{
		return "Feruchemy";
	}

	@Override
	public ISpiritwebSubmodule makeSubmodule()
	{
		return new FeruchemySpiritwebSubmodule();
	}

	private void handleConfigEvent(ModConfigEvent configEvent)
	{
		ModConfig config = configEvent.getConfig();
		if (config.getSpec() == FeruchemyConfigs.SERVER.getConfigSpec())
		{
			FeruchemyConfigs.SERVER.clearCache();
		}
	}

	private void onConfigLoad(ModConfigEvent.Loading configEvent)
	{
		handleConfigEvent(configEvent);
	}

	private void onConfigReload(ModConfigEvent.Reloading configEvent)
	{
		handleConfigEvent(configEvent);
	}
}
