/*
 * File updated ~ 12 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.common.eventHandlers;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEventHandler
{

	@SubscribeEvent
	public static void onEntityJoinWorldEvent(EntityJoinLevelEvent event)
	{
		Entity eventEntity = event.getEntity();

		if (eventEntity.level.isClientSide || !(eventEntity instanceof LivingEntity livingEntity))
		{
			return;
		}

		SpiritwebCapability.get(livingEntity).ifPresent(iSpiritweb ->
		{
			SpiritwebCapability spiritweb = (SpiritwebCapability) iSpiritweb;

			//find out if any innate powers exist on the entity first
			//if they do
			if (spiritweb.hasBeenInitialized() || spiritweb.hasAnyPowers())
			{
				//then skip
				//no need to give them extras just for rejoining the world.
				return;
			}

			//players always start with powers
			if (eventEntity instanceof Player)
			{
				//todo from random powertype?
				{
					//give random power
					giveEntityStartingManifestation(livingEntity, spiritweb);
				}
			}
			else if (canStartWithPowers(eventEntity))
			{
				//random 1/16
				// only 1 in 16 will have the gene


				final int chance = eventEntity instanceof Raider ? 50 : 16;
				if (MathHelper.randomInt(1, chance) % chance == 0)
				{
					giveEntityStartingManifestation(livingEntity, spiritweb);
				}

			}

			spiritweb.setHasBeenInitialized();
		});

	}

	public static boolean canStartWithPowers(Entity entity)
	{
		return entity instanceof Player
				|| entity instanceof AbstractVillager
				|| entity instanceof ZombieVillager
				|| (entity instanceof Raider && !(entity instanceof Ravager))
				|| entity instanceof AbstractPiglin;
	}

	//todo eventually we want to replace this. Maybe an origins style menu that lets you choose a randomised power by world type
	public static void giveEntityStartingManifestation(LivingEntity entity, SpiritwebCapability spiritwebCapability)
	{
		boolean isPlayerEntity = entity instanceof Player;
		//low chance of having full powers of one type
		//0-15 inclusive is normal powers.
		boolean isFullPowersFromOneType = MathHelper.randomInt(0, 16) % 16 == 0;

		//small chance of being twin born, but only if not having full powers above
		//except for players who are guaranteed having at least two powers.
		boolean isTwinborn = isPlayerEntity || MathHelper.randomInt(0, 16) < 3;

		//randomise the given powers from allomancy and feruchemy
		int allomancyPowerID = MathHelper.randomInt(0, 15);
		int feruchemyPowerID = MathHelper.randomInt(0, 15);

		final Metals.MetalType allomancyMetal = Metals.MetalType.valueOf(allomancyPowerID).get();
		final Metals.MetalType feruchemyMetal = Metals.MetalType.valueOf(feruchemyPowerID).get();

		//if not twinborn, pick one power
		boolean isAllomancy = MathHelper.randomBool();

		if (isFullPowersFromOneType)
		{
			//ooh full powers
			final Manifestations.ManifestationTypes manifestationType =
					isAllomancy
					? Manifestations.ManifestationTypes.ALLOMANCY
					: Manifestations.ManifestationTypes.FERUCHEMY;

			for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
			{
				if (manifestation.getManifestationType() == manifestationType)
				{
					spiritwebCapability.giveManifestation(manifestation, 8);

				}
			}
			if (spiritwebCapability.getLiving() instanceof Player player)
			{
				spiritwebCapability.spiritwebSubmodules.get(manifestationType).GiveStartingItem(player);
			}
		}
		else
		{
			final Manifestation allomancyPower = Manifestations.ManifestationTypes.ALLOMANCY.getManifestation(allomancyMetal.getID());
			final Manifestation feruchemyPower = Manifestations.ManifestationTypes.FERUCHEMY.getManifestation(feruchemyMetal.getID());
			if (isTwinborn)
			{
				spiritwebCapability.giveManifestation(allomancyPower, 9);
				spiritwebCapability.giveManifestation(feruchemyPower, 9);

				if (spiritwebCapability.getLiving() instanceof Player player)
				{
					spiritwebCapability.spiritwebSubmodules.get(Manifestations.ManifestationTypes.ALLOMANCY).GiveStartingItem(player, allomancyPower);
					spiritwebCapability.spiritwebSubmodules.get(Manifestations.ManifestationTypes.FERUCHEMY).GiveStartingItem(player, feruchemyPower);
				}
			}
			else
			{
				Manifestation manifestation =
						isAllomancy
						? allomancyPower
						: feruchemyPower;

				spiritwebCapability.giveManifestation(manifestation, 10);

				//at this time, players are twin-born minimum, so no need to try give powers here
			}
		}
	}

	@SubscribeEvent
	public static void onLivingTick(LivingEvent.LivingTickEvent event)
	{
		SpiritwebCapability.get(event.getEntity()).ifPresent(ISpiritweb::tick);
	}
}
