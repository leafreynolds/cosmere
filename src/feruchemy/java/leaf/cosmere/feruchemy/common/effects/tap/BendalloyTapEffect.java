/*
 * File updated ~ 9 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;


public class BendalloyTapEffect extends FeruchemyEffectBase
{
	public BendalloyTapEffect(Metals.MetalType type)
	{
		super(type);
	}

	@Override
	public void applyEffectTick(ISpiritweb data, double strength)
	{
		if (data.getLiving() instanceof Player player && !player.level().isClientSide)
		{
			final FoodData foodData = player.getFoodData();
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
