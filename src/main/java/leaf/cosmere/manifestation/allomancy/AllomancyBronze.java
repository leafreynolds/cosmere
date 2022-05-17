/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.EffectsRegistry;
import leaf.cosmere.utils.helpers.VectorHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

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
		boolean isActiveTick = livingEntity.tickCount % 20 == 0;
		//Detects Allomantic Pulses

		//passive active ability, if any
		if (livingEntity instanceof ServerPlayer playerEntity && isActiveTick)
		{

			int distance = getRange(data);
			List<LivingEntity> entitiesToCheckForAllomancy = getLivingEntitiesInRange(livingEntity, distance, false);

			for (LivingEntity targetEntity : entitiesToCheckForAllomancy)
			{
				MobEffectInstance copperEffect = targetEntity.getEffect(EffectsRegistry.ALLOMANTIC_COPPER.get());
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
							BlockPos destinationPosition = targetEntity.blockPosition();
							BlockPos startingPosition = livingEntity.blockPosition();

							BlockPos direction = new BlockPos(VectorHelper.Normalize(destinationPosition.subtract(startingPosition)));

							playerEntity.playNotifySound(
									NoteBlockInstrument.BASEDRUM.getSoundEvent(),
									SoundSource.PLAYERS, //todo check this category
									3.0F, //volume
									1.0F); //pitch

							//todo visual cue?
							//todo make this stuff only happen for the user
							targetEntity.level.addParticle(
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
