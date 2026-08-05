/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.allomancy.common;

import leaf.cosmere.allomancy.common.capabilities.AllomancySpiritwebSubmodule;
import leaf.cosmere.allomancy.common.config.AllomancyConfigs;
import leaf.cosmere.allomancy.common.network.AllomancyPacketHandler;
import leaf.cosmere.allomancy.common.registries.*;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.ICosmereConfig;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

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

		IEventBus modBus = ModLoadingContext.get().getActiveContainer().getEventBus();
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);
		modBus.addListener(this::commonSetup);

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
		AllomancyAttachments.ATTACHMENTS.register(modBus);

		//Set our version number to match the neoforge.mods.toml file, which matches the one in our build.gradle
		versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
		packetHandler = new AllomancyPacketHandler();
		packetHandler.register(modBus);
	}

	public static ResourceLocation rl(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(Allomancy.MODID, path);
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
				AllomancyConfigs.CLIENT,
				AllomancyConfigs.SERVER))
		{
			if (cosmereConfig.getConfigSpec() == config.getSpec())
			{
				cosmereConfig.clearCache();
				return;
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
	}
}
