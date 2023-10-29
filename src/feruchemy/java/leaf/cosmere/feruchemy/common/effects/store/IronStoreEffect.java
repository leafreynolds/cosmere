/*
 * File updated ~ 29 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;


public class IronStoreEffect extends FeruchemyEffectBase
{
	public IronStoreEffect(Metals.MetalType type)
	{
		super(type);
		addAttributeModifier(
				Attributes.KNOCKBACK_RESISTANCE,
				-0.3D,
				AttributeModifier.Operation.ADDITION);
/*		addAttributeModifier(
				ForgeMod.ENTITY_GRAVITY.get(),
				"89499e2b-8797-4473-89c4-541aa703f17f",
				-0.01D,
				AttributeModifier.Operation.ADDITION);*/
	}

	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, double strength)
	{
		//ensure the user has correct buffs at least as strong as their store effect
		if (entityLivingBaseIn.level.isClientSide || entityLivingBaseIn.tickCount % 20 != 0)
		{
			return;
		}
		entityLivingBaseIn.addEffect(EffectsHelper.getNewEffect(MobEffects.SLOW_FALLING, (int) strength));
		entityLivingBaseIn.addEffect(EffectsHelper.getNewEffect(MobEffects.JUMP, (int) strength));
	}
}
