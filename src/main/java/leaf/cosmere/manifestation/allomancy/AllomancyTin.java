/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.Cosmere;
import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.EffectsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AllomancyTin extends AllomancyBase
{
    public AllomancyTin(Metals.MetalType metalType)
    {
        super(metalType);
    }

    @Override
    protected void performEffect(ISpiritweb data)
    {
        //Increases Physical Senses
        LivingEntity livingEntity = data.getLiving();
        boolean isActiveTick = livingEntity.ticksExisted % 20 == 0;

        //give night vision
        if (isActiveTick)
        {
            livingEntity.addPotionEffect(EffectsHelper.getNewEffect(Effects.NIGHT_VISION, 0));
        }
    }


    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onSound(PlaySoundEvent event)
    {
        ISound eventSound = event.getSound();

        if ((eventSound == null))
        {
            return;
        }

        PlayerEntity localPlayer = Minecraft.getInstance().player;

        SpiritwebCapability.get(localPlayer).ifPresent(playerSpiritweb ->
        {
            if (isActive(playerSpiritweb))
            {
                //todo make the entity glow or something to the user?
            }
        });
    }
}
