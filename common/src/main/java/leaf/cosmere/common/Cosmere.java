/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.common;

import leaf.cosmere.api.*;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.commands.CosmereCommand;
import leaf.cosmere.common.compat.curios.CuriosCompat;
import leaf.cosmere.common.compat.patchouli.PatchouliCompat;
import leaf.cosmere.common.config.CosmereConfigs;
import leaf.cosmere.common.config.CosmereModConfig;
import leaf.cosmere.common.eventHandlers.ColorHandler;
import leaf.cosmere.common.network.NetworkPacketHandler;
import leaf.cosmere.common.registry.*;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class Cosmere
{

	public static final String MODID = CosmereAPI.COSMERE_MODID;
	public static final Map<String, IModModule> modulesLoaded = new HashMap<>();

	public static Cosmere instance;
	private final NetworkPacketHandler packetHandler;


	public Cosmere()
	{
		instance = this;
		DimensionRegistry.register();
		AdvancementTriggerRegistry.init();
		packetHandler = new NetworkPacketHandler();
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
		return new ResourceLocation(Cosmere.MODID, path);
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
}
