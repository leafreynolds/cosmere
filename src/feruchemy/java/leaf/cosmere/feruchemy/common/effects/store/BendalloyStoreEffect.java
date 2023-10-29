/*
 * File updated ~ 29 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.entity.LivingEntity;
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
	public void applyEffectTick(LivingEntity entityLivingBaseIn, double strength)
	{
		if (!isActiveTick(entityLivingBaseIn))
		{
			return;
		}

		if (!entityLivingBaseIn.level.isClientSide)
		{
			final FoodData foodData = ((Player) entityLivingBaseIn).getFoodData();
			foodData.setFoodLevel((int) Math.max(0, foodData.getFoodLevel() - (1 + strength)));
			foodData.setSaturation(Math.min(foodData.getFoodLevel(), foodData.getSaturationLevel()));
		}
	}
}
