/*
 * File updated ~ 7 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.common.config.AllomancyConfigs;
import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlaySoundEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class AllomancyTin extends AllomancyManifestation
{
	private static final HashMap<Vec3, Integer> soundPosMap = new HashMap<>();

	public AllomancyTin(Metals.MetalType metalType)
	{
		super(metalType);
	}

	public static ArrayList<Vec3> getTinSoundList()
	{
		return new ArrayList<>(soundPosMap.keySet());
	}

	@Override
	public void onModeChange(ISpiritweb data, int lastMode) {
		super.onModeChange(data, lastMode);

		if (data.getLiving().level().isClientSide())
		{
			int mode = getMode(data);

			if (mode <= 0)
			{
				soundPosMap.clear();
			}
		}
	}

	@Override
	public void applyEffectTick(ISpiritweb data)
	{
		final LivingEntity living = data.getLiving();
		//allomantic tin allows you to see through investiture based hindrances
		//I would say that the darkness from a warden definitely counts.
		if (living.hasEffect(MobEffects.DARKNESS))
		{
			living.removeEffect(MobEffects.DARKNESS);
		}

		//todo re-evaluate if this is fun or interesting. We want there to be drawbacks to powers too.
		final int blockLighting = living.getFeetBlockState().getLightEmission();
		if (blockLighting > 14)
		{
			living.addEffect(EffectsHelper.getNewEffect(MobEffects.BLINDNESS, blockLighting - 14));
		}
		else if (blockLighting < 8 && living.hasEffect(MobEffects.BLINDNESS))
		{
			living.removeEffect(MobEffects.BLINDNESS);
		}

		// show particles from sound origins towards player burning tin
        if (living.level().isClientSide() && living instanceof LocalPlayer)
		{
			soundPosMap.entrySet().removeIf(entry -> entry.getValue() <= 0);

			Vec3 playerPos = new Vec3(living.position().x, living.position().y, living.position().z);
			Vec3 lookAngle = living.getLookAngle();
			playerPos = playerPos.add(lookAngle.x, 1.2D, lookAngle.z);

			AbsolutePositionSource playerSource = new AbsolutePositionSource(playerPos);
			for (Vec3 v : soundPosMap.keySet())
			{
				// only make particles for 3 ticks
				if (soundPosMap.get(v) > 18)
				{
					VibrationParticleOption vib = new VibrationParticleOption(playerSource, 10);
					living.level().addParticle(vib, v.x, v.y, v.z, 1D, 1D, 1D);
				}

				soundPosMap.put(v, soundPosMap.get(v)-1);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void onSound(PlaySoundEvent event)
	{
		if (Minecraft.getInstance().level == null)
			return;

		SoundInstance eventSound = event.getSound();
		AllomancyTin tinAllomancy = (AllomancyTin) AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.TIN).get();

		if ((eventSound == null))
		{
			return;
		}

		Player localPlayer = Minecraft.getInstance().player;

		SpiritwebCapability.get(localPlayer).ifPresent(playerSpiritweb ->
		{
			// if is rain and config is not enabled, return
			if (event.getName().equals("weather.rain") && !AllomancyConfigs.CLIENT.canHearRain.get())
				return;

			boolean isBronzeSound = event.getName().equals("block.note_block.basedrum");
			if (tinAllomancy.isActive(playerSpiritweb) && !isBronzeSound)
			{
				//todo make the entity glow or something to the user?
				Vec3 soundPos = new Vec3(event.getSound().getX(), event.getSound().getY(), event.getSound().getZ());
				double distance = localPlayer.position().distanceTo(soundPos);
				int range = tinAllomancy.getRange(playerSpiritweb);

				if (distance < range)
				{
					soundPosMap.put(soundPos, 20);	// timer in ticks
				}
			}
		});
	}

	static class AbsolutePositionSource implements PositionSource
	{
		private Vec3 pos;

		public AbsolutePositionSource(Vec3 pos)
		{
			this.pos = pos;
		}

		@Override
		public Optional<Vec3> getPosition(Level pLevel)
		{
			return Optional.of(pos);
		}

		@Override
		public PositionSourceType<?> getType()
		{
			return PositionSourceType.ENTITY;
		}
	}
}
