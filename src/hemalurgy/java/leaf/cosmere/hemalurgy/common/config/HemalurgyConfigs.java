/*
 * File updated ~ 28 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.config;

import leaf.cosmere.common.config.CosmereConfigHelper;
import net.neoforged.fml.ModContainer;

public class HemalurgyConfigs
{
	public static final HemalurgyServerConfig SERVER = new HemalurgyServerConfig();

	public static void registerConfigs(ModContainer modContainer)
	{
		CosmereConfigHelper.registerConfig(modContainer, SERVER);
	}

}
