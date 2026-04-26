/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.example.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.ICosmereConfig;
import leaf.cosmere.example.common.capabilities.ExampleSpiritwebSubmodule;
import leaf.cosmere.example.common.config.ExampleConfigs;
import leaf.cosmere.example.common.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

@Mod(Example.MODID)
public class Example implements IModModule
{
	public static final String MODID = "example";

	public static Example instance;

	public final Version versionNumber;

	public Example(IEventBus modBus, ModContainer modContainer)
	{
		Cosmere.addModule(instance = this);

		ExampleConfigs.registerConfigs(modContainer);

		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);

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
		ExampleCreativeTabs.CREATIVE_TABS.register(modBus);

		versionNumber = new Version(modContainer);
	}

	public static ResourceLocation rl(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(Example.MODID, path);
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		CosmereAPI.logger.info("Cosmere: Example module Version {} initializing...", versionNumber);

		event.enqueueWork(() ->
		{
			//AllomancyEntityTypes.PrepareEntityAttributes();
			ExampleStats.initStatEntries();
		});
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

	private void onConfigLoad(ModConfigEvent.Loading configEvent)
	{
		handleConfigEvent(configEvent);
	}

	private void onConfigReload(ModConfigEvent.Reloading configEvent)
	{
		handleConfigEvent(configEvent);
	}

	private void handleConfigEvent(ModConfigEvent event)
	{
		ModConfig config = event.getConfig();
		if (!config.getModId().equals(MODID))
		{
			return;
		}
		for (ICosmereConfig cosmereConfig : List.of(ExampleConfigs.SERVER))
		{
			if (cosmereConfig.getConfigSpec() == config.getSpec())
			{
				cosmereConfig.clearCache();
				return;
			}
		}
	}
}