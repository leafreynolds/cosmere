/*
 * File created ~ 25 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Manifestations;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class EntityHelper
{
    public static List<LivingEntity> getLivingEntitiesInRange(LivingEntity selfEntity, int range, boolean includeSelf)
    {
        AxisAlignedBB areaOfEffect = new AxisAlignedBB(selfEntity.blockPosition());
        areaOfEffect = areaOfEffect.inflate(range, range, range);

        List<LivingEntity> entitiesFound = selfEntity.level.getEntitiesOfClass(LivingEntity.class, areaOfEffect);

        if (!includeSelf)
        {
            //removes self entity if it exists in the list
            //otherwise list unchanged
            entitiesFound.remove(selfEntity);
        }

        return entitiesFound;
    }

    public static List<Entity> getEntitiesInRange(Entity entity, int range, boolean includeSelf)
    {
        AxisAlignedBB areaOfEffect = new AxisAlignedBB(entity.blockPosition());
        areaOfEffect = areaOfEffect.expandTowards(range, range, range);

        List<Entity> entitiesFound = entity.level.getEntitiesOfClass(Entity.class, areaOfEffect);

        if (!includeSelf && entitiesFound.contains(entity))
        {
            entitiesFound.remove(entity);
        }

        return entitiesFound;
    }


    public static void giveEntityStartingManifestation(LivingEntity entity, SpiritwebCapability spiritwebCapability)
    {
        boolean isPlayerEntity = entity instanceof PlayerEntity;
        //low chance of having full powers of one type
        boolean isFullPowersFromOneType = MathHelper.randomInt(0, 16) % 16 == 0;

        //small chance of being twin born, but only if not having full powers above
        //except for players who are guaranteed having at least two powers.
        boolean isTwinborn = isPlayerEntity || MathHelper.randomInt(0, 16) < 3;

        //randomise the given powers from allomancy and feruchemy
        int allomancyPower = MathHelper.randomInt(0, 15);
        int feruchemyPower = MathHelper.randomInt(0, 15);

        //if not twinborn, pick one power
        boolean isAllomancy = MathHelper.randomBool();
        Manifestations.ManifestationTypes powerType = isAllomancy
                                                      ? Manifestations.ManifestationTypes.ALLOMANCY
                                                      : Manifestations.ManifestationTypes.FERUCHEMY;

        if (isFullPowersFromOneType)
        {
            //ooh full powers
            for (int i = 0; i < 16; i++)
            {
                spiritwebCapability.giveManifestation(powerType, i);
            }

            if (!isPlayerEntity)
            {
                //todo translations
                //todo grant random name
                entity.setCustomName(TextHelper.createTranslatedText(isAllomancy
                                                                     ? "Mistborn"
                                                                     : "Feruchemist"));
            }
        }
        else if (isTwinborn)
        {
            spiritwebCapability.giveManifestation(Manifestations.ManifestationTypes.ALLOMANCY, allomancyPower);
            spiritwebCapability.giveManifestation(Manifestations.ManifestationTypes.FERUCHEMY, feruchemyPower);

            if (!isPlayerEntity)
            {
                //todo translations
                //todo grant random name
                entity.setCustomName(TextHelper.createTranslatedText("Twinborn"));
            }
        }
        else
        {
            int powerID = isAllomancy ? allomancyPower : feruchemyPower;
            spiritwebCapability.giveManifestation(powerType, powerID);
            //todo translations
            //todo grant random name
            entity.setCustomName(powerType.getManifestation(powerID).translation());
        }
    }
}
