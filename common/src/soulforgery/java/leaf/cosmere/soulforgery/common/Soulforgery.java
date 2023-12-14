/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.soulforgery.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.CosmereModConfig;
import leaf.cosmere.soulforgery.common.capabilities.SoulforgerySpiritwebSubmodule;
import leaf.cosmere.soulforgery.common.config.SoulforgeryConfigs;
import leaf.cosmere.soulforgery.common.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Soulforgery.MODID)
public class Soulforgery implements IModModule
{
	public static final String MODID = "soulforgery";

	public static Soulforgery instance;

	public final Version versionNumber;

	public Soulforgery()
	{
		Cosmere.addModule(instance = this);
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		SoulforgeryConfigs.registerConfigs(ModLoadingContext.get());

		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);
		modBus.addListener(this::imcQueue);

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

		versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
	}

	public static ResourceLocation rl(String path)
	{
		return new ResourceLocation(Soulforgery.MODID, path);
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		CosmereAPI.logger.info("Cosmere: Soulforgery module Version {} initializing...", versionNumber);

		event.enqueueWork(() ->
		{
			//AllomancyEntityTypes.PrepareEntityAttributes();
			SoulforgeryStats.initStatEntries();
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
		return "Soulforgery";
	}

	@Override
	public ISpiritwebSubmodule makeSubmodule()
	{
		return new SoulforgerySpiritwebSubmodule();
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