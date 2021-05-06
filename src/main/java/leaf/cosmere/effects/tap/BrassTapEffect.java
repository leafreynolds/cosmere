/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.tap;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.FeruchemyEffectBase;
import leaf.cosmere.registry.EffectsRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

//https://coppermind.net/wiki/Brass#Feruchemical_Use

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BrassTapEffect extends FeruchemyEffectBase
{
    public BrassTapEffect(Metals.MetalType type, EffectType effectType)
    {
        super(type, effectType);

        //reduce frost damage? theres no cold damage in base minecraft is there?

        //add fire damage?
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
    {
        if (!entityLivingBaseIn.world.isRemote && amplifier > 10 && !entityLivingBaseIn.isInWater())
        {
            //set user on fire
            entityLivingBaseIn.setFire(3);
        }

    }


    @Override
    public boolean isReady(int duration, int amplifier)
    {
        //assume we can apply the effect regardless
        boolean result = true;

        //but if we are a specific effect
        if (metalType == Metals.MetalType.BENDALLOY)
        {
            int k = 100 >> amplifier;
            if (k > 0)
            {
                result = duration % k == 0;
            }
        }
        return result;
    }


    @SubscribeEvent
    public static void onLivingDamageEvent(LivingDamageEvent event)
    {
        if (event.getSource().getTrueSource() instanceof LivingEntity)
        {
            LivingEntity livingEntity = (LivingEntity) event.getSource().getTrueSource();
            EffectInstance effectInstance = livingEntity.getActivePotionEffect(EffectsRegistry.TAPPING_EFFECTS.get(Metals.MetalType.BRASS).get());

            if (effectInstance != null && effectInstance.getDuration() > 0 && effectInstance.getAmplifier() > 3)
            {
                event.getEntityLiving().setFire(effectInstance.getAmplifier());
            }
        }
    }
}
