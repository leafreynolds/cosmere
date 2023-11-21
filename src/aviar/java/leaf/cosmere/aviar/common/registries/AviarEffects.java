/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.common.registries;

import leaf.cosmere.aviar.common.Aviar;
import leaf.cosmere.aviar.common.effects.AviarBondEffect;
import leaf.cosmere.common.registration.impl.CosmereEffectDeferredRegister;
import leaf.cosmere.common.registration.impl.CosmereEffectRegistryObject;

public class AviarEffects
{

	public static final CosmereEffectDeferredRegister EFFECTS = new CosmereEffectDeferredRegister(Aviar.MODID);

	public static final CosmereEffectRegistryObject<AviarBondEffect> AVIAR_BOND_EFFECT = EFFECTS.register(
			"aviar_bond_effect",
			AviarBondEffect::new);

}
