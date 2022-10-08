/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

//health
public class GoldStoreEffect extends FeruchemyEffectBase
{
	public GoldStoreEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);
		addAttributeModifier(
				Attributes.MAX_HEALTH,
				"2ee9153f-372f-4bd2-b21a-ccf08fecb8fa",
				-4.0D,
				AttributeModifier.Operation.ADDITION);
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
