/*
 * File updated ~ 5 - 3 - 2025 ~ Leaf
 */

package leaf.cosmere.common.registry;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.effects.DrainInvestitureEffect;
import leaf.cosmere.common.registration.impl.CosmereEffectDeferredRegister;
import leaf.cosmere.common.registration.impl.CosmereEffectRegistryObject;

public class CosmereEffectsRegistry
{
	public static final CosmereEffectDeferredRegister EFFECTS = new CosmereEffectDeferredRegister(Cosmere.MODID);
	//Cosmere library mod registers the cosmere effect registry that all sub mods can then add their effects to


	public static final CosmereEffectRegistryObject<DrainInvestitureEffect> DRAIN_INVESTITURE = EFFECTS.register(
			"drain_investiture",
			DrainInvestitureEffect::new);
}
