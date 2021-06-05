/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LootingLevelEvent;

//luck
public class ChromiumStoreEffect extends FeruchemyEffectBase
{
    public ChromiumStoreEffect(Metals.MetalType type, EffectType effectType)
    {
        super(type, effectType);

        addAttributesModifier(
                Attributes.LUCK,
                "97c8b98f-fb33-4218-bd32-1ace62d75019",
                -1.0D,
                AttributeModifier.Operation.ADDITION);

        MinecraftForge.EVENT_BUS.addListener(this::onLootingLevelEvent);
    }

    public void onLootingLevelEvent(LootingLevelEvent event)
    {
        if (event.getDamageSource() == null)
            return;
    
        boolean isRemote = event.getEntityLiving().world.isRemote;
        boolean entityNotLiving = !(event.getDamageSource().getTrueSource() instanceof LivingEntity);

        if (isRemote || entityNotLiving)
        {
            return;
        }

        EffectInstance effectInstance = ((LivingEntity) event.getDamageSource().getTrueSource()).getActivePotionEffect(this);
        if (effectInstance != null && effectInstance.getDuration() > 0)
        {
            event.setLootingLevel(event.getLootingLevel() - effectInstance.getAmplifier());
        }
    }


    @Override
    public void applyAttributesModifiersToEntity(LivingEntity entityLivingBaseIn, AttributeModifierManager attributeMapIn, int amplifier)
    {
        super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    }
}
