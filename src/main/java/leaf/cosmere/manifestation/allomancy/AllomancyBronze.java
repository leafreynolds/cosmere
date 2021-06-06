/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.*;
import leaf.cosmere.utils.helpers.VectorHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.*;
import net.minecraft.state.properties.NoteBlockInstrument;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.util.List;

import static leaf.cosmere.utils.helpers.EntityHelper.getLivingEntitiesInRange;

public class AllomancyBronze extends AllomancyBase
{
    public AllomancyBronze(Metals.MetalType metalType)
    {
        super(metalType);
    }

    @Override
    protected void performEffect(ISpiritweb data)
    {
        LivingEntity livingEntity = data.getLiving();
        boolean isActiveTick = livingEntity.ticksExisted % 20 == 0;
        //Detects Allomantic Pulses

        //passive active ability, if any
        if (livingEntity instanceof ServerPlayerEntity && isActiveTick)
        {
            ServerPlayerEntity playerEntity = (ServerPlayerEntity) livingEntity;

            int distance = getRange(data);
            List<LivingEntity> entitiesToCheckForAllomancy = getLivingEntitiesInRange(livingEntity, distance, false);

            for (LivingEntity targetEntity : entitiesToCheckForAllomancy)
            {
                EffectInstance copperEffect = targetEntity.getActivePotionEffect(EffectsRegistry.ALLOMANTIC_COPPER.get());
                if (copperEffect != null && copperEffect.getDuration() > 0)
                {
                    //skip clouded entities.
                    continue;
                }

                SpiritwebCapability.get(targetEntity).ifPresent(targetSpiritweb ->
                {
                    //check if any allomantic powers are active
                    for (Metals.MetalType metalType : Metals.MetalType.values())
                    {
                        if (!metalType.hasAssociatedManifestation())
                        {
                            continue;
                        }

                        int metalTypeID = metalType.getID();
                        //todo decide what to do about this part
                        //since this is running on the server specifically.
                        if (targetSpiritweb.canTickManifestation(Manifestations.ManifestationTypes.ALLOMANCY, metalTypeID))
                        {
                            //found one

                            //todo play thump sound
                            //get the position between the user and the entity we found

                            //end point minus start point, then normalize
                            BlockPos destinationPosition = targetEntity.getPosition();
                            BlockPos startingPosition = livingEntity.getPosition();

                            BlockPos direction = new BlockPos(VectorHelper.Normalize(destinationPosition.subtract(startingPosition)));

                            playerEntity.playSound(
                                    NoteBlockInstrument.BASEDRUM.getSound(),
                                    SoundCategory.PLAYERS, //todo check this category
                                    3.0F, //volume
                                    1.0F); //pitch

                            //todo visual cue?
                            //todo make this stuff only happen for the user
                            targetEntity.world.addParticle(
                                    ParticleTypes.NOTE,
                                    (double) direction.getX() + 0.5D,
                                    (double) direction.getY() + 1.2D,
                                    (double) direction.getZ() + 0.5D,
                                    (double) metalTypeID / 24.0D,
                                    0.0D,
                                    0.0D);

                            break;
                        }
                    }
                });
            }
        }
    }
}
