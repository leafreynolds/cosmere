/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.soulforgery.common.config;

import leaf.cosmere.common.config.CosmereConfigHelper;
import net.neoforged.fml.ModContainer;

public class SoulforgeryConfigs
{
	public static final SoulforgeryServerConfig SERVER = new SoulforgeryServerConfig();

	public static void registerConfigs(ModContainer modContainer)
	{
		CosmereConfigHelper.registerConfig(modContainer, SERVER);
	}
}
