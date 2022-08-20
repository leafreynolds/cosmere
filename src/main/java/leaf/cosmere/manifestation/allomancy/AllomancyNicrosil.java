/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.EffectsRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import leaf.cosmere.utils.helpers.EffectsHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AllomancyNicrosil extends AllomancyBase
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
					AllomancyNicrosil alloNicrosil = (AllomancyNicrosil) ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.NICROSIL).get();

					//if manifestation is active and has nicrosil metal to burn
					if (alloNicrosil.isActive(iSpiritweb))
					{
						//valid set up found.
						MobEffectInstance newEffect = EffectsHelper.getNewEffect(
								EffectsRegistry.ALLOMANCY_BOOST.get(),
								Mth.floor(alloNicrosil.getStrength(iSpiritweb,false))
						);

						//apply to the hit entity
						event.getEntity().addEffect(newEffect);
					}
				}
			});
		}
	}
}
