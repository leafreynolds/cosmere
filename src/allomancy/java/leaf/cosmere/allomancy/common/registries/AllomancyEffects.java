/*
 * File updated ~ 27 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.registries;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.effects.AllomancyBoostEffect;
import leaf.cosmere.allomancy.common.effects.BrassStunEffect;
import leaf.cosmere.allomancy.common.effects.CopperCloudEffect;
import leaf.cosmere.allomancy.common.effects.PewterBurnEffect;
import leaf.cosmere.common.registration.impl.CosmereEffectDeferredRegister;
import leaf.cosmere.common.registration.impl.CosmereEffectRegistryObject;
import leaf.cosmere.common.registration.impl.MobEffectDeferredRegister;
import leaf.cosmere.common.registration.impl.MobEffectRegistryObject;
import net.minecraft.world.effect.MobEffectCategory;

public class AllomancyEffects
{

	public static final CosmereEffectDeferredRegister EFFECTS = new CosmereEffectDeferredRegister(Allomancy.MODID);
	public static final MobEffectDeferredRegister MOB_EFFECTS = new MobEffectDeferredRegister(Allomancy.MODID);

	public static final CosmereEffectRegistryObject<CopperCloudEffect> ALLOMANTIC_COPPER = EFFECTS.register(
			"copper_cloud",
			CopperCloudEffect::new);

	public static final CosmereEffectRegistryObject<AllomancyBoostEffect> ALLOMANCY_BOOST = EFFECTS.register(
			"allomancy_boost",
			AllomancyBoostEffect::new);

	public static final CosmereEffectRegistryObject<PewterBurnEffect> ALLOMANTIC_PEWTER = EFFECTS.register(
			"allomantic_pewter",
			PewterBurnEffect::new);

	public static final MobEffectRegistryObject<BrassStunEffect> ALLOMANTIC_BRASS_STUN = MOB_EFFECTS.register(
			"allomantic_brass_stun",
			() -> new BrassStunEffect(MobEffectCategory.HARMFUL, 0));

}
