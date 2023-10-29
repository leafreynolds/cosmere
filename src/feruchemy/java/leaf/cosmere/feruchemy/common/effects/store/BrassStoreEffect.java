/*
 * File updated ~ 26 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

//warmth
public class BrassStoreEffect extends FeruchemyEffectBase
{
	public BrassStoreEffect(Metals.MetalType type)
	{
		super(type);
		addAttributeModifier(
				AttributesRegistry.WARMTH.get(),
				-1, // colder when storing
				AttributeModifier.Operation.ADDITION);
	}

	public static void onLivingHurtEvent(LivingHurtEvent event)
	{
		if (!event.getSource().isFire() || event.isCanceled())
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
					amount = event.getAmount() / 2;
					break;
				case 2:
					amount = event.getAmount() / 4;
					break;
				default:
				case 3:
					event.setCanceled(true);
					return;
			}
			event.setAmount(amount);
		}
	}


	public static void onLivingAttackEvent(LivingAttackEvent event)
	{
		if (!event.getSource().isFire() || event.isCanceled())
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
