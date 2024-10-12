/*
 * File updated ~ 6 - 9 - 2022 ~ Leaf
 */

package leaf.cosmere.common.registry;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.common.Cosmere;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class DimensionRegistry
{
	public static final ResourceKey<Level> SHADESMAR_DIM_KEY =
			ResourceKey.create(
					Registries.DIMENSION,
					Cosmere.rl("shadesmar")
			);

	public static final ResourceKey<DimensionType> SHADESMAR_DIM_TYPE =
			ResourceKey.create(
					Registries.DIMENSION_TYPE,
					SHADESMAR_DIM_KEY.registry()
			);

	public static void register()
	{
		CosmereAPI.logger.info("Registering cosmere dimensions");
	}
}
