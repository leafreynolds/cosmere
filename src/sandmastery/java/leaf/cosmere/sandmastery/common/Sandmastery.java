/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.sandmastery.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.ICosmereConfig;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.config.SandmasteryConfigs;
import leaf.cosmere.sandmastery.common.network.SandmasteryPacketHandler;
import leaf.cosmere.sandmastery.common.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mod(Sandmastery.MODID)
public class Sandmastery implements IModModule
{
	public static final String MODID = "sandmastery";
	public static Sandmastery instance;
	public final Version versionNumber;

	private final SandmasteryPacketHandler packetHandler;

	public Sandmastery(IEventBus modBus, ModContainer modContainer)
	{
		Cosmere.addModule(instance = this);

		SandmasteryConfigs.registerConfigs(modContainer);

		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);
		modBus.addListener(this::commonSetup);

		SandmasteryItems.ITEMS.register(modBus);
		SandmasteryBlocks.BLOCKS.register(modBus);
		SandmasteryBlockEntitiesRegistry.BLOCK_ENTITIES.register(modBus);
		SandmasteryAttributes.ATTRIBUTES.register(modBus);
		SandmasteryEffects.EFFECTS.register(modBus);
		SandmasteryEntityTypes.ENTITY_TYPES.register(modBus);
		SandmasteryManifestations.MANIFESTATIONS.register(modBus);
		SandmasteryMenuTypes.MENU_TYPES.register(modBus);
		SandmasteryCreativeTabs.CREATIVE_TABS.register(modBus);

		SandmasteryDimensions.register();

		this.versionNumber = new Version(modContainer);
		this.packetHandler = new SandmasteryPacketHandler();
		this.packetHandler.register(modBus);
	}

	public static ResourceLocation rl(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(Sandmastery.MODID, path);
	}

	@Override
	public Version getVersion()
	{
		return versionNumber;
	}

	public static SandmasteryPacketHandler packetHandler()
	{
		return instance.packetHandler;
	}

	@Override
	public String getName()
	{
		return "Sandmastery";
	}

	@Nullable
	@Override
	public ISpiritwebSubmodule makeSubmodule()
	{
		return new SandmasterySpiritwebSubmodule();
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
		for (ICosmereConfig cosmereConfig : List.of(SandmasteryConfigs.SERVER))
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
		CosmereAPI.logger.info("Cosmere: Sand Mastery module Version {} initializing...", versionNumber);

		event.enqueueWork(() ->
		{
		});
	}
}