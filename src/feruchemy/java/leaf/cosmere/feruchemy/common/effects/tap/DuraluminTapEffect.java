/*
 * File updated ~ 9 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;


public class DuraluminTapEffect extends FeruchemyEffectBase
{
	public DuraluminTapEffect(Metals.MetalType type)
	{
		super(type);

		addAttributeModifier(
				AttributesRegistry.CONNECTION.getAttribute(),
				1.0D,
				AttributeModifier.Operation.ADDITION);
	}

	@Override
	public void applyEffectTick(ISpiritweb data, double strength)
	{
		final LivingEntity living = data.getLiving();
		//ensure the user has correct buffs at least as strong as their store effect
		if (living.level().isClientSide)
		{
			return;
		}

		living.addEffect(EffectsHelper.getNewEffect(MobEffects.HERO_OF_THE_VILLAGE, (int) strength));
	}
}
