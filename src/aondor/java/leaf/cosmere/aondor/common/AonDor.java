/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.aondor.common;

import leaf.cosmere.aondor.common.capabilities.AonDorSpiritwebSubmodule;
import leaf.cosmere.aondor.common.config.AonDorConfigs;
import leaf.cosmere.aondor.common.registries.*;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.ICosmereConfig;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

@Mod(AonDor.MODID)
public class AonDor implements IModModule
{
	public static final String MODID = "aondor";

	public static AonDor instance;

	public final Version versionNumber;

	public AonDor(IEventBus modBus, ModContainer modContainer)
	{
		Cosmere.addModule(instance = this);

		AonDorConfigs.registerConfigs(modContainer);

		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);

		AonDorAttributes.ATTRIBUTES.register(modBus);
		AonDorBiomes.BIOMES.register(modBus);
		AonDorBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modBus);
		AonDorBlocks.BLOCKS.register(modBus);
		AonDorEffects.EFFECTS.register(modBus);
		AonDorFeatures.CONFIGURED_FEATURES.register(modBus);
		AonDorFeatures.PLACED_FEATURES.register(modBus);
		AonDorEntityTypes.ENTITY_TYPES.register(modBus);
		AonDorItems.ITEMS.register(modBus);
		AonDorManifestations.MANIFESTATIONS.register(modBus);
		AonDorMenuTypes.MENU_TYPES.register(modBus);
		AonDorRecipes.SPECIAL_RECIPES.register(modBus);
		AonDorStats.STATS.register(modBus);

		versionNumber = new Version(modContainer);
	}

	public static ResourceLocation rl(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(AonDor.MODID, path);
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		CosmereAPI.logger.info("Cosmere: AonDor module Version {} initializing...", versionNumber);

		event.enqueueWork(() ->
		{
			//AllomancyEntityTypes.PrepareEntityAttributes();
			AonDorStats.initStatEntries();
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
		return "AonDor";
	}

	@Override
	public ISpiritwebSubmodule makeSubmodule()
	{
		return new AonDorSpiritwebSubmodule();
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
		for (ICosmereConfig cosmereConfig : List.of(AonDorConfigs.SERVER))
		{
			if (cosmereConfig.getConfigSpec() == config.getSpec())
			{
				cosmereConfig.clearCache();
				return;
			}
		}
	}
}
