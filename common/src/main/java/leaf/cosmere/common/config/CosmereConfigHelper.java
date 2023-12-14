/*
 * File updated ~ 27 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.common.config;

import net.minecraftforge.fml.ModContainer;

public class CosmereConfigHelper
{
	public static void registerConfig(ModContainer modContainer, ICosmereConfig config)
	{
		CosmereModConfig modConfig = new CosmereModConfig(modContainer, config);
		if (config.addToContainer())
		{
			modContainer.addConfig(modConfig);
		}
	}
}