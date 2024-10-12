/*
 * File updated ~ 4 - 9 - 2024 ~ Leaf
 */

package leaf.cosmere.soulforgery.common.registries;

import leaf.cosmere.common.registration.impl.ConfiguredFeatureDeferredRegister;
import leaf.cosmere.common.registration.impl.PlacedFeatureDeferredRegister;
import leaf.cosmere.soulforgery.common.Soulforgery;

public class SoulforgeryFeatures
{
	public static final ConfiguredFeatureDeferredRegister CONFIGURED_FEATURES = new ConfiguredFeatureDeferredRegister(Soulforgery.MODID);
	public static final PlacedFeatureDeferredRegister PLACED_FEATURES = new PlacedFeatureDeferredRegister(Soulforgery.MODID);

}
