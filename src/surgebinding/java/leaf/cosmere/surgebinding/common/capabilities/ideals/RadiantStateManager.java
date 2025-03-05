/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.capabilities.ideals;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.util.TaskQueueManager;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capabilities.ideals.order.WindrunnerIdealStateManager;
import leaf.cosmere.surgebinding.common.config.SurgebindingConfigs;
import leaf.cosmere.surgebinding.common.config.SurgebindingServerConfig;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.event.ServerChatEvent;
import org.jetbrains.annotations.NotNull;

public class RadiantStateManager
{
	private static ResourceLocation SWEAR_IDEAL = new ResourceLocation(Surgebinding.MODID, "swear_ideal");

	private SpiritwebCapability spiritweb;
	private Roshar.RadiantOrder order = null;
	private int ideal = 0;

	public Roshar.RadiantOrder getOrder()
	{
		return order;
	}

	public int getIdeal()
	{
		return ideal;
	}

	public void serialize(ISpiritweb spiritweb)
	{
		final CompoundTag compoundTag = spiritweb.getCompoundTag();

		if (order != null)
		{
			compoundTag.putInt("order", order.getID());
		}
		else if (compoundTag.contains("order")) //remove tracking of order if it's null
		{
			compoundTag.remove("order");
		}

		compoundTag.putInt("ideal", ideal);
	}

	public void deserialize(ISpiritweb spiritweb)
	{
		this.spiritweb = (SpiritwebCapability) spiritweb;
		final CompoundTag spiritwebCompoundTag = spiritweb.getCompoundTag();
		this.order = spiritwebCompoundTag.contains("order")
		             ? Roshar.RadiantOrder.valueOf(spiritwebCompoundTag.getInt("order")).orElse(null)
		             : null;
		this.ideal = spiritwebCompoundTag.getInt("ideal");
	}


	public void onChatMessageReceived(ServerChatEvent event)
	{
		//players can swear an ideal by typing a specific message in chat
		//it's more efficient to check if a chat message is relevant before checking
		// if the player meets any requirements needed to do something with those messages

		//todo maybe we should check if the player is in the right dimension first?

		//todo
		{
			//a player can potentially bond _two_ spren, not just a single one
			//this means they can have two sets of surges, and two sets of ideals
			//so maybe it's fine to hard code to only two sets of radiant orders?
			//todo config for allowing a second order
			//tbd on the conditions required before a player can bond a second spren
		}

		String playerMessage = SurgebindingServerConfig.cleanIdeal(event.getRawText());
		int idealToSwear = 0;
		Roshar.RadiantOrder idealOrder = null;

		//every order's first ideal is the same

		boolean foundAssociatedIdeal = false;

		if (idealToSwear <= this.ideal)
		{
			return;
		}

		//for each order
		for (Roshar.RadiantOrder radiantOrder : EnumUtils.RADIANT_ORDERS)
		{
			if (foundAssociatedIdeal)
			{
				break;
			}

			//for each ideal
			for (int ideal = 1; ideal <= 5; ideal++)
			{
				String thisIdeal = SurgebindingConfigs.SERVER.getIdeal(ideal, radiantOrder.getID());
				if (thisIdeal.equals(SurgebindingServerConfig.IDEAL_NOT_IMPLEMENTED))
				{
					//skip ideals that are not yet implemented
					continue;
				}

				if (playerMessage.contains(thisIdeal))
				{
					//check if the message matches the ideal
					//if it does, set idealToSwear to the ideal's index
					//also set which order the ideal belongs to
					//break out of the loop
					idealToSwear = ideal;

					//if it's not the first ideal
					if (idealToSwear != 1)
					{
						//only the first ideal is universal,
						idealOrder = radiantOrder;
					}

					foundAssociatedIdeal = true;
					break;
				}
			}
		}

		if (idealToSwear == 0)
		{
			//not relevant, exit
			return;
		}

		if (this.ideal > 2 && this.order == null)
		{
			CosmereAPI.logger.error("Player has sworn a higher ideal but has no order set?? How does this happen. Resetting to zero.");
			this.ideal = 0;
		}

		if (this.ideal == 0 && this.order != null)
		{
			CosmereAPI.logger.error("Player has an order despite swearing no oaths?. Resetting.");
			this.order = null;
		}

		//assuming we have found a message that matches trying to qualify for an ideal
		boolean successfulSwear = false;
		//check the current state of the player's spiritweb
		//swearing the first or second ideal
		if (this.order == null)
		{

			//we want to know whether they are unoathed, or have sworn an ideal
			//if they have sworn an ideal, we want to know which one
			//ideal has to be zero, check if swearing the first ideal.
			if (idealToSwear == 1 && this.ideal == 0)
			{
				successfulSwear = onTrySuccessfulSwear(idealToSwear, null);
			}
			else if (idealToSwear == 2 && this.ideal == 1)
			{
				successfulSwear = onTrySuccessfulSwear(idealToSwear, idealOrder);
			}
			// else we

			if (successfulSwear)
			{
				this.ideal++;
				this.order = idealOrder;
				onSuccessfulIdealSworn(spiritweb);
			}

			//no need to continue
			return;
		}

		//else they must be trying to swear a higher ideal
		if (this.order.equals(idealOrder))
		{
			boolean swearingHigherIdeal = idealToSwear > 2;
			//swearing a higher ideal
			if (idealToSwear == this.ideal + 1)
			{
				successfulSwear = onTrySuccessfulSwear(idealToSwear, idealOrder);
			}

			if (successfulSwear)
			{
				this.ideal++;
				onSuccessfulIdealSworn(spiritweb);
			}
			else
			{
				//only the higher ideas tell you if words are accepted?
				//todo translatable
				event.setCanceled(true);
				event.getPlayer().sendSystemMessage(Component.literal("THESE WORDS ARE NOT ACCEPTED."));
			}
		}
	}

	private boolean onTrySuccessfulSwear(int idealToSwear, Roshar.RadiantOrder idealOrder)
	{
		if (idealToSwear == 1 && idealOrder == null)
		{
			//swearing the first ideal
			return true;
		}

		switch (idealOrder)
		{
			case WINDRUNNER ->
			{
				return WindrunnerIdealStateManager.validateIdeal(spiritweb, idealToSwear);
			}
			case SKYBREAKER ->
			{
			}
			case DUSTBRINGER ->
			{
			}
			case EDGEDANCER ->
			{
			}
			case TRUTHWATCHER ->
			{
			}
			case LIGHTWEAVER ->
			{
			}
			case ELSECALLER ->
			{
			}
			case WILLSHAPER ->
			{
			}
			case STONEWARD ->
			{
			}
			case BONDSMITH ->
			{
			}
		}


		return false;
	}


	private void onSuccessfulIdealSworn(@NotNull SpiritwebCapability spiritweb)
	{
		//rumble if ideal 2
		if (spiritweb.getLiving() instanceof ServerPlayer player)
		{
			//todo we should check the configs for whether this should be broadcasted to all players, or just the player themselves

			final Runnable work = () ->
			{
				//todo translatable
				if (this.ideal != 1)
				{
					player.sendSystemMessage(Component.literal("THESE WORDS ARE ACCEPTED."));
					updatePowerState();
				}
				//player.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 1000, 0.8F + player.getRandom().nextFloat() * 0.2F);
				player.level().playSound(
						null,//null so it also sends to triggering player
						player.getX(),
						player.getY(),
						player.getZ(),
						SoundEvents.LIGHTNING_BOLT_THUNDER,
						SoundSource.WEATHER,
						10000.0F,
						0.8F + player.getRandom().nextFloat() * 0.2F
				);


				//todo - if the player is trying to swear an ideal, check if they have stormlight nearby to facilitate the process
				{
					//if they do, we should consume the stormlight and give them the appropriate buffs
				}
				{
					//if they don't, we should inform them that they need more stormlight to swear an ideal?
					//or maybe just allow the words to be accepted but don't heal them or give them the buffs
				}

				//todo spawn particle order glyph at player if higher ideal

			};

			TaskQueueManager.submitDelayedTask(SWEAR_IDEAL, 60, new TaskQueueManager.OneOffTask(work));
		}
	}

	private void updatePowerState()
	{
		Manifestation firstSurge = SurgebindingManifestations.SURGEBINDING_POWERS.get(this.order.getFirstSurge()).get();
		Manifestation secondSurge = SurgebindingManifestations.SURGEBINDING_POWERS.get(this.order.getSecondSurge()).get();

		spiritweb.giveManifestation(firstSurge, getIdeal());
		spiritweb.giveManifestation(secondSurge, getIdeal());
	}
}
