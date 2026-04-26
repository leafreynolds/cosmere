/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.awakening.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.awakening.common.capabilities.AwakeningSpiritwebSubmodule;
import leaf.cosmere.awakening.common.config.AwakeningConfigs;
import leaf.cosmere.awakening.common.registries.*;
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

@Mod(Awakening.MODID)
public class Awakening implements IModModule
{
	public static final String MODID = "awakening";

	public static Awakening instance;

	public final Version versionNumber;

	public Awakening(IEventBus modBus, ModContainer modContainer)
	{
		Cosmere.addModule(instance = this);

		AwakeningConfigs.registerConfigs(modContainer);

		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);

		AwakeningAttributes.ATTRIBUTES.register(modBus);
		AwakeningBiomes.BIOMES.register(modBus);
		AwakeningBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modBus);
		AwakeningBlocks.BLOCKS.register(modBus);
		//AwakeningEffects.EFFECTS.register(modBus);
		AwakeningFeatures.CONFIGURED_FEATURES.register(modBus);
		AwakeningFeatures.PLACED_FEATURES.register(modBus);
		AwakeningEntityTypes.ENTITY_TYPES.register(modBus);
		AwakeningItems.ITEMS.register(modBus);
		AwakeningManifestations.MANIFESTATIONS.register(modBus);
		AwakeningMenuTypes.MENU_TYPES.register(modBus);
		AwakeningRecipes.SPECIAL_RECIPES.register(modBus);
		AwakeningStats.STATS.register(modBus);

		versionNumber = new Version(modContainer);
	}

	public static ResourceLocation rl(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(Awakening.MODID, path);
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		CosmereAPI.logger.info("Cosmere: Awakening module Version {} initializing...", versionNumber);

		event.enqueueWork(() ->
		{
			//AllomancyEntityTypes.PrepareEntityAttributes();
			AwakeningStats.initStatEntries();
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
		return "Awakening";
	}

	@Override
	public ISpiritwebSubmodule makeSubmodule()
	{
		return new AwakeningSpiritwebSubmodule();
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
		for (ICosmereConfig cosmereConfig : List.of(AwakeningConfigs.SERVER))
		{
			if (cosmereConfig.getConfigSpec() == config.getSpec())
			{
				cosmereConfig.clearCache();
				return;
			}
		}
	}
}
