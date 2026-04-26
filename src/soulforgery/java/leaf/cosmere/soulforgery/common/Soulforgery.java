/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.soulforgery.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.ICosmereConfig;
import leaf.cosmere.soulforgery.common.capabilities.SoulforgerySpiritwebSubmodule;
import leaf.cosmere.soulforgery.common.config.SoulforgeryConfigs;
import leaf.cosmere.soulforgery.common.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

@Mod(Soulforgery.MODID)
public class Soulforgery implements IModModule
{
	public static final String MODID = "soulforgery";

	public static Soulforgery instance;

	public final Version versionNumber;

	public Soulforgery(IEventBus modBus, ModContainer modContainer)
	{
		Cosmere.addModule(instance = this);

		SoulforgeryConfigs.registerConfigs(modContainer);

		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);

		SoulforgeryAttributes.ATTRIBUTES.register(modBus);
		SoulforgeryBiomes.BIOMES.register(modBus);
		SoulforgeryBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modBus);
		SoulforgeryBlocks.BLOCKS.register(modBus);
		SoulforgeryEffects.EFFECTS.register(modBus);
		SoulforgeryFeatures.CONFIGURED_FEATURES.register(modBus);
		SoulforgeryFeatures.PLACED_FEATURES.register(modBus);
		SoulforgeryEntityTypes.ENTITY_TYPES.register(modBus);
		SoulforgeryItems.ITEMS.register(modBus);
		SoulforgeryManifestations.MANIFESTATIONS.register(modBus);
		SoulforgeryMenuTypes.MENU_TYPES.register(modBus);
		SoulforgeryRecipes.SPECIAL_RECIPES.register(modBus);
		SoulforgeryStats.STATS.register(modBus);

		versionNumber = new Version(modContainer);
	}

	public static ResourceLocation rl(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(Soulforgery.MODID, path);
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		CosmereAPI.logger.info("Cosmere: Soulforgery module Version {} initializing...", versionNumber);

		event.enqueueWork(() ->
		{
			//AllomancyEntityTypes.PrepareEntityAttributes();
			SoulforgeryStats.initStatEntries();
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
		return "Soulforgery";
	}

	@Override
	public ISpiritwebSubmodule makeSubmodule()
	{
		return new SoulforgerySpiritwebSubmodule();
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
		for (ICosmereConfig cosmereConfig : List.of(SoulforgeryConfigs.SERVER))
		{
			if (cosmereConfig.getConfigSpec() == config.getSpec())
			{
				cosmereConfig.clearCache();
				return;
			}
		}
	}
}
