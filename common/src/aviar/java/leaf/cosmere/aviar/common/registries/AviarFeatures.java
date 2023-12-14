/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.common.registries;

import leaf.cosmere.common.registration.impl.ConfiguredFeatureDeferredRegister;
import leaf.cosmere.common.registration.impl.PlacedFeatureDeferredRegister;
import leaf.cosmere.aviar.common.Aviar;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class AviarFeatures
{
	public static final ConfiguredFeatureDeferredRegister CONFIGURED_FEATURES = new ConfiguredFeatureDeferredRegister(Aviar.MODID);
	public static final PlacedFeatureDeferredRegister PLACED_FEATURES = new PlacedFeatureDeferredRegister(Aviar.MODID);


	// The "New Tardis Mod" code says we should register configured versions of the features in FMLCommonSetup
	// since it helps prevent mod incompatibility issues. No need to delete other mod's world gen. Thank you 50!


	//todo move this to a parent class so it can be reused across mods
	//copied from OrePlacements.java, since they're private methods that should really be public
	private static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange)
	{
		return orePlacement(CountPlacement.of(pCount), pHeightRange);
	}

	private static List<PlacementModifier> rareOrePlacement(int pCount, PlacementModifier pHeightRange)
	{
		return orePlacement(RarityFilter.onAverageOnceEvery(pCount), pHeightRange);
	}

	private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_)
	{
		return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
	}

}
