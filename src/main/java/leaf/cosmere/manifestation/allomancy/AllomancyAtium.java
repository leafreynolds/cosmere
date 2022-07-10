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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class AllomancyAtium extends AllomancyBase
{
	public AllomancyAtium(Metals.MetalType metalType)
	{
		super(metalType);


	}

	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		//Reveals Your Future
		{
			//todo
		}
	}


	public static void onLivingAttackEvent(LivingAttackEvent event)
	{
		if (event.isCanceled())
		{
			return;
		}

		SpiritwebCapability.get(event.getEntityLiving()).ifPresent((targetData) ->
		{
			AllomancyBase atium = ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.ATIUM).get();
			AllomancyBase electrum = ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.ELECTRUM).get();

			if (atium.isActive(targetData))
			{
				//target is burning atium, so may automatically cancel damage.
				//check that attacker is not also burning atium or electrum


				//if the attacker is capable of burning atium
				if (event.getSource().getEntity() instanceof LivingEntity attackerLiving)
				{
					final LazyOptional<ISpiritweb> iSpiritwebLazyOptional = SpiritwebCapability.get(attackerLiving);

					if (iSpiritwebLazyOptional.isPresent())
					{
						iSpiritwebLazyOptional.ifPresent((attackerData) ->
						{
							if (atium.isActive(attackerData) || electrum.isActive(attackerData))
							{
								//contested, prevents automatically dodging.
							}
							else
							{
								//attacker is not burning contesting metals.
								event.setCanceled(true);
							}
						});
					}
					else
					{
						//attacker has no spiritweb
						event.setCanceled(true);
					}
				}
				else
				{
					//attacker is not living
					//atium isn't going to prevent damage from swimming in lava for example.
				}
			}
		});
	}
}
