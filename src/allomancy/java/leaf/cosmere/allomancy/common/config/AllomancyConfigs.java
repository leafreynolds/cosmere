/*
 * File updated ~ 7 - 6 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.config;

import leaf.cosmere.common.config.CosmereConfigHelper;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;

public class AllomancyConfigs
{
	private AllomancyConfigs()
	{

	}

	public static final AllomancyServerConfig SERVER = new AllomancyServerConfig();
	public static final AllomancyClientConfig CLIENT = new AllomancyClientConfig();

	public static void registerConfigs(ModLoadingContext modLoadingContext)
	{
		ModContainer modContainer = modLoadingContext.getActiveContainer();
		CosmereConfigHelper.registerConfig(modContainer, SERVER);
		CosmereConfigHelper.registerConfig(modContainer, CLIENT);
	}

}
