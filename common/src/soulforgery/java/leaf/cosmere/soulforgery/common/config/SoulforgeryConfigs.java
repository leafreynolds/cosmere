/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.soulforgery.common.config;

import leaf.cosmere.common.config.CosmereConfigHelper;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;

public class SoulforgeryConfigs
{
	public static final SoulforgeryServerConfig SERVER = new SoulforgeryServerConfig();

	public static void registerConfigs(ModLoadingContext modLoadingContext)
	{
		ModContainer modContainer = modLoadingContext.getActiveContainer();
		CosmereConfigHelper.registerConfig(modContainer, SERVER);
	}
}
