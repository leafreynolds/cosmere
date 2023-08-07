package leaf.cosmere.sandmastery.common.registries;

import leaf.cosmere.common.registration.impl.MobEffectDeferredRegister;
import leaf.cosmere.common.registration.impl.MobEffectRegistryObject;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.effects.OvermasteryEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class SandmasteryEffects
{
	public static final MobEffectDeferredRegister EFFECTS = new MobEffectDeferredRegister(Sandmastery.MODID);

	public static final MobEffectRegistryObject<MobEffect> OVERMASTERY_EFFECT = EFFECTS.register("overmastery", () -> new OvermasteryEffect(MobEffectCategory.NEUTRAL, 0xC2B280));
}
