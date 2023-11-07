/*
 * File updated ~ 19 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;


public class BendalloyTapEffect extends FeruchemyEffectBase
{
	public BendalloyTapEffect(Metals.MetalType type)
	{
		super(type);
	}

	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, double strength)
	{
		if (!isActiveTick(entityLivingBaseIn))
		{
			return;
		}

		if (!entityLivingBaseIn.level.isClientSide)
		{
			final FoodData foodData = ((Player) entityLivingBaseIn).getFoodData();
			final int i = (int) (1 + strength);
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
