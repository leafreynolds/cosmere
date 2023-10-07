/*
 * File updated ~ 7 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
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

@Mod.EventBusSubscriber(modid = Allomancy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AllomancyTin extends AllomancyManifestation
{
	public AllomancyTin(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		final LivingEntity living = data.getLiving();
		if (living.hasEffect(MobEffects.DARKNESS))
		{
			living.removeEffect(MobEffects.DARKNESS);
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
