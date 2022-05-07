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
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
		boolean isActiveTick = livingEntity.tickCount % 20 == 0;

		//give night vision
		if (isActiveTick)
		{
			livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.NIGHT_VISION, 0));
		}
	}


	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onSound(PlaySoundEvent event)
	{
		SoundInstance eventSound = event.getSound();

		if ((eventSound == null))
		{
			return;
		}

		Player localPlayer = Minecraft.getInstance().player;

		SpiritwebCapability.get(localPlayer).ifPresent(playerSpiritweb ->
		{
			if (isActive(playerSpiritweb))
			{
				//todo make the entity glow or something to the user?
			}
		});
	}
}
