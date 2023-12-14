/*
 * File updated ~ 26 - 5 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.feruchemy.common.config.FeruchemyConfigs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;

public class FeruchemyBendalloy extends FeruchemyManifestation
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
				//currently increase cost if only doing saturation
				cost *= FeruchemyConfigs.SERVER.BENDALLOY_SATURATION_MULTIPLIER.get();
			}
		}

		return cost;
	}

	@Override
	public boolean tick(ISpiritweb data)
	{
		//don't check every tick.
		LivingEntity livingEntity = data.getLiving();
		int mode = getMode(data);
		if (mode == 0)
		{
			return false;
		}

		if (livingEntity instanceof Player player)
		{
			FoodData foodData = player.getFoodData();
			if (isStoring(data) && foodData.getFoodLevel() <= 0)
			{
				//no food to store
				return false;
			}
			else if (isTapping(data) && !(foodData.needsFood() || foodData.getSaturationLevel() < foodData.getFoodLevel()))
			{
				//already full
				return false;
			}
		}

		return super.tick(data);
	}
}
