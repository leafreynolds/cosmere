/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.awakening.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.CosmereModConfig;
import leaf.cosmere.awakening.common.capabilities.AwakeningSpiritwebSubmodule;
import leaf.cosmere.awakening.common.config.AwakeningConfigs;
import leaf.cosmere.awakening.common.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Awakening.MODID)
public class Awakening implements IModModule
{
	public static final String MODID = "awakening";

	public static Awakening instance;

	public final Version versionNumber;

	public Awakening()
	{
		Cosmere.addModule(instance = this);
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		AwakeningConfigs.registerConfigs(ModLoadingContext.get());

		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);
		modBus.addListener(this::imcQueue);

		AwakeningAttributes.ATTRIBUTES.register(modBus);
		AwakeningBiomes.BIOMES.register(modBus);
		AwakeningBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modBus);
		AwakeningBlocks.BLOCKS.register(modBus);
		AwakeningEffects.EFFECTS.register(modBus);
		AwakeningFeatures.CONFIGURED_FEATURES.register(modBus);
		AwakeningFeatures.PLACED_FEATURES.register(modBus);
		AwakeningEntityTypes.ENTITY_TYPES.register(modBus);
		AwakeningItems.ITEMS.register(modBus);
		AwakeningManifestations.MANIFESTATIONS.register(modBus);
		AwakeningMenuTypes.MENU_TYPES.register(modBus);
		AwakeningRecipes.SPECIAL_RECIPES.register(modBus);
		AwakeningStats.STATS.register(modBus);

		versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
	}

	public static ResourceLocation rl(String path)
	{
		return new ResourceLocation(Awakening.MODID, path);
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		CosmereAPI.logger.info("Cosmere: Awakening module Version {} initializing...", versionNumber);

		event.enqueueWork(() ->
		{
			//AllomancyEntityTypes.PrepareEntityAttributes();
			AwakeningStats.initStatEntries();
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
		return "Awakening";
	}

	@Override
	public ISpiritwebSubmodule makeSubmodule()
	{
		return new AwakeningSpiritwebSubmodule();
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