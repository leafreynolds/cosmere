/*
 * File updated ~ 2026-04-23 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.common;

import leaf.cosmere.api.*;
import leaf.cosmere.common.compat.curios.CuriosCompat;
import leaf.cosmere.common.compat.patchouli.PatchouliCompat;
import leaf.cosmere.common.config.CosmereConfigs;
import leaf.cosmere.common.config.ICosmereConfig;
import leaf.cosmere.common.eventHandlers.ColorHandler;
import leaf.cosmere.common.network.NetworkPacketHandler;
import leaf.cosmere.common.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod(Cosmere.MODID)
public class Cosmere
{

	public static final String MODID = CosmereAPI.COSMERE_MODID;
	public static final Map<String, IModModule> modulesLoaded = new HashMap<>();

	public static Cosmere instance;

	public final Version versionNumber;
	private final NetworkPacketHandler packetHandler;


	public Cosmere(IEventBus modBus, ModContainer modContainer)
	{
		instance = this;

		CosmereConfigs.registerConfigs(modContainer);

		modBus.addListener(this::onCommonSetup);
		modBus.addListener(this::onClientSetup);
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);

		//Set our version number to match the neoforge.mods.toml file, which matches the one in our build.gradle
		versionNumber = new Version(modContainer);


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
		ArgumentTypeRegistry.ARGUMENT_TYPE_INFOS.register(modBus);
		CosmereRecipesRegistry.RECIPE_SERIALIZERS.register(modBus);

		CreativeTabsRegistry.CREATIVE_TABS.register(modBus);
		BiomeRegistry.BIOMES.register(modBus);
		LootModifiersRegistry.LOOT_MODIFIERS.register(modBus);
		FeatureRegistry.FEATURES.register(modBus);
		IntProviderTypesRegistry.INT_PROVIDER_TYPES.register(modBus);
		HeightProviderTypesRegistry.HEIGHT_PROVIDER_TYPES.register(modBus);

		DimensionRegistry.register();

		AdvancementTriggerRegistry.init();

		packetHandler = new NetworkPacketHandler();
		packetHandler.register(modBus);

		// TODO [NeoForge 1.21.1 port]: the old Forge Capability<T> / AttachCapabilitiesEvent system
		//  has been removed. Spiritweb needs to migrate to an AttachmentType<SpiritwebCapability>
		//  registered via a DeferredRegister<AttachmentType<?>> on NeoForgeRegistries.ATTACHMENT_TYPES.
		//  See CapabilitiesHandler.java and src/main/java/leaf/cosmere/common/cap/.

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
		return ResourceLocation.fromNamespaceAndPath(Cosmere.MODID, path);
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
					case "Example":
						maniType = Manifestations.ManifestationTypes.NONE;
						break;
					case "Allomancy":
						maniType = Manifestations.ManifestationTypes.ALLOMANCY;
						break;
					case "Feruchemy":
						maniType = Manifestations.ManifestationTypes.FERUCHEMY;
						break;
					case "Hemalurgy":
						maniType = Manifestations.ManifestationTypes.HEMALURGY;
						break;
					case "Surgebinding":
						maniType = Manifestations.ManifestationTypes.SURGEBINDING;
						break;
					case "Sandmastery":
						maniType = Manifestations.ManifestationTypes.SANDMASTERY;
						break;
					case "Aviar":
						maniType = Manifestations.ManifestationTypes.AVIAR;
						break;
				}

				spiritwebSubmoduleMap.put(maniType, iSpiritwebSubmodule);
			}
		}

		return spiritwebSubmoduleMap;
	}

	private void onCommonSetup(FMLCommonSetupEvent event)
	{
		//Initialization notification
		CosmereAPI.logger.info("Cosmere Version {} initializing...", versionNumber);
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
		for (ICosmereConfig cosmereConfig : List.of(
				CosmereConfigs.CLIENT_CONFIG,
				CosmereConfigs.SERVER_CONFIG,
				CosmereConfigs.WORLD_CONFIG))
		{
			if (cosmereConfig.getConfigSpec() == config.getSpec())
			{
				cosmereConfig.clearCache();
				return;
			}
		}
	}

	private void onClientSetup(FMLClientSetupEvent event)
	{
		event.enqueueWork(ColorHandler::init);
	}
}
