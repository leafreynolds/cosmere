/*
 * File updated ~ 22 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.registries;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.effects.AllomancyBoostEffect;
import leaf.cosmere.allomancy.common.effects.AllomancyEffectBase;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registration.impl.MobEffectDeferredRegister;
import leaf.cosmere.common.registration.impl.MobEffectRegistryObject;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class AllomancyEffects
{

	public static final MobEffectDeferredRegister EFFECTS = new MobEffectDeferredRegister(Allomancy.MODID);

	public static final MobEffectRegistryObject<AllomancyEffectBase> ALLOMANTIC_COPPER = EFFECTS.register(
			"copper_cloud",
			() -> new AllomancyEffectBase(Metals.MetalType.COPPER, MobEffectCategory.NEUTRAL));

	public static final MobEffectRegistryObject<MobEffect> ALLOMANCY_BOOST = EFFECTS.register(
			"allomancy_boost",
			() -> new AllomancyBoostEffect(Metals.MetalType.DURALUMIN, MobEffectCategory.NEUTRAL));

}
