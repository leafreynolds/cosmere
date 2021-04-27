/*
 * File created ~ 27 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.feruchemy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.charge.ItemChargeHelper;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.helpers.EffectsHelper;
import leaf.cosmere.helpers.XPHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

public class FeruchemyCopper extends FeruchemyBase
{
    public FeruchemyCopper(Metals.MetalType metalType)
    {
        super(metalType);
    }

    @Override
    public int modeMin(ISpiritweb data)
    {
        return -20;
    }

    @Override
    public int modeMax(ISpiritweb data)
    {
        return 20;
    }

    @Override
    public void tick(ISpiritweb data)
    {
        //don't check every tick.
        LivingEntity livingEntity = data.getLiving();

        if (!(livingEntity instanceof PlayerEntity) || livingEntity.ticksExisted % 20 != 0)
        {
            return;
        }
        PlayerEntity playerEntity = (PlayerEntity) livingEntity;

        int mode = data.getMode(manifestationType, metalType.getID());

        //can't store or tap any more
        if (mode == 0)
        {
            //remove active effects.
            //let the current effect run out.
            return;
        }

        performXPAdjustment(playerEntity, mode);

    }

    private int getCost(int mode)
    {
        // if we are tapping
        //check if there is charges to tap
        if (mode < 0)
        {
            //wanting to tap
            //get cost
            return mode <= -3 ? -(mode * mode) : mode;

        }
        //if we are storing
        //check if there is space to store
        else if (mode > 0)
        {
            return mode;
        }
        return 0;
    }


    private void performXPAdjustment(PlayerEntity playerEntity, int mode)
    {
        int cost = getCost(mode);
        int experiencePoints = playerEntity.isCreative() ? 10 : cost;

        if (playerEntity.experienceTotal > cost && ItemChargeHelper.adjustMetalmindChargeExact(playerEntity, metalType, -experiencePoints, true, true))
        {
            //successfully added xp to metalmind

            if (!playerEntity.isCreative())
            {
                //adjust player xp

                if (cost > 0) // active storage
                {
                    decreasePlayerExperience(playerEntity, cost);
                }
                else // tapping storage
                {
                    playerEntity.giveExperiencePoints(-cost);
                }

            }


            Effect effect = getEffect(mode);

            EffectInstance currentEffect = EffectsHelper.getNewEffect(effect, Math.abs(mode) - 1);

            //potion effect doesn't do anything other than tell the player they are storing.
            playerEntity.addPotionEffect(currentEffect);
        }
    }


    //why does minecraft not have functions that handle this better
    //unless it does, in which case I'm a goober who can't read.
    //either way, thank you xreliquary and P3pp3rF1y showing their example of decreasing player xp nicely.
    //https://github.com/P3pp3rF1y/Reliquary/blob/1.16.x/src/main/java/xreliquary/items/HeroMedallionItem.java
    private void decreasePlayerExperience(PlayerEntity player, int pointsToRemove)
    {
        player.experienceTotal -= pointsToRemove;
        int newLevel = XPHelper.getLevelForExperience(player.experienceTotal);
        player.experienceLevel = newLevel;
        player.experience = (float) (player.experienceTotal - XPHelper.getExperienceForLevel(newLevel)) / player.xpBarCap();
    }

}
