/*
 * File created ~ 27 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.feruchemy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.charge.MetalmindChargeHelper;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.EffectsHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;

public class FeruchemyBendalloy extends FeruchemyBase
{
	public FeruchemyBendalloy(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public int getCost(ISpiritweb data)
	{
		int cost = super.getCost(data);

		if (data.getLiving() instanceof Player player)
		{
			FoodData foodData = player.getFoodData();
			if (isTapping(data) && !foodData.needsFood() && foodData.getSaturationLevel() < foodData.getFoodLevel())
			{
				//todo config
				//currently double cost if only doing saturation
				cost *= 2;
			}
		}

		return cost;
	}

	@Override
	public void tick(ISpiritweb data)
	{
		//don't check every tick.
		LivingEntity livingEntity = data.getLiving();
		int mode = getMode(data);
		if (mode == 0)
		{
			return;
		}

		if (livingEntity instanceof Player player)
		{
			FoodData foodData = player.getFoodData();
			if (isStoring(data) && foodData.getFoodLevel() <= 0)
			{
				//no food to store
				return;
			}
			else if (isTapping(data) && !(foodData.needsFood() || foodData.getSaturationLevel() < foodData.getFoodLevel()))
			{
				//already full
				return;
			}
		}

		super.tick(data);
	}
}
