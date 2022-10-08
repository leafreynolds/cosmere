/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

//health
public class AtiumTapEffect extends FeruchemyEffectBase
{
	public AtiumTapEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);
		final String atiumStoreAttributeUUID = "69225c21-d36f-4ca3-8071-6b15279ca4f9";

		//atium attribute, size
		addAttributeModifier(
				AttributesRegistry.SIZE_ATTRIBUTE.get(),
				atiumStoreAttributeUUID,
				0.15D,
				AttributeModifier.Operation.ADDITION);

		//reduce related attributes appropriately
		addAttributeModifier(
				Attributes.MOVEMENT_SPEED,
				atiumStoreAttributeUUID,
				0.1D,
				AttributeModifier.Operation.MULTIPLY_TOTAL);
		addAttributeModifier(
				Attributes.MAX_HEALTH,
				atiumStoreAttributeUUID,
				0.1D,
				AttributeModifier.Operation.MULTIPLY_TOTAL);
		addAttributeModifier(
				Attributes.KNOCKBACK_RESISTANCE,
				atiumStoreAttributeUUID,
				0.1D,
				AttributeModifier.Operation.MULTIPLY_TOTAL);
		addAttributeModifier(
				Attributes.ATTACK_DAMAGE,
				atiumStoreAttributeUUID,
				0.1D,
				AttributeModifier.Operation.MULTIPLY_TOTAL);
		addAttributeModifier(
				Attributes.ATTACK_KNOCKBACK,
				atiumStoreAttributeUUID,
				0.1D,
				AttributeModifier.Operation.MULTIPLY_TOTAL);
	}


	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier)
	{
		super.applyEffectTick(entityLivingBaseIn, amplifier);

		if (entityLivingBaseIn.getHealth() > entityLivingBaseIn.getMaxHealth())
		{
			entityLivingBaseIn.setHealth(entityLivingBaseIn.getMaxHealth());
		}
	}
}
