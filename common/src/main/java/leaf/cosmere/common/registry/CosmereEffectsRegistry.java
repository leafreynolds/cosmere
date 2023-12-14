/*
 * File updated ~ 29 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.common.registry;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registration.impl.CosmereEffectDeferredRegister;

public class CosmereEffectsRegistry
{
	public static final CosmereEffectDeferredRegister EFFECTS = new CosmereEffectDeferredRegister(Cosmere.MODID);
	//Cosmere library mod registers the cosmere effect registry that all sub mods can then add their effects to
}
