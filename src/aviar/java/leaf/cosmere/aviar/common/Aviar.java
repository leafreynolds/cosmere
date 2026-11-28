/*
 * File updated ~ 20 - 12 - 2024 ~ Leaf
 */

package leaf.cosmere.aviar.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.aviar.common.capabilities.AviarSpiritwebSubmodule;
import leaf.cosmere.aviar.common.config.AviarConfigs;
import leaf.cosmere.aviar.common.registries.*;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.ICosmereConfig;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;

import java.util.List;

@Mod(Aviar.MODID)
public class Aviar implements IModModule
{
	public static final String MODID = "aviar";

	public static Aviar instance;

	public final Version versionNumber;

	public Aviar(IEventBus modBus, ModContainer modContainer)
	{
		Cosmere.addModule(instance = this);

		AviarConfigs.registerConfigs(modContainer);

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
		AviarCreativeTabs.CREATIVE_TABS.register(modBus);

		versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
	}

	public static ResourceLocation rl(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(Aviar.MODID, path);
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
		for (ICosmereConfig cosmereConfig : List.of(AviarConfigs.SERVER))
		{
			if (cosmereConfig.getConfigSpec() == config.getSpec())
			{
				cosmereConfig.clearCache();
				return;
			}
		}
	}
}