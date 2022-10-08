/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
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
				foodData.eat(1, i);
			}

			//todo add tough as nails mod compatibility?
		}
	}
}
