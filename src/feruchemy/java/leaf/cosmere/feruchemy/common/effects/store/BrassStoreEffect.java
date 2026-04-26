/*
 * File updated ~ 9 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

//warmth
public class BrassStoreEffect extends FeruchemyEffectBase
{
	public BrassStoreEffect(Metals.MetalType type)
	{
		super(type);
		addAttributeModifier(
				AttributesRegistry.WARMTH.get(),
				-1, // colder when storing
				AttributeModifier.Operation.ADD_VALUE);
	}

	public static void onLivingHurtEvent(LivingDamageEvent.Pre event)
	{
		if (!event.getSource().is(DamageTypes.ON_FIRE))
		{
			return;
		}

		//a higher total means hotter
		//a lower total means colder
		final int total = (int) EntityHelper.getAttributeValue(event.getEntity(), AttributesRegistry.WARMTH.getAttribute());
		if (total < 0)
		{
			//absolute value, because we're using the mode as the strength for feruchemy
			int warmth = Math.abs(total);
			final float amount;
			switch (warmth)
			{
				case 1:
					amount = event.getNewDamage() / 2;
					break;
				case 2:
					amount = event.getNewDamage() / 4;
					break;
				default:
				case 3:
					event.setNewDamage(0);
					return;
			}
			event.setNewDamage(amount);
		}
	}


	public static void onLivingAttackEvent(LivingIncomingDamageEvent event)
	{
		//todo - check if on fire is what this is meant to be
		//and whether we should actually be cancelling damage outright is correct
		if (!event.getSource().is(DamageTypes.ON_FIRE))
		{
			return;
		}

		//a higher total means hotter
		//a lower total means colder
		final int total = (int) EntityHelper.getAttributeValue(event.getEntity(), AttributesRegistry.WARMTH.getAttribute());
		if (total < 0)
		{
			//absolute value, because we're using the mode as the strength for feruchemy
			int warmth = Math.abs(total);
			switch (warmth)
			{
				case 0:
				case 1:
				case 2:
					break;
				default:
				case 3:
					if (event.getEntity().isOnFire())
					{
						event.getEntity().clearFire();
					}
					event.setCanceled(true);
			}
		}
	}

}
