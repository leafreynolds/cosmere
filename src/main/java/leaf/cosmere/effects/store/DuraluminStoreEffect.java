/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.FeruchemyEffectBase;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

//connection aka ability for people to notice you
public class DuraluminStoreEffect extends FeruchemyEffectBase
{
    public DuraluminStoreEffect(Metals.MetalType type, EffectType effectType)
    {
        super(type, effectType);
        MinecraftForge.EVENT_BUS.addListener(this::onLivingVisibilityEvent);
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        return true;
    }

    public void onLivingVisibilityEvent(LivingEvent.LivingVisibilityEvent event)
    {
        EffectInstance effectInstance = event.getEntityLiving().getActivePotionEffect(this);
        if (effectInstance != null && effectInstance.getDuration() > 0)
        {
            //wearing no armor, you could stand a block or two away from a creeper and it wont see you.
            //walk right into it though, and it will blow up.
            event.modifyVisibility(0.1);
        }
    }
}
