package leaf.cosmere.forge;

import dev.architectury.platform.forge.EventBuses;
import leaf.cosmere.common;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Cosmere)
public class CosmereForge
{
	public CosmereForge()
	{
		instance = this;

		CosmereConfigs.registerConfigs(ModLoadingContext.get());

		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::commonSetup);
		modBus.addListener(this::loadComplete);
		modBus.addListener(this::onAddCaps);
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);

		//Set our version number to match the mods.toml file, which matches the one in our build.gradle
		versionNumber = new Version(ModLoadingContext.get().getActiveContainer());


		//Register our deferred registries
		ItemsRegistry.ITEMS.register(modBus);
		BlocksRegistry.BLOCKS.register(modBus);
		EntityTypeRegistry.ENTITY_TYPES.register(modBus);
		AttributesRegistry.ATTRIBUTES.register(modBus);
		ManifestationRegistry.MANIFESTATIONS.createAndRegisterManifestation(modBus);
		CosmereEffectsRegistry.EFFECTS.createAndRegister(modBus);
		PoiTypesRegistry.POINT_OF_INTERESTS.register(modBus);
		VillagerProfessionRegistry.VILLAGE_PROFESSIONS.register(modBus);
		BiomeModifierRegistry.BIOME_MODIFIER_SERIALIZERS.register(modBus);
		LootFunctionRegistry.LOOT_FUNCTIONS.register(modBus);
		GameEventRegistry.GAME_EVENTS.register(modBus);

		BiomeRegistry.BIOMES.register(modBus);
		FeatureRegistry.CONFIGURED_FEATURES.register(modBus);
		FeatureRegistry.PLACED_FEATURES.register(modBus);

		DimensionRegistry.register();

		AdvancementTriggerRegistry.init();

		packetHandler = new NetworkPacketHandler();

		// init cross mod compatibility stuff, if relevant
		CuriosCompat.init();
		PatchouliCompat.init();
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		//Initialization notification
		CosmereAPI.logger.info("Cosmere Version {} initializing...", versionNumber);

		event.enqueueWork(() ->
		{
			CosmereCommand.registerCustomArgumentTypes();
			//StatsRegistry.register();
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
		capabilitiesEvent.register(SpiritwebCapability.class);
	}

	private void loadComplete(FMLLoadCompleteEvent event)
	{
		event.enqueueWork(() ->
		{
			ColorHandler.init();

		});
	}
}