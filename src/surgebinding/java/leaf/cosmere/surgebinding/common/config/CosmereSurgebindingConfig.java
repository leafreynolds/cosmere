/*
 * File updated ~ 7 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.config;

import leaf.cosmere.common.config.CosmereConfigHelper;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;

public class CosmereSurgebindingConfig
{
	private CosmereSurgebindingConfig()
	{

	}

	public static final SurgebindingConfig SERVER = new SurgebindingConfig();

	public static void registerConfigs(ModLoadingContext modLoadingContext)
	{
		ModContainer modContainer = modLoadingContext.getActiveContainer();
		CosmereConfigHelper.registerConfig(modContainer, SERVER);
	}

}
