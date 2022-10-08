/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;


public class IronStoreEffect extends FeruchemyEffectBase
{
	public IronStoreEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);
		addAttributeModifier(
				Attributes.KNOCKBACK_RESISTANCE,
				"a8fade1f-573d-405d-9885-39da3906d5f6",
				-0.3D,
				AttributeModifier.Operation.ADDITION);
/*		addAttributeModifier(
				ForgeMod.ENTITY_GRAVITY.get(),
				"89499e2b-8797-4473-89c4-541aa703f17f",
				-0.01D,
				AttributeModifier.Operation.ADDITION);*/
	}

	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier)
	{
		//ensure the user has correct buffs at least as strong as their store effect
		if (entityLivingBaseIn.level.isClientSide || entityLivingBaseIn.tickCount % 20 != 0)
		{
			return;
		}
		entityLivingBaseIn.addEffect(EffectsHelper.getNewEffect(MobEffects.SLOW_FALLING, amplifier));
		entityLivingBaseIn.addEffect(EffectsHelper.getNewEffect(MobEffects.JUMP, amplifier));
	}
}
