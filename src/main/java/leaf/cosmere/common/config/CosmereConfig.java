/*
 * File updated ~ 6 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.common.config;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;

public class CosmereConfig
{
	private CosmereConfig()
	{

	}

	public static final ClientConfig client = new ClientConfig();
	public static final CommonConfig common = new CommonConfig();

	public static void registerConfigs(ModLoadingContext modLoadingContext)
	{
		ModContainer modContainer = modLoadingContext.getActiveContainer();
		CosmereConfigHelper.registerConfig(modContainer, client);
		CosmereConfigHelper.registerConfig(modContainer, common);
	}

}
