/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.allomancy.common;

import leaf.cosmere.allomancy.common.capabilities.AllomancySpiritwebSubmodule;
import leaf.cosmere.allomancy.common.capabilities.world.IScadrial;
import leaf.cosmere.allomancy.common.config.AllomancyConfigs;
import leaf.cosmere.allomancy.common.network.AllomancyPacketHandler;
import leaf.cosmere.allomancy.common.registries.*;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.CosmereModConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Allomancy.MODID)
public class Allomancy implements IModModule
{
	public static final String MODID = "allomancy";
	public static Allomancy instance;
	public final Version versionNumber;
	private final AllomancyPacketHandler packetHandler;

	public Allomancy()
	{
		Cosmere.addModule(instance = this);
		AllomancyConfigs.registerConfigs(ModLoadingContext.get());

		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);
		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onAddCaps);

		AllomancyItems.ITEMS.register(modBus);
		AllomancyAttributes.ATTRIBUTES.register(modBus);
		AllomancyEffects.EFFECTS.register(modBus);
		AllomancyEffects.MOB_EFFECTS.register(modBus);
		AllomancyEntityTypes.ENTITY_TYPES.register(modBus);
		AllomancyManifestations.MANIFESTATIONS.register(modBus);
		AllomancyMenuTypes.MENU_TYPES.register(modBus);
		AllomancyRecipes.RECIPE_SERIALIZERS.register(modBus);
		AllomancyStats.STATS.register(modBus);
		AllomancyCreativeTabs.CREATIVE_TABS.register(modBus);

		//Set our version number to match the mods.toml file, which matches the one in our build.gradle
		versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
		packetHandler = new AllomancyPacketHandler();
	}

	public static ResourceLocation rl(String path)
	{
		return new ResourceLocation(Allomancy.MODID, path);
	}

	@Override
	public Version getVersion()
	{
		return versionNumber;
	}

	@Override
	public String getName()
	{
		return "Allomancy";
	}

	@Override
	public ISpiritwebSubmodule makeSubmodule()
	{
		return new AllomancySpiritwebSubmodule();
	}

	public static AllomancyPacketHandler packetHandler()
	{
		return instance.packetHandler;
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
			if (cosmereModConfig.getSpec() == AllomancyConfigs.CLIENT.getConfigSpec())
			{
			}
		}
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		CosmereAPI.logger.info("Cosmere: Allomancy module Version {} initializing...", versionNumber);

		event.enqueueWork(() ->
		{
			//AllomancyEntityTypes.PrepareEntityAttributes();
			AllomancyStats.initStatEntries();
		});


		packetHandler.initialize();
	}

	private void onAddCaps(RegisterCapabilitiesEvent capabilitiesEvent)
	{
		capabilitiesEvent.register(IScadrial.class);
	}
}