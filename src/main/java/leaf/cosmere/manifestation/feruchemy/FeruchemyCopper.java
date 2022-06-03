/*
 * File created ~ 27 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.feruchemy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.charge.MetalmindChargeHelper;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.XPHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FeruchemyCopper extends FeruchemyBase
{
	public FeruchemyCopper(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public int modeMin(ISpiritweb data)
	{
		return -1;
	}

	@Override
	public int modeMax(ISpiritweb data)
	{
		return 1;
	}

	@Override
	public void tick(ISpiritweb data)
	{
		//don't check every tick.
		LivingEntity livingEntity = data.getLiving();

		if (!(livingEntity instanceof Player playerEntity) || livingEntity.tickCount % 20 != 0)
		{
			return;
		}

		int mode = getMode(data);

		//can't store or tap any more
		if (mode == 0)
		{
			//remove active effects.
			//let the current effect run out.
			return;
		}

		performXPAdjustment(data, playerEntity);

	}


	private void performXPAdjustment(ISpiritweb data, Player playerEntity)
	{
		float experiencePoints;// = playerEntity.isCreative() ? 10 : mode;

		final boolean storing = isStoring(data);
		final boolean tapping = isTapping(data);

		if (storing) // active storage
		{
			//store xp progress, if any.
			if (playerEntity.experienceProgress > 0)
			{
				experiencePoints = playerEntity.experienceProgress;
			}
			//else store a level's worth of xp points.
			else
			{
				experiencePoints = XPHelper.getXpNeededForNextLevel(playerEntity.experienceLevel - 1);
			}
		}
		else
		{
			if (tapping) // tapping storage
			{
				experiencePoints = XPHelper.getXpNeededForNextLevel(playerEntity.experienceLevel);
			}
			else
			{
				return;
			}
		}


		//mode < 0 means we are tapping eg. removing from metalmind
		//mode > 0 means we are storing eg. adding to the metalmind


		//successfully added xp to metalmind
		final int xp = Mth.floor(experiencePoints);


		if ((storing && playerEntity.totalExperience >= xp) || tapping)
		{
			final ItemStack itemStack =
					MetalmindChargeHelper.adjustMetalmindChargeExact(
							playerEntity,
							metalType,
							storing ? -xp : xp,
							true,
							true);
			if (itemStack != null)
			{
				//adjust player xp

				if (storing) // active storage
				{
					decreasePlayerLevel(playerEntity, xp);
				}
				else // tapping storage
				{
					XPHelper.giveExperiencePoints(playerEntity, xp);
				}
			}
		}
	}


	//why does minecraft not have functions that handle this better
	//unless it does, in which case I'm a goober who can't read.
	//either way, thank you xreliquary and P3pp3rF1y showing their example of decreasing player xp nicely.
	//https://github.com/P3pp3rF1y/Reliquary/blob/1.16.x/src/main/java/xreliquary/items/HeroMedallionItem.java
	private static void decreasePlayerLevel(Player player, int pointsToRemove)
	{
		player.totalExperience = Math.max(player.totalExperience - pointsToRemove, 0);
		player.experienceLevel = XPHelper.getLevelForTotalExperience(player.totalExperience);
		player.experienceProgress = 0;
	}

}
