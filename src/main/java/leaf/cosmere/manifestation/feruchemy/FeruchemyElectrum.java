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

		//take less damage when tapping
		if (tapEffect != null && tapEffect.getDuration() > 0)
		{
			//always reduce damage by something
			final int i = tapEffect.getAmplifier() + 1;

			//never able to reduce by 100%
			// 76% ish max? eg tap10 / 13 = 0.76
			final float v = i / 13f;

			// leaving 24%
			// 1 - 0.76 = 0.24
			final float v1 = 1 - v;

			//eg 7 damage at tap 10 would be:
			// 7 * 0.24 = 1.68 damage remaining
			event.setAmount(event.getAmount() * v1);//todo convert to config
		}

		//take more damage when Storing
		if (storeEffect != null && storeEffect.getDuration() > 0)
		{
			//always increase damage by something
			final int i = storeEffect.getAmplifier() + 1;

			//store 3 is the max so never able to increase damage to self by more than 23%?
			// 23% ish max? eg store3 / 13 = 0.23
			final float v = i / 13f;

			// So we add 76% extra damage
			// 1 + 0.23 = 1.23
			final float v1 = 1 + v;

			//eg 7 damage at store 3 would be:
			// 7 * 1.23 = 8.61
			event.setAmount(event.getAmount() * v1);//todo convert to config
		}

	}

}
