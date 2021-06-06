/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.EffectsHelper;
import leaf.cosmere.registry.EffectsRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AllomancyNicrosil extends AllomancyBase
{
    public AllomancyNicrosil(Metals.MetalType metalType)
    {
        super(metalType);
        MinecraftForge.EVENT_BUS.addListener(this::onLivingHurtEvent);
    }

    //active or not active
    @Override
    public int modeMax(ISpiritweb data)
    {
        return 1;
    }

    @Override
    public int modeMin(ISpiritweb data)
    {
        return 0;
    }

    @Override
    public boolean modeWraps(ISpiritweb data)
    {
        return false;
    }

    //Enhances Allomantic Burn of Target
    @SubscribeEvent
    public void onLivingHurtEvent(LivingHurtEvent event)
    {
        Entity trueSource = event.getSource().getTrueSource();
        if (trueSource instanceof PlayerEntity)
        {
            SpiritwebCapability.get((LivingEntity) trueSource).ifPresent(iSpiritweb ->
            {
                ItemStack itemInHand = iSpiritweb.getLiving().getHeldItemMainhand();

                if (itemInHand.isEmpty())
                {
                    //if manifestation is active and has nicrosil metal to burn
                    if (isActive(iSpiritweb))
                    {
                        //valid set up found.
                        EffectInstance newEffect = EffectsHelper.getNewEffect(
                                EffectsRegistry.ALLOMANCY_BOOST.get(),
                                MathHelper.floor(getAllomanticStrength(iSpiritweb))
                        );

                        //apply to the hit entity
                        event.getEntityLiving().addPotionEffect(newEffect);
                    }
                }
            });
        }
    }
}
