package leaf.cosmere.surgebinding.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.ICosmereConfig;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.capabilities.world.SurgebindingAttachments;
import leaf.cosmere.surgebinding.common.config.SurgebindingConfigs;
import leaf.cosmere.surgebinding.common.network.SurgebindingPacketHandler;
import leaf.cosmere.surgebinding.common.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

@Mod(Surgebinding.MODID)
public class Surgebinding implements IModModule
{
	public static final String MODID = "surgebinding";

	public static Surgebinding instance;

	public final Version versionNumber;
	private final SurgebindingPacketHandler packetHandler;

	public Surgebinding(IEventBus modBus, ModContainer modContainer)
	{
		Cosmere.addModule(instance = this);

		SurgebindingConfigs.registerConfigs(modContainer);

		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onConfigReload);
		modBus.addListener(this::onConfigLoad);

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
		SurgebindingAttachments.ATTACHMENT_TYPES.register(modBus);

		SurgebindingDimensions.register();

		versionNumber = new Version(modContainer);
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
