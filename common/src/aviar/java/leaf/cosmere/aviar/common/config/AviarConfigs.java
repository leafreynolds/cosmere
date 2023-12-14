/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.common.config;

import leaf.cosmere.common.config.CosmereConfigHelper;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;

public class AviarConfigs
{
	public static final AviarServerConfig SERVER = new AviarServerConfig();

	public static void registerConfigs(ModLoadingContext modLoadingContext)
	{
		ModContainer modContainer = modLoadingContext.getActiveContainer();
		CosmereConfigHelper.registerConfig(modContainer, SERVER);
	}
}
