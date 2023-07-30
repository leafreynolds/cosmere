/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.registries;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.sandmastery.common.Sandmastery;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class SandmasteryDimensions
{
    public static final ResourceKey<Level> DAYSIDE_TALDAIN_DIM_KEY =
            ResourceKey.create(
                    Registry.DIMENSION_REGISTRY,
                    Sandmastery.rl("dayside")
            );

    public static void register()
    {
        CosmereAPI.logger.info("Registering sandmastery dimensions");
    }
}
