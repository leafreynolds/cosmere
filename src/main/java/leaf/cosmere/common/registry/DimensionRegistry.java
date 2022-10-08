/*
 * File updated ~ 6 - 9 - 2022 ~ Leaf
 */

package leaf.cosmere.common.registry;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.common.Cosmere;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class DimensionRegistry
{
	public static final ResourceKey<Level> SHADESMAR_DIM_KEY =
			ResourceKey.create(
					Registry.DIMENSION_REGISTRY,
					Cosmere.rl("shadesmar")
			);

	public static final ResourceKey<DimensionType> SHADESMAR_DIM_TYPE =
			ResourceKey.create(
					Registry.DIMENSION_TYPE_REGISTRY,
					SHADESMAR_DIM_KEY.registry()
			);

	public static void register()
	{
		CosmereAPI.logger.info("Registering cosmere dimensions");
	}
}
