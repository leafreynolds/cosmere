/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;

// food
public class BendalloyStoreEffect extends FeruchemyEffectBase
{
	public BendalloyStoreEffect(Metals.MetalType type, MobEffectCategory effectType)
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
			foodData.setFoodLevel(Math.max(0, foodData.getFoodLevel() - (1 + amplifier)));
			foodData.setSaturation(Math.min(foodData.getFoodLevel(), foodData.getSaturationLevel()));
		}
	}
}
