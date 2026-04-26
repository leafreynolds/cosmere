/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.common.config.ICosmereConfig;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.tools.common.capabilities.ToolsSpiritwebSubmodule;
import leaf.cosmere.tools.common.config.ToolsConfigs;
import leaf.cosmere.tools.common.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

@Mod(CosmereTools.MODID)
public class CosmereTools implements IModModule
{
	public static final String MODID = "cosmeretools";

	public static CosmereTools instance;

	public final Version versionNumber;

	public CosmereTools(IEventBus modBus, ModContainer modContainer)
	{
		Cosmere.addModule(instance = this);

		ToolsConfigs.registerConfigs(modContainer);

		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);

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

		versionNumber = new Version(modContainer);
	}

	public static ResourceLocation rl(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(CosmereTools.MODID, path);
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		CosmereAPI.logger.info("Cosmere: Tools module Version {} initializing...", versionNumber);

		event.enqueueWork(() ->
		{
			//AllomancyEntityTypes.PrepareEntityAttributes();
			ToolsStats.initStatEntries();
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
		return "Cosmere: Tools";
	}

	@Override
	public ISpiritwebSubmodule makeSubmodule()
	{
		return new ToolsSpiritwebSubmodule();
	}

	private void handleConfigEvent(ModConfigEvent event)
	{
		var config = event.getConfig();
		for (ICosmereConfig cosmereConfig : List.of(ToolsConfigs.SERVER))
		{
			if (config.getModId().equals(MODID) && config.getSpec() == cosmereConfig.getConfigSpec())
			{
				cosmereConfig.clearCache();
				break;
			}
		}
	}

	private void onConfigLoad(ModConfigEvent.Loading event)
	{
		handleConfigEvent(event);
	}

	private void onConfigReload(ModConfigEvent.Reloading event)
	{
		handleConfigEvent(event);
	}
}