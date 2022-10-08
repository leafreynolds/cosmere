/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LootingLevelEvent;

//luck
public class ChromiumStoreEffect extends FeruchemyEffectBase
{
	public ChromiumStoreEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);

		addAttributeModifier(
				Attributes.LUCK,
				"97c8b98f-fb33-4218-bd32-1ace62d75019",
				-1.0D,
				AttributeModifier.Operation.ADDITION);

		MinecraftForge.EVENT_BUS.addListener(this::onLootingLevelEvent);
	}

	public void onLootingLevelEvent(LootingLevelEvent event)
	{
		if (event.getDamageSource() == null)
		{
			return;
		}

		boolean isRemote = event.getEntity().level.isClientSide;
		boolean entityNotLiving = !(event.getDamageSource().getEntity() instanceof LivingEntity);

		if (isRemote || entityNotLiving)
		{
			return;
		}

		MobEffectInstance effectInstance = ((LivingEntity) event.getDamageSource().getEntity()).getEffect(this);
		if (effectInstance != null && effectInstance.getDuration() > 0)
		{
			event.setLootingLevel(event.getLootingLevel() - effectInstance.getAmplifier());
		}
	}


	@Override
	public void addAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier)
	{
		super.addAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
	}
}
