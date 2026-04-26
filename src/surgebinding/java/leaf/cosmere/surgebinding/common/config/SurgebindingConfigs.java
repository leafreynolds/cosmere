/*
 * File updated ~ 27 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.config;

import leaf.cosmere.common.config.CosmereConfigHelper;
import net.neoforged.fml.ModContainer;

public class SurgebindingConfigs
{
	public static final SurgebindingServerConfig SERVER = new SurgebindingServerConfig();

	public static void registerConfigs(ModContainer modContainer)
	{
		CosmereConfigHelper.registerConfig(modContainer, SERVER);
	}

}
