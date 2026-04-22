/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.common.config;

import net.neoforged.fml.ModContainer;

public class CosmereConfigs
{
	public static final CosmereClientConfig CLIENT_CONFIG = new CosmereClientConfig();
	public static final CosmereServerConfig SERVER_CONFIG = new CosmereServerConfig();
	public static final CosmereWorldConfig WORLD_CONFIG = new CosmereWorldConfig();

	public static void registerConfigs(ModContainer modContainer)
	{
		CosmereConfigHelper.registerConfig(modContainer, CLIENT_CONFIG);
		CosmereConfigHelper.registerConfig(modContainer, SERVER_CONFIG);
		CosmereConfigHelper.registerConfig(modContainer, WORLD_CONFIG);
	}
}
