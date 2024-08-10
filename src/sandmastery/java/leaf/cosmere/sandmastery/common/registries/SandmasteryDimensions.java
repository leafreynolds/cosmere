/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.registries;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.sandmastery.common.Sandmastery;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class SandmasteryDimensions
{
	public static final ResourceKey<Level> DAYSIDE_TALDAIN_DIM_KEY =
			ResourceKey.create(
					Registries.DIMENSION,
					Sandmastery.rl("dayside")
			);

	public static void register()
	{
		CosmereAPI.logger.info("Registering sandmastery dimensions");
	}
}
