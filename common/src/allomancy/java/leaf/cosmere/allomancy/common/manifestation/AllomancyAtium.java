/*
 * File updated ~ 10 - 1 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.common.registries.AllomancyAttributes;
import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class AllomancyAtium extends AllomancyManifestation
{
	public AllomancyAtium(Metals.MetalType metalType)
	{
		super(metalType);


	}

	@Override
	public Attribute getAttribute()
	{
		return AllomancyAttributes.ALLOMANCY_ATTRIBUTES.get(Metals.MetalType.ELECTRUM).getAttribute();
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

		SpiritwebCapability.get(event.getEntity()).ifPresent((targetData) ->
		{
			AllomancyManifestation atium = AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.ATIUM).get();
			AllomancyManifestation electrum = AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.ELECTRUM).get();

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
