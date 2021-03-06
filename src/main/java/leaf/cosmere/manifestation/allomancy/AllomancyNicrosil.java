/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.EffectsRegistry;
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
		Entity trueSource = event.getSource().getEntity();
		if (trueSource instanceof Player)
		{
			SpiritwebCapability.get((LivingEntity) trueSource).ifPresent(iSpiritweb ->
			{
				ItemStack itemInHand = iSpiritweb.getLiving().getMainHandItem();

				if (itemInHand.isEmpty())
				{
					//if manifestation is active and has nicrosil metal to burn
					if (isActive(iSpiritweb))
					{
						//valid set up found.
						MobEffectInstance newEffect = EffectsHelper.getNewEffect(
								EffectsRegistry.ALLOMANCY_BOOST.get(),
								Mth.floor(getStrength(iSpiritweb,false))
						);

						//apply to the hit entity
						event.getEntityLiving().addEffect(newEffect);
					}
				}
			});
		}
	}
}
