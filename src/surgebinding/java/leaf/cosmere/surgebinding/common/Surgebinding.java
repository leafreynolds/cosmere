
/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.ICosmereConfig;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.capabilities.world.IRoshar;
import leaf.cosmere.surgebinding.common.config.SurgebindingConfigs;
import leaf.cosmere.surgebinding.common.network.SurgebindingPacketHandler;
import leaf.cosmere.surgebinding.common.registries.*;
import leaf.cosmere.surgebinding.common.worldgen.SurgebindingConfiguredFeatures;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;
import net.neoforged.fml.ModLoadingContext;
import top.theillusivec4.curios.api.CuriosApi;

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

		IEventBus modBus = ModLoadingContext.get().getActiveContainer().getEventBus();
		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onConfigReload);
		modBus.addListener(this::onConfigLoad);

		//Registries
		SurgebindingAttachments.ATTACHMENTS.register(modBus);
		SurgebindingDataComponents.DATA_COMPONENTS.register(modBus);
		SurgebindingBlocks.BLOCKS.register(modBus);
		SurgebindingItems.ITEMS.register(modBus);
		SurgebindingEntityTypes.ENTITY_TYPES.register(modBus);
		SurgebindingAttributes.ATTRIBUTES.register(modBus);
		SurgebindingManifestations.MANIFESTATIONS.register(modBus);
		SurgebindingEffects.EFFECTS.register(modBus);
		SurgebindingEffects.MOB_EFFECTS.register(modBus);
		SurgebindingRecipes.RECIPE_SERIALIZERS.register(modBus);
		SurgebindingLootFunctions.LOOT_FUNCTIONS.register(modBus);

		SurgebindingBiomes.BIOMES.register(modBus);

		SurgebindingCreativeTabs.CREATIVE_TABS.register(modBus);

		SurgebindingArgumentTypes.ARGUMENT_TYPE_INFOS.register(modBus);

		SurgebindingDimensions.register();

		versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
		packetHandler = new SurgebindingPacketHandler();
		packetHandler.register(modBus);


	}

	public static ResourceLocation rl(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(Surgebinding.MODID, path);
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
	}

	private void onConfigLoad(ModConfigEvent configEvent)
	{
		ModConfig config = configEvent.getConfig();
		if (!config.getModId().equals(MODID))
		{
			return;
		}
		for (ICosmereConfig cosmereConfig : List.of(SurgebindingConfigs.SERVER))
		{
			if (cosmereConfig.getConfigSpec() == config.getSpec())
			{
				cosmereConfig.clearCache();
				return;
			}
		}
	}

	private void onConfigReload(ModConfigEvent.Reloading configEvent)
	{
		ModConfig config = configEvent.getConfig();
		if (!config.getModId().equals(MODID))
		{
			return;
		}
		for (ICosmereConfig cosmereConfig : List.of(SurgebindingConfigs.SERVER))
		{
			if (cosmereConfig.getConfigSpec() == config.getSpec())
			{
				cosmereConfig.clearCache();
				return;
			}
		}
	}


}
