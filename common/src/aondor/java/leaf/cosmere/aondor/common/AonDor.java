/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
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
import leaf.cosmere.common.config.CosmereModConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AonDor.MODID)
public class AonDor implements IModModule
{
	public static final String MODID = "aondor";

	public static AonDor instance;

	public final Version versionNumber;

	public AonDor()
	{
		Cosmere.addModule(instance = this);
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		AonDorConfigs.registerConfigs(ModLoadingContext.get());

		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);
		modBus.addListener(this::imcQueue);

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

		versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
	}

	public static ResourceLocation rl(String path)
	{
		return new ResourceLocation(AonDor.MODID, path);
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		CosmereAPI.logger.info("Cosmere: AonDor module Version {} initializing...", versionNumber);

		event.enqueueWork(() ->
		{
			//AllomancyEntityTypes.PrepareEntityAttributes();
			AonDorStats.initStatEntries();
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
		return "AonDor";
	}

	@Override
	public ISpiritwebSubmodule makeSubmodule()
	{
		return new AonDorSpiritwebSubmodule();
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