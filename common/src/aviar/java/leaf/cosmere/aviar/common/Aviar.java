/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.CosmereModConfig;
import leaf.cosmere.aviar.common.capabilities.AviarSpiritwebSubmodule;
import leaf.cosmere.aviar.common.config.AviarConfigs;
import leaf.cosmere.aviar.common.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Aviar.MODID)
public class Aviar implements IModModule
{
	public static final String MODID = "aviar";

	public static Aviar instance;

	public final Version versionNumber;

	public Aviar()
	{
		Cosmere.addModule(instance = this);
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		AviarConfigs.registerConfigs(ModLoadingContext.get());

		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);
		modBus.addListener(this::imcQueue);

		AviarAttributes.ATTRIBUTES.register(modBus);
		AviarBiomes.BIOMES.register(modBus);
		AviarBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modBus);
		AviarBlocks.BLOCKS.register(modBus);
		AviarEffects.EFFECTS.register(modBus);
		AviarFeatures.CONFIGURED_FEATURES.register(modBus);
		AviarFeatures.PLACED_FEATURES.register(modBus);
		AviarEntityTypes.ENTITY_TYPES.register(modBus);
		AviarItems.ITEMS.register(modBus);
		AviarManifestations.MANIFESTATIONS.register(modBus);
		AviarMenuTypes.MENU_TYPES.register(modBus);
		AviarRecipes.SPECIAL_RECIPES.register(modBus);
		AviarStats.STATS.register(modBus);

		versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
	}

	public static ResourceLocation rl(String path)
	{
		return new ResourceLocation(Aviar.MODID, path);
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		CosmereAPI.logger.info("Cosmere: Aviar module Version {} initializing...", versionNumber);

		event.enqueueWork(() ->
		{
			AviarStats.initStatEntries();
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
		return "Aviar";
	}

	@Override
	public ISpiritwebSubmodule makeSubmodule()
	{
		return new AviarSpiritwebSubmodule();
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