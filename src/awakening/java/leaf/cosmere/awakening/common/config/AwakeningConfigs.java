/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.awakening.common.config;

import leaf.cosmere.common.config.CosmereConfigHelper;
import net.neoforged.fml.ModContainer;

public class AwakeningConfigs
{
	public static final AwakeningServerConfig SERVER = new AwakeningServerConfig();

	public static void registerConfigs(ModContainer modContainer)
	{
		CosmereConfigHelper.registerConfig(modContainer, SERVER);
	}
}
