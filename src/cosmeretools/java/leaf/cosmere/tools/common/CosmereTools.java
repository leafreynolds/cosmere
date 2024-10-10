/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.CosmereModConfig;
import leaf.cosmere.tools.common.capabilities.ToolsSpiritwebSubmodule;
import leaf.cosmere.tools.common.config.ToolsConfigs;
import leaf.cosmere.tools.common.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CosmereTools.MODID)
public class CosmereTools implements IModModule
{
	public static final String MODID = "cosmeretools";

	public static CosmereTools instance;

	public final Version versionNumber;

	public CosmereTools()
	{
		Cosmere.addModule(instance = this);
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		ToolsConfigs.registerConfigs(ModLoadingContext.get());

		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);
		modBus.addListener(this::imcQueue);

		ToolsAttributes.ATTRIBUTES.register(modBus);
		ToolsBiomes.BIOMES.register(modBus);
		ToolsBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modBus);
		ToolsBlocks.BLOCKS.register(modBus);
		ToolsEffects.EFFECTS.register(modBus);
		ToolsFeatures.CONFIGURED_FEATURES.register(modBus);
		ToolsFeatures.PLACED_FEATURES.register(modBus);
		ToolsEntityTypes.ENTITY_TYPES.register(modBus);
		ToolsItems.ITEMS.register(modBus);
		ToolsManifestations.MANIFESTATIONS.register(modBus);
		ToolsMenuTypes.MENU_TYPES.register(modBus);
		ToolsRecipes.SPECIAL_RECIPES.register(modBus);
		ToolsStats.STATS.register(modBus);
		ToolsCreativeTabs.CREATIVE_TABS.register(modBus);

		versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
	}

	public static ResourceLocation rl(String path)
	{
		return new ResourceLocation(CosmereTools.MODID, path);
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		CosmereAPI.logger.info("Cosmere: Tools module Version {} initializing...", versionNumber);

		event.enqueueWork(() ->
		{
			//AllomancyEntityTypes.PrepareEntityAttributes();
			ToolsStats.initStatEntries();
		});


		//packetHandler.initialize();
	}

	private void imcQueue(InterModEnqueueEvent event)
	{
	}

	@Override
	public Version getVersion()
	{
		return versionNumber;
	}

	@Override
	public String getName()
	{
		return "Cosmere: Tools";
	}

	@Override
	public ISpiritwebSubmodule makeSubmodule()
	{
		return new ToolsSpiritwebSubmodule();
	}

	private void onConfigLoad(ModConfigEvent configEvent)
	{
		ModConfig config = configEvent.getConfig();
		if (config.getModId().equals(MODID) && config instanceof CosmereModConfig cosmereModConfig)
		{
			cosmereModConfig.clearCache();
		}
	}

	private void onConfigReload(ModConfigEvent.Reloading configEvent)
	{
		ModConfig config = configEvent.getConfig();
		if (config.getModId().equals(MODID) && config instanceof CosmereModConfig cosmereModConfig)
		{
			cosmereModConfig.clearCache();
		}
	}
}