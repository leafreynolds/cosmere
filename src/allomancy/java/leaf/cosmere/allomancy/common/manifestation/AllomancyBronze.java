/*
 * File updated ~ 12 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.eventHandlers.ModBusEventHandler;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

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
		boolean isActiveTick = isActiveTick(data);
		//Detects Allomantic Pulses

		//passive active ability, if any
		if (livingEntity instanceof ServerPlayer playerEntity && isActiveTick)
		{
			int distance = getRange(data);
			List<LivingEntity> entitiesToCheckForAllomancy = EntityHelper.getLivingEntitiesInRange(livingEntity, distance, false);

			for (LivingEntity targetEntity : entitiesToCheckForAllomancy)
			{
				if (AllomancyBronze.isValidSeekTarget(data, targetEntity))
				{
					seekTarget(playerEntity, targetEntity);
				}
				//does not meet requirements for seeking.
				//either not using bronze, not in range, or not strong enough, or target has no powers
			}
		}
	}

	private void seekTarget(ServerPlayer seekerPlayer, LivingEntity targetEntity)
	{
		SpiritwebCapability.get(targetEntity).ifPresent(targetSpiritweb ->
		{
			//show all manifestations, including hemalurgic based ones.
			for (Manifestation manifestation : targetSpiritweb.getAvailableManifestations())
			{
				//since this is running on the server specifically.
				if (!(targetEntity instanceof Player) || targetSpiritweb.canTickManifestation(manifestation))
				{
					//found one
					//play thump sound
					//todo, make it so the sounds are spread out based on power id.
					//that would potentially be too much scanning though, since we are trying not to do stuff every frame
					final float pitch = Mth.lerp(manifestation.getPowerID() / 16f, 0.5F, 2.0F);
					seekerPlayer.connection.send(
							new ClientboundSoundPacket(
									SoundEvents.NOTE_BLOCK_BASEDRUM,
									SoundSource.NEUTRAL,
									targetEntity.getX(),
									targetEntity.getY(),
									targetEntity.getZ(),
									64.0f,
									pitch,
									targetEntity.getRandom().nextLong()
							)
					);

					break;
				}

			}
		});
	}


	public static boolean isValidSeekTarget(ISpiritweb seeker, LivingEntity potentialConcealed)
	{
		//can't get anything from entities that don't have powers
		if (!Arrays.stream(ModBusEventHandler.ENTITIES_THAT_CAN_HAVE_POWERS).anyMatch(test -> test == potentialConcealed.getType()))
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
