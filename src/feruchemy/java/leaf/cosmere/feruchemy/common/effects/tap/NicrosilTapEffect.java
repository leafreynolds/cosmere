/*
 * File updated ~ 5 - 8 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyNicrosil;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;


public class NicrosilTapEffect extends FeruchemyEffectBase
{
	public NicrosilTapEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);
	}

	@Override
	public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier)
	{
		super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);

		SpiritwebCapability.get(pLivingEntity).ifPresent(FeruchemyNicrosil::clearNicrosilPowers);
	}
}
