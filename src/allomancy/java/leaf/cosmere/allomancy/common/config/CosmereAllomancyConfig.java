/*
 * File updated ~ 7 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.config;

import leaf.cosmere.common.config.CosmereConfigHelper;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;

public class CosmereAllomancyConfig
{
	private CosmereAllomancyConfig()
	{

	}

	public static final AllomancyConfig SERVER = new AllomancyConfig();

	public static void registerConfigs(ModLoadingContext modLoadingContext)
	{
		ModContainer modContainer = modLoadingContext.getActiveContainer();
		CosmereConfigHelper.registerConfig(modContainer, SERVER);
	}

}
