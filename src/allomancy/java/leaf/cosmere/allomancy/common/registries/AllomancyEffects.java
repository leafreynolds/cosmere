/*
 * File updated ~ 27 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.registries;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.effects.AllomancyBoostEffect;
import leaf.cosmere.allomancy.common.effects.CopperCloudEffect;
import leaf.cosmere.allomancy.common.effects.PewterBurnEffect;
import leaf.cosmere.common.registration.impl.CosmereEffectDeferredRegister;
import leaf.cosmere.common.registration.impl.CosmereEffectRegistryObject;

public class AllomancyEffects
{

	public static final CosmereEffectDeferredRegister EFFECTS = new CosmereEffectDeferredRegister(Allomancy.MODID);

	public static final CosmereEffectRegistryObject<CopperCloudEffect> ALLOMANTIC_COPPER = EFFECTS.register(
			"copper_cloud",
			CopperCloudEffect::new);

	public static final CosmereEffectRegistryObject<AllomancyBoostEffect> ALLOMANCY_BOOST = EFFECTS.register(
			"allomancy_boost",
			AllomancyBoostEffect::new);

	public static final CosmereEffectRegistryObject<PewterBurnEffect> ALLOMANTIC_PEWTER = EFFECTS.register(
			"allomantic_pewter",
			PewterBurnEffect::new);

}
