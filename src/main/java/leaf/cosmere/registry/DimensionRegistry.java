/*
 * File created ~ 6 - 9 - 2022 ~ Leaf
 */

package leaf.cosmere.registry;

import leaf.cosmere.utils.helpers.LogHelper;
import leaf.cosmere.utils.helpers.ResourceLocationHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class DimensionRegistry
{
	public static final ResourceKey<Level> SHADESMAR_DIM_KEY =
			ResourceKey.create(
					Registry.DIMENSION_REGISTRY,
					ResourceLocationHelper.prefix("shadesmar_dimension")
			);

	public static final ResourceKey<DimensionType> SHADESMAR_DIM_TYPE =
			ResourceKey.create(
					Registry.DIMENSION_TYPE_REGISTRY,
					ROSHAR_DIM_KEY.registry()
			);

	public static void register()
	{
		LogHelper.info("Registering cosmere dimensions");
	}
}
