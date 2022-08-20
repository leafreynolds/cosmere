/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.tap;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LootingLevelEvent;


public class ChromiumTapEffect extends FeruchemyEffectBase
{
	public ChromiumTapEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);

		addAttributeModifier(
				Attributes.LUCK,
				"7faaa8a8-fee1-422c-8f85-6794042e8f09",
				1.0D,
				AttributeModifier.Operation.ADDITION);

		MinecraftForge.EVENT_BUS.addListener(this::onLootingLevelEvent);
	}

	public void onLootingLevelEvent(LootingLevelEvent event)
	{
		boolean isRemote = event.getEntity().level.isClientSide;
		boolean entityNotLiving = event.getDamageSource() == null || !(event.getDamageSource().getEntity() instanceof LivingEntity);

		if (isRemote || entityNotLiving)
		{
			return;
		}

		MobEffectInstance effectInstance = ((LivingEntity) event.getDamageSource().getEntity()).getEffect(this);
		if (effectInstance != null && effectInstance.getDuration() > 0)
		{
			event.setLootingLevel(event.getLootingLevel() + effectInstance.getAmplifier());
		}
	}


	@Override
	public void addAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier)
	{
		super.addAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
	}
}
