/*
 * File updated ~ 7 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.registries;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.effects.AllomancyBoostEffect;
import leaf.cosmere.allomancy.common.effects.CopperCloudEffect;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registration.impl.MobEffectDeferredRegister;
import leaf.cosmere.common.registration.impl.MobEffectRegistryObject;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class AllomancyEffects
{

	public static final MobEffectDeferredRegister EFFECTS = new MobEffectDeferredRegister(Allomancy.MODID);

	public static final MobEffectRegistryObject<CopperCloudEffect> ALLOMANTIC_COPPER = EFFECTS.register(
			"copper_cloud",
			() -> new CopperCloudEffect(Metals.MetalType.COPPER, MobEffectCategory.NEUTRAL));

	public static final MobEffectRegistryObject<MobEffect> ALLOMANCY_BOOST = EFFECTS.register(
			"allomancy_boost",
			() -> new AllomancyBoostEffect(Metals.MetalType.DURALUMIN, MobEffectCategory.NEUTRAL));

}
