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
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

//warmth
public class BrassStoreEffect extends FeruchemyEffectBase
{
	public BrassStoreEffect(Metals.MetalType type)
	{
		super(type);
		addAttributeModifier(
				AttributesRegistry.WARMTH.getHolder(),
				-1, // colder when storing
				AttributeModifier.Operation.ADD_VALUE);
	}

	public static void onLivingHurtEvent(LivingIncomingDamageEvent event)
	{
		if (!event.getSource().is(DamageTypes.ON_FIRE) || event.isCanceled())
		{
			return;
		}

		//a higher total means hotter
		//a lower total means colder
		final int total = (int) EntityHelper.getAttributeValue(event.getEntity(), AttributesRegistry.WARMTH.getHolder());
		if (total < 0)
		{
			//absolute value, because we're using the mode as the strength for feruchemy
			int warmth = Math.abs(total);
			final float amount;
			switch (warmth)
			{
				case 1:
					amount = event.getAmount() / 2;
					break;
				case 2:
					amount = event.getAmount() / 4;
					break;
				default:
				case 3:
					if (event.getEntity().isOnFire())
					{
						event.getEntity().clearFire();
					}
					event.setCanceled(true);
					return;
			}
			event.setAmount(amount);
		}
	}

}
