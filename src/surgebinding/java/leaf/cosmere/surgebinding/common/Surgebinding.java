/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.CosmereModConfig;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.capabilities.world.IRoshar;
import leaf.cosmere.surgebinding.common.config.SurgebindingConfigs;
import leaf.cosmere.surgebinding.common.network.SurgebindingPacketHandler;
import leaf.cosmere.surgebinding.common.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Surgebinding.MODID)
public class Surgebinding implements IModModule
{
	public static final String MODID = "surgebinding";

	public static Surgebinding instance;

	public final Version versionNumber;
	private final SurgebindingPacketHandler packetHandler;

	public Surgebinding()
	{
		Cosmere.addModule(instance = this);

		SurgebindingConfigs.registerConfigs(ModLoadingContext.get());

		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onConfigReload);
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onAddCaps);

		//Registries
		SurgebindingBlocks.BLOCKS.register(modBus);
		SurgebindingItems.ITEMS.register(modBus);
		SurgebindingEntityTypes.ENTITY_TYPES.register(modBus);
		SurgebindingAttributes.ATTRIBUTES.register(modBus);
		SurgebindingManifestations.MANIFESTATIONS.register(modBus);

		SurgebindingBiomes.BIOMES.register(modBus);
		SurgebindingBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modBus);

		SurgebindingFeatures.CONFIGURED_FEATURES.register(modBus);
		SurgebindingFeatures.PLACED_FEATURES.register(modBus);
		SurgebindingCreativeTabs.CREATIVE_TABS.register(modBus);

		SurgebindingArgumentTypes.ARGUMENT_TYPE_INFOS.register(modBus);

		SurgebindingDimensions.register();

		versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
		packetHandler = new SurgebindingPacketHandler();
	}

	public static ResourceLocation rl(String path)
	{
		return new ResourceLocation(Surgebinding.MODID, path);
	}

	@Override
	public String getName()
	{
		return "Surgebinding";
	}

	@Override
	public ISpiritwebSubmodule makeSubmodule()
	{
		return new SurgebindingSpiritwebSubmodule();
	}

	@Override
	public Version getVersion()
	{
		return versionNumber;
	}

	public static SurgebindingPacketHandler packetHandler()
	{
		return instance.packetHandler;
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		CosmereAPI.logger.info("Cosmere: Surgebinding module Version {} initializing...", versionNumber);

		event.enqueueWork(() ->
		{

		});

		packetHandler.initialize();
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


	private void onAddCaps(RegisterCapabilitiesEvent capabilitiesEvent)
	{
		capabilitiesEvent.register(IRoshar.class);
	}
}