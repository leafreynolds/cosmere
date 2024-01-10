/*
 * File updated ~ 18 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.registries;

import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.common.registration.impl.CosmereEffectDeferredRegister;
import leaf.cosmere.common.registration.impl.CosmereEffectRegistryObject;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.effects.DehydratedEffect;
import leaf.cosmere.sandmastery.common.effects.OvermasteredEffect;

public class SandmasteryEffects
{
	public static final CosmereEffectDeferredRegister EFFECTS = new CosmereEffectDeferredRegister(Sandmastery.MODID);

	public static final CosmereEffectRegistryObject<CosmereEffect> DEHYDRATED_EFFECT = EFFECTS.register("dehydrated", DehydratedEffect::new);
	public static final CosmereEffectRegistryObject<CosmereEffect> OVERMASTERED_EFFECT = EFFECTS.register("overmastered", OvermasteredEffect::new);
}
