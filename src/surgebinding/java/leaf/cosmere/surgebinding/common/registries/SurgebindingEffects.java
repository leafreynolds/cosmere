package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.common.registration.impl.CosmereEffectDeferredRegister;
import leaf.cosmere.common.registration.impl.MobEffectDeferredRegister;
import leaf.cosmere.common.registration.impl.MobEffectRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.effects.DividedEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class SurgebindingEffects
{
	public static final CosmereEffectDeferredRegister EFFECTS = new CosmereEffectDeferredRegister(Surgebinding.MODID);
	public static final MobEffectDeferredRegister MOB_EFFECTS = new MobEffectDeferredRegister(Surgebinding.MODID);

	public static final MobEffectRegistryObject<DividedEffect> DIVIDED = MOB_EFFECTS.register("divided",
			() -> new DividedEffect(MobEffectCategory.HARMFUL, 0));
}
