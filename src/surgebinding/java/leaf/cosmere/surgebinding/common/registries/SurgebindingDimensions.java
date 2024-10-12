/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class SurgebindingDimensions
{
	public static final ResourceKey<Level> ROSHAR_DIM_KEY =
			ResourceKey.create(
					Registries.DIMENSION,
					Surgebinding.rl("roshar")
			);

	public static final ResourceKey<DimensionType> ROSHAR_DIM_TYPE =
			ResourceKey.create(
					Registries.DIMENSION_TYPE,
					ROSHAR_DIM_KEY.registry()
			);

	public static void register()
	{
		CosmereAPI.logger.info("Registering surgebinding dimensions");
	}
}
