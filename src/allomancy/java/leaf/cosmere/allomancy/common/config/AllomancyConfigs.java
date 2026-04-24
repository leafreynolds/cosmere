/*
 * File updated ~ 2026-04-25 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.allomancy.common.config;

import leaf.cosmere.common.config.CosmereConfigHelper;
import net.neoforged.fml.ModContainer;

public class AllomancyConfigs
{
	private AllomancyConfigs()
	{

	}

	public static final AllomancyServerConfig SERVER = new AllomancyServerConfig();
	public static final AllomancyClientConfig CLIENT = new AllomancyClientConfig();

	public static void registerConfigs(ModContainer modContainer)
	{
		CosmereConfigHelper.registerConfig(modContainer, SERVER);
		CosmereConfigHelper.registerConfig(modContainer, CLIENT);
	}

}
