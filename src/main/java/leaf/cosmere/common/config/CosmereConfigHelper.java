/*
 * File updated ~ 27 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.common.config;

import leaf.cosmere.common.Cosmere;
import net.neoforged.fml.ModContainer;

public class CosmereConfigHelper
{
	public static void registerConfig(ModContainer modContainer, ICosmereConfig config)
	{
		if (config.addToContainer())
		{
			modContainer.registerConfig(
					config.getConfigType(),
					config.getConfigSpec(),
					Cosmere.MODID + "/" + config.getFileName() + ".toml");
		}
	}
}
