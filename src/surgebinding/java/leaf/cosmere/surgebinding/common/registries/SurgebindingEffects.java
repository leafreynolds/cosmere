package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.common.registration.impl.CosmereEffectDeferredRegister;
import leaf.cosmere.common.registration.impl.MobEffectDeferredRegister;
import leaf.cosmere.common.registration.impl.MobEffectRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.effects.DividedEffect;
import leaf.cosmere.surgebinding.common.effects.SurgebindingMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class SurgebindingEffects
{
	public static final CosmereEffectDeferredRegister EFFECTS = new CosmereEffectDeferredRegister(Surgebinding.MODID);
	public static final MobEffectDeferredRegister MOB_EFFECTS = new MobEffectDeferredRegister(Surgebinding.MODID);

	public static final MobEffectRegistryObject<DividedEffect> DIVIDED = MOB_EFFECTS.register("divided",
			()-> new DividedEffect(MobEffectCategory.HARMFUL,0));

	public static final MobEffectRegistryObject<MobEffect> RIGID_DEFENSE = MOB_EFFECTS.register("rigid_defense",
			()-> new SurgebindingMobEffect(MobEffectCategory.BENEFICIAL, 49)
					.addAttributeModifier(Attributes.ARMOR,UUID.randomUUID().toString(),4, AttributeModifier.Operation.ADDITION));





}
