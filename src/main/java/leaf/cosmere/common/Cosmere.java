/*
 * File updated ~ 27 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.common;

import leaf.cosmere.api.*;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.commands.CosmereCommand;
import leaf.cosmere.common.compat.curios.CuriosCompat;
import leaf.cosmere.common.compat.patchouli.PatchouliCompat;
import leaf.cosmere.common.config.CosmereConfigs;
import leaf.cosmere.common.config.CosmereModConfig;
import leaf.cosmere.common.eventHandlers.ColorHandler;
import leaf.cosmere.common.network.NetworkPacketHandler;
import leaf.cosmere.common.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.HashMap;
import java.util.Map;

@Mod(Cosmere.MODID)
public class Cosmere
{

	public static final String MODID = CosmereAPI.COSMERE_MODID;
	public static final Map<String, IModModule> modulesLoaded = new HashMap<>();

	public static Cosmere instance;

	public final Version versionNumber;
	private final NetworkPacketHandler packetHandler;


	public Cosmere()
	{
		instance = this;

		CosmereConfigs.registerConfigs(ModLoadingContext.get());

		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::commonSetup);
		modBus.addListener(this::loadComplete);
		modBus.addListener(this::onAddCaps);
		modBus.addListener(this::onConfigLoad);

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

	public static synchronized void addModule(IModModule modModule)
	{
		modulesLoaded.put(modModule.getName(), modModule);
	}

	public static boolean isModuleLoaded(String moduleName)
	{
		return modulesLoaded.containsKey(moduleName);
	}

	public static NetworkPacketHandler packetHandler()
	{
		return instance.packetHandler;
	}

	public static ResourceLocation rl(String path)
	{
		return new ResourceLocation(Cosmere.MODID, path);
	}

	public static Map<Manifestations.ManifestationTypes, ISpiritwebSubmodule> makeSpiritwebSubmodules()
	{
		Map<Manifestations.ManifestationTypes, ISpiritwebSubmodule> spiritwebSubmoduleMap = new HashMap<>();

		for (IModModule iModModule : modulesLoaded.values())
		{
			ISpiritwebSubmodule iSpiritwebSubmodule = iModModule.makeSubmodule();
			if (iSpiritwebSubmodule != null)
			{
				Manifestations.ManifestationTypes maniType = null;
				switch (iModModule.getName())
				{
					case "Allomancy":
						maniType = Manifestations.ManifestationTypes.ALLOMANCY;
						break;
					case "Feruchemy":
						maniType = Manifestations.ManifestationTypes.FERUCHEMY;
						break;
					case "Surgebinding":
						maniType = Manifestations.ManifestationTypes.SURGEBINDING;
						break;
					case "Sandmastery":
						maniType = Manifestations.ManifestationTypes.SANDMASTERY;
						break;
				}

				spiritwebSubmoduleMap.put(maniType, iSpiritwebSubmodule);
			}
		}

		return spiritwebSubmoduleMap;
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
