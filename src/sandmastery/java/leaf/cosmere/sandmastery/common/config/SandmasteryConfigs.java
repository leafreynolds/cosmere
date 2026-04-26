/*
 * File updated ~ 28 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.config;

import leaf.cosmere.common.config.CosmereConfigHelper;
import net.neoforged.fml.ModContainer;

public class SandmasteryConfigs
{
	public static final SandmasteryServerConfig SERVER = new SandmasteryServerConfig();

	public static void registerConfigs(ModContainer modContainer)
	{
		CosmereConfigHelper.registerConfig(modContainer, SERVER);
	}

}
