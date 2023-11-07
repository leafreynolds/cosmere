/*
 * File updated ~ 29 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

//health
public class AtiumStoreEffect extends FeruchemyEffectBase
{
	public AtiumStoreEffect(Metals.MetalType type)
	{
		super(type);

		//atium attribute, size
		addAttributeModifier(
				AttributesRegistry.SIZE_ATTRIBUTE.get(),
				-0.15D,
				AttributeModifier.Operation.ADDITION);

		//reduce related attributes appropriately
		addAttributeModifier(
				Attributes.MOVEMENT_SPEED,
				-0.1D,
				AttributeModifier.Operation.MULTIPLY_TOTAL);
		addAttributeModifier(
				Attributes.MAX_HEALTH,
				-0.1D,
				AttributeModifier.Operation.MULTIPLY_TOTAL);
		addAttributeModifier(
				Attributes.KNOCKBACK_RESISTANCE,
				-0.1D,
				AttributeModifier.Operation.MULTIPLY_TOTAL);
		addAttributeModifier(
				Attributes.ATTACK_DAMAGE,
				-0.1D,
				AttributeModifier.Operation.MULTIPLY_TOTAL);
		addAttributeModifier(
				Attributes.ATTACK_KNOCKBACK,
				-0.1D,
				AttributeModifier.Operation.MULTIPLY_TOTAL);
	}


	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, double strength)
	{
		super.applyEffectTick(entityLivingBaseIn, strength);

		if (entityLivingBaseIn.getHealth() > entityLivingBaseIn.getMaxHealth())
		{
			entityLivingBaseIn.setHealth(entityLivingBaseIn.getMaxHealth());
		}
	}

}
