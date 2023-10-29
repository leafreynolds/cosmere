/*
 * File updated ~ 27 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.math.VectorHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.eventHandlers.ModBusEventHandler;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

import java.util.Arrays;
import java.util.List;

public class AllomancyBronze extends AllomancyManifestation
{
	public AllomancyBronze(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		LivingEntity livingEntity = data.getLiving();
		boolean isActiveTick = livingEntity.tickCount % 20 == 0;
		//Detects Allomantic Pulses

		//passive active ability, if any
		if (livingEntity instanceof ServerPlayer playerEntity && isActiveTick)
		{
			int distance = getRange(data);
			List<LivingEntity> entitiesToCheckForAllomancy = EntityHelper.getLivingEntitiesInRange(livingEntity, distance, false);

			for (LivingEntity targetEntity : entitiesToCheckForAllomancy)
			{
				if (!AllomancyBronze.contestConcealment(data, targetEntity))
				{
					//does not meet requirements for seeking.
					//either not using bronze, not in range, or not strong enough
					seekTarget(playerEntity, targetEntity);
				}
			}
		}
	}

	private void seekTarget(ServerPlayer playerEntity, LivingEntity targetEntity)
	{
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
				if (targetSpiritweb.canTickManifestation(Manifestations.ManifestationTypes.ALLOMANCY.getManifestation(metalTypeID)))
				{
					//found one

					//todo play thump sound
					//get the position between the user and the entity we found

					//end point minus start point, then normalize
					BlockPos destinationPosition = targetEntity.blockPosition();
					BlockPos startingPosition = playerEntity.blockPosition();

					BlockPos direction = new BlockPos(VectorHelper.Normalize(destinationPosition.subtract(startingPosition)));

					//todo play in direction of target
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


	public static boolean contestConcealment(ISpiritweb seeker, LivingEntity potentialConcealed)
	{
		//can't get anything from entities that don't have powers
		if (Arrays.stream(ModBusEventHandler.ENTITIES_THAT_CAN_HAVE_POWERS).anyMatch(test -> test != potentialConcealed.getType()))
		{
			return false;
		}


		final AllomancyManifestation bronzeAllomancyManifestation = AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.BRONZE).get();
		//if the player does not have bronze, early exit
		if (!bronzeAllomancyManifestation.isAllomanticBurn(seeker))
		{
			//powers not active, don't bother
			return false;
		}
		final double bronzeStrength = bronzeAllomancyManifestation.getStrength(seeker, false);

		//todo range to config
		//get allomantic strength of
		double range = bronzeAllomancyManifestation.getRange(seeker);
		final boolean inRangeOfBronze = seeker.getLiving().distanceTo(potentialConcealed) < range;
		if (!inRangeOfBronze)
		{
			//target isn't even in range, nope
			return false;
		}
		//if target has copper and it's active, early exit
		final AttributeMap targetAttributes = potentialConcealed.getAttributes();
		double concealmentStrength = 0;
		final Attribute cognitiveConcealmentAttr = AttributesRegistry.COGNITIVE_CONCEALMENT.get();
		if (targetAttributes.hasAttribute(cognitiveConcealmentAttr))
		{
			concealmentStrength = targetAttributes.getValue(cognitiveConcealmentAttr);
		}

		//do they have more concealment than the player has bronze strength?
		if (concealmentStrength >= bronzeStrength)
		{
			//target has more concealment than the seeker has strength
			return false;
		}

		//seeking successful
		return true;
	}
}
