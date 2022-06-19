/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.tap;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import leaf.cosmere.manifestation.feruchemy.FeruchemyNicrosil;
import leaf.cosmere.registry.AttributesRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;


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

		SpiritwebCapability.get(pLivingEntity).ifPresent(data->{
			final FeruchemyNicrosil manifestation = (FeruchemyNicrosil) ManifestationRegistry.FERUCHEMY_POWERS.get(metalType).get();
			manifestation.clearNicrosilPowers(data);
		});
	}
}
