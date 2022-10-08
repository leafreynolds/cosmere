/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.common.registries.AllomancyEffects;
import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class AllomancyNicrosil extends AllomancyManifestation
{
	public AllomancyNicrosil(Metals.MetalType metalType)
	{
		super(metalType);
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
	public static void onLivingHurtEvent(LivingHurtEvent event)
	{
		Entity trueSource = event.getSource().getEntity();
		if (trueSource instanceof Player)
		{
			SpiritwebCapability.get((LivingEntity) trueSource).ifPresent(iSpiritweb ->
			{
				ItemStack itemInHand = iSpiritweb.getLiving().getMainHandItem();

				if (itemInHand.isEmpty())
				{
					AllomancyNicrosil alloNicrosil = (AllomancyNicrosil) AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.NICROSIL).get();

					//if manifestation is active and has nicrosil metal to burn
					if (alloNicrosil.isActive(iSpiritweb))
					{
						//valid set up found.
						MobEffectInstance newEffect = EffectsHelper.getNewEffect(
								AllomancyEffects.ALLOMANCY_BOOST.get(),
								Mth.floor(alloNicrosil.getStrength(iSpiritweb, false))
						);

						//apply to the hit entity
						event.getEntity().addEffect(newEffect);
					}
				}
			});
		}
	}
}
