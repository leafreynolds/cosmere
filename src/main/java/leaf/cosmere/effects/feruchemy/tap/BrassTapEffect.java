/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.tap;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import leaf.cosmere.registry.EffectsRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

//https://coppermind.net/wiki/Brass#Feruchemical_Use

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BrassTapEffect extends FeruchemyEffectBase
{
	public BrassTapEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);

		//reduce frost damage? theres no cold damage in base minecraft is there?

		//add fire damage?
	}

	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier)
	{
		if (!isActiveTick(entityLivingBaseIn))
		{
			return;
		}

		if (!entityLivingBaseIn.level.isClientSide && amplifier >= 5 && !entityLivingBaseIn.isInWater())
		{
			//set user on fire
			entityLivingBaseIn.setSecondsOnFire(3);
		}

	}

	@SubscribeEvent
	public static void onLivingDamageEvent(LivingDamageEvent event)
	{
		if (event.getSource().getEntity() instanceof LivingEntity livingEntity)
		{
			MobEffectInstance effectInstance = livingEntity.getEffect(EffectsRegistry.TAPPING_EFFECTS.get(Metals.MetalType.BRASS).get());

			if (effectInstance != null && effectInstance.getDuration() > 0 && effectInstance.getAmplifier() > 3)
			{
				event.getEntityLiving().setSecondsOnFire(effectInstance.getAmplifier());
			}
		}
	}
}
