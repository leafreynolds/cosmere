/*
 * File created ~ 27 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.feruchemy;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.EffectsRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class FeruchemyElectrum extends FeruchemyBase
{
	public FeruchemyElectrum(Metals.MetalType metalType)
	{
		super(metalType);
		MinecraftForge.EVENT_BUS.addListener(this::onLivingHurtEvent);
	}

	public void onLivingHurtEvent(LivingHurtEvent event)
	{
		MobEffectInstance tapEffect = event.getEntityLiving().getEffect(EffectsRegistry.TAPPING_EFFECTS.get(getMetalType()).get());
		MobEffectInstance storeEffect = event.getEntityLiving().getEffect(EffectsRegistry.STORING_EFFECTS.get(getMetalType()).get());

		if (tapEffect != null && tapEffect.getDuration() > 0)
		{
			event.setAmount(event.getAmount() - ((tapEffect.getAmplifier() + 1) * 0.25f));
		}

		if (storeEffect != null && storeEffect.getDuration() > 0)
		{
			event.setAmount(event.getAmount() + ((storeEffect.getAmplifier() + 1) * 0.25f));
		}

	}

}
