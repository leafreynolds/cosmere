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
import leaf.cosmere.hemalurgy.common.capabilities.world.HemalurgyAttachments;
import leaf.cosmere.hemalurgy.common.config.HemalurgyConfigs;
import leaf.cosmere.hemalurgy.common.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

@Mod(Hemalurgy.MODID)
public class Hemalurgy implements IModModule
{
	public static final String MODID = "hemalurgy";

	public static Hemalurgy instance;

	public final Version versionNumber;

	public Hemalurgy(IEventBus modBus, ModContainer modContainer)
	{
		Cosmere.addModule(instance = this);

		HemalurgyConfigs.registerConfigs(modContainer);

		modBus.addListener(this::commonSetup);
		modBus.addListener(this::onConfigLoad);
		modBus.addListener(this::onConfigReload);

		HemalurgyAttributes.ATTRIBUTES.register(modBus);
		HemalurgyItems.ITEMS.register(modBus);
		HemalurgyLootFunctions.LOOT_FUNCTIONS.register(modBus);
		HemalurgyEntityTypes.ENTITY_TYPES.register(modBus);
		HemalurgyCreativeTabs.CREATIVE_TABS.register(modBus);
		HemalurgyAttachments.ATTACHMENT_TYPES.register(modBus);

		versionNumber = new Version(modContainer);
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

	private void handleConfigEvent(ModConfigEvent event)
	{
		for (ICosmereConfig config : List.of(HemalurgyConfigs.SERVER))
		{
			if (event.getConfig().getSpec() == config.getConfigSpec())
			{
				config.clearCache();
				return;
			}
		}
	}

	private void onConfigLoad(ModConfigEvent.Loading event)
	{
		handleConfigEvent(event);
	}

	private void onConfigReload(ModConfigEvent.Reloading event)
	{
		handleConfigEvent(event);
	}
}
