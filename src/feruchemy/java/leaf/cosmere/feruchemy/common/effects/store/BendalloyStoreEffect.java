/*
 * File updated ~ 9 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;

// food
public class BendalloyStoreEffect extends FeruchemyEffectBase
{
	public BendalloyStoreEffect(Metals.MetalType type)
	{
		super(type);
	}


	@Override
	public void applyEffectTick(ISpiritweb data, double strength)
	{
		if (data.getLiving() instanceof Player player && !player.level().isClientSide)
		{
			final FoodData foodData = player.getFoodData();
			foodData.setFoodLevel((int) Math.max(0, foodData.getFoodLevel() - (1 + strength)));
			foodData.setSaturation(Math.min(foodData.getFoodLevel(), foodData.getSaturationLevel()));
		}
	}
}
