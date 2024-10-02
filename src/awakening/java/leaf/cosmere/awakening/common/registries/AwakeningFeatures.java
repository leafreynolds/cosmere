/*
 * File updated ~ 4 - 9 - 2024 ~ Leaf
 */

package leaf.cosmere.awakening.common.registries;

import leaf.cosmere.awakening.common.Awakening;
import leaf.cosmere.common.registration.impl.ConfiguredFeatureDeferredRegister;
import leaf.cosmere.common.registration.impl.PlacedFeatureDeferredRegister;

public class AwakeningFeatures
{
	public static final ConfiguredFeatureDeferredRegister CONFIGURED_FEATURES = new ConfiguredFeatureDeferredRegister(Awakening.MODID);
	public static final PlacedFeatureDeferredRegister PLACED_FEATURES = new PlacedFeatureDeferredRegister(Awakening.MODID);

}
