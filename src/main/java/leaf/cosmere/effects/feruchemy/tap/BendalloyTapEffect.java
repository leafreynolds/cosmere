/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.tap;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;


public class BendalloyTapEffect extends FeruchemyEffectBase
{
	public BendalloyTapEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);
	}

	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier)
	{
		if (!isActiveTick(entityLivingBaseIn))
		{
			return;
		}

		if (!entityLivingBaseIn.level.isClientSide)
		{
			final FoodData foodData = ((Player) entityLivingBaseIn).getFoodData();
			final int i = 1 + amplifier;
			if (foodData.needsFood())
			{
				foodData.setFoodLevel(foodData.getFoodLevel() + i);
			}
			else
			{
				//add saturation after.
				foodData.eat(0, i);
			}

			//todo add tough as nails mod compatibility?
		}
	}
}
