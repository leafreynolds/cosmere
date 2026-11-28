/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.ICosmereConfig;
import leaf.cosmere.hemalurgy.common.capabilities.HemalurgySpiritwebSubmodule;
import leaf.cosmere.hemalurgy.common.config.HemalurgyConfigs;
import leaf.cosmere.hemalurgy.common.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

@Mod(Hemalurgy.MODID)
public class Hemalurgy implements IModModule
{
	public static final String MODID = "hemalurgy";

	public static Hemalurgy instance;

	public final Version versionNumber;

	public Hemalurgy()
	{
		Cosmere.addModule(instance = this);
		HemalurgyConfigs.registerConfigs(ModLoadingContext.get());

		IEventBus modBus = ModLoadingContext.get().getActiveContainer().getEventBus();
		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);

		HemalurgyAttributes.ATTRIBUTES.register(modBus);
		HemalurgyItems.ITEMS.register(modBus);
		HemalurgyLootFunctions.LOOT_FUNCTIONS.register(modBus);
		HemalurgyEntityTypes.ENTITY_TYPES.register(modBus);
		HemalurgyCreativeTabs.CREATIVE_TABS.register(modBus);
		HemalurgyAttachments.ATTACHMENTS.register(modBus);
		HemalurgyDataComponents.DATA_COMPONENTS.register(modBus);

		versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
	}

	public static ResourceLocation rl(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(Hemalurgy.MODID, path);
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		CosmereAPI.logger.info("Cosmere: Hemalurgy module Version {} initializing...", versionNumber);
	}

	@Override
	public Version getVersion()
	{
		return versionNumber;
	}

	@Override
	public String getName()
	{
		return "Hemalurgy";
	}

	@Override
	public ISpiritwebSubmodule makeSubmodule()
	{
		return new HemalurgySpiritwebSubmodule();
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
		for (ICosmereConfig cosmereConfig : List.of(HemalurgyConfigs.SERVER))
		{
			if (cosmereConfig.getConfigSpec() == config.getSpec())
			{
				cosmereConfig.clearCache();
				return;
			}
		}
	}
}
