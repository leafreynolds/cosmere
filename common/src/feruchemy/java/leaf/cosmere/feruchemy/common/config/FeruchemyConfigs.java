/*
 * File updated ~ 28 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.config;

import leaf.cosmere.common.config.CosmereConfigHelper;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;

public class FeruchemyConfigs
{
	public static final FeruchemyServerConfig SERVER = new FeruchemyServerConfig();

	public static void registerConfigs(ModLoadingContext modLoadingContext)
	{
		ModContainer modContainer = modLoadingContext.getActiveContainer();
		CosmereConfigHelper.registerConfig(modContainer, SERVER);
	}

}
