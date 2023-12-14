/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.example.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.CosmereModConfig;
import leaf.cosmere.example.common.capabilities.ExampleSpiritwebSubmodule;
import leaf.cosmere.example.common.config.ExampleConfigs;
import leaf.cosmere.example.common.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Example.MODID)
public class Example implements IModModule
{
	public static final String MODID = "example";

	public static Example instance;

	public final Version versionNumber;

	public Example()
	{
		Cosmere.addModule(instance = this);
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		ExampleConfigs.registerConfigs(ModLoadingContext.get());

		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);
		modBus.addListener(this::imcQueue);

		ExampleAttributes.ATTRIBUTES.register(modBus);
		ExampleBiomes.BIOMES.register(modBus);
		ExampleBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modBus);
		ExampleBlocks.BLOCKS.register(modBus);
		ExampleEffects.EFFECTS.register(modBus);
		ExampleFeatures.CONFIGURED_FEATURES.register(modBus);
		ExampleFeatures.PLACED_FEATURES.register(modBus);
		ExampleEntityTypes.ENTITY_TYPES.register(modBus);
		ExampleItems.ITEMS.register(modBus);
		ExampleManifestations.MANIFESTATIONS.register(modBus);
		ExampleMenuTypes.MENU_TYPES.register(modBus);
		ExampleRecipes.SPECIAL_RECIPES.register(modBus);
		ExampleStats.STATS.register(modBus);

		versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
	}

	public static ResourceLocation rl(String path)
	{
		return new ResourceLocation(Example.MODID, path);
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		CosmereAPI.logger.info("Cosmere: Example module Version {} initializing...", versionNumber);

		event.enqueueWork(() ->
		{
			//AllomancyEntityTypes.PrepareEntityAttributes();
			ExampleStats.initStatEntries();
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
		return "Example";
	}

	@Override
	public ISpiritwebSubmodule makeSubmodule()
	{
		return new ExampleSpiritwebSubmodule();
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