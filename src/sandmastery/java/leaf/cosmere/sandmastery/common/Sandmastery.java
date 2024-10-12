/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.CosmereModConfig;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.config.SandmasteryConfigs;
import leaf.cosmere.sandmastery.common.network.SandmasteryPacketHandler;
import leaf.cosmere.sandmastery.common.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Nullable;

@Mod(Sandmastery.MODID)
public class Sandmastery implements IModModule
{
	public static final String MODID = "sandmastery";
	public static Sandmastery instance;
	public final Version versionNumber;

	private final SandmasteryPacketHandler packetHandler;

	public Sandmastery()
	{
		Cosmere.addModule(instance = this);

		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		SandmasteryConfigs.registerConfigs(ModLoadingContext.get());

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

		//Set our version number to match the mods.toml file, which matches the one in our build.gradle
		this.versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
		this.packetHandler = new SandmasteryPacketHandler();
	}

	public static ResourceLocation rl(String path)
	{
		return new ResourceLocation(Sandmastery.MODID, path);
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

	private void commonSetup(FMLCommonSetupEvent event)
	{
		CosmereAPI.logger.info("Cosmere: Sand Mastery module Version {} initializing...", versionNumber);

		event.enqueueWork(() ->
		{
		});

		this.packetHandler.initialize();
	}
}