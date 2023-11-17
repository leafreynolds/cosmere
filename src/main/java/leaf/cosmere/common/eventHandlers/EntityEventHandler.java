/*
 * File updated ~ 27 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.common.eventHandlers;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.config.CosmereConfigs;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

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
				//todo choose based on planet? eg scadrial gets twinborn, roshar gets surgebinding etc?
				{
					//give random power
					giveEntityStartingManifestation(livingEntity, spiritweb);
				}
			}
			else if (canStartWithPowers(eventEntity))
			{
				//random 1/16
				// only 1 in 16 will have the gene


				final int raiderPowersChance = CosmereConfigs.SERVER_CONFIG.RAIDER_POWERS_CHANCE.get();
				final int mobPowersChance = CosmereConfigs.SERVER_CONFIG.MOB_POWERS_CHANCE.get();
				final int chance = eventEntity instanceof Raider ? raiderPowersChance : mobPowersChance;
				if (MathHelper.chance(chance))
				{
					giveEntityStartingManifestation(livingEntity, spiritweb);
				}

			}
			else if (eventEntity instanceof Warden warden)
			{
				//todo move this out
				final Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("allomancy:bronze"));
				if (attribute == null)
				{
					return;
				}
				AttributeInstance manifestationAttribute = livingEntity.getAttribute(attribute);

				if (manifestationAttribute != null)
				{
					manifestationAttribute.setBaseValue(9);
				}
			}

			spiritweb.setHasBeenInitialized();
		});


	}

	public static boolean canStartWithPowers(Entity entity)
	{
		//thanks to type erasure, java neutered their generics system.
		//No nice checking of parent types for us.

		return entity instanceof Player
				|| entity instanceof AbstractVillager
				|| entity instanceof ZombieVillager
				|| (entity instanceof Raider && !(entity instanceof Ravager))
				|| entity instanceof AbstractPiglin;
	}

	//todo eventually we want to replace this.
	// Maybe an origins style menu that lets you choose a randomised power by world type
	// Each mod could report the available powers, and what other mods they're allowed to spawn powers with (allomancy/feruchemy)
	public static void giveEntityStartingManifestation(LivingEntity entity, SpiritwebCapability spiritwebCapability)
	{
		boolean isPlayerEntity = entity instanceof Player;

		final Integer chanceOfFullPowers = CosmereConfigs.SERVER_CONFIG.FULLBORN_POWERS_CHANCE.get();
		final Integer chanceOfTwinborn = CosmereConfigs.SERVER_CONFIG.TWINBORN_POWERS_CHANCE.get();
		//low chance of having full powers of one type
		//0-15 inclusive is normal powers.
		boolean isFullPowersFromOneType = MathHelper.chance(chanceOfFullPowers);

		//small chance of being twin born, but only if not having full powers above
		//except for players who are guaranteed having at least two powers.
		boolean isTwinborn = isPlayerEntity || MathHelper.chance(chanceOfTwinborn);

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


			CosmereAPI.logger.info("Entity {} has full powers! {}", spiritwebCapability.getLiving().getName().getString(), manifestationType);

			for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
			{
				if (manifestation.getManifestationType() == manifestationType)
				{
					spiritwebCapability.giveManifestation(manifestation, 9);

				}
			}
			if (spiritwebCapability.getLiving() instanceof Player player)
			{
				spiritwebCapability.getSubmodule(manifestationType).GiveStartingItem(player);
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
					spiritwebCapability.getSubmodule(Manifestations.ManifestationTypes.ALLOMANCY).GiveStartingItem(player, allomancyPower);
					spiritwebCapability.getSubmodule(Manifestations.ManifestationTypes.FERUCHEMY).GiveStartingItem(player, feruchemyPower);
				}

				CosmereAPI.logger.info(
						"Entity {} has been granted allomantic {} and feruchemical {}!",
						spiritwebCapability.getLiving().getName().getString(),
						allomancyMetal,
						feruchemyMetal);
			}
			else
			{
				Manifestation manifestation =
						isAllomancy
						? allomancyPower
						: feruchemyPower;

				spiritwebCapability.giveManifestation(manifestation, 9);
				CosmereAPI.logger.info("Entity {} has been granted {}, with metal {}!",
						spiritwebCapability.getLiving().getName().getString(),
						isAllomancy
						? Manifestations.ManifestationTypes.ALLOMANCY.getName()
						: Manifestations.ManifestationTypes.FERUCHEMY.getName(),
						isAllomancy
						? allomancyMetal
						: feruchemyMetal);

				//at this time, players are twin-born minimum, so no need to try give powers here
			}
		}

		// TODO We wanna change how powers are granted, as cosmere library mod shouldn't be in charge of this
		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			if (manifestation.getManifestationType() == Manifestations.ManifestationTypes.SANDMASTERY)
			{
				final int ribbonCount = MathHelper.randomInt(1, 24);
				spiritwebCapability.giveManifestation(manifestation, ribbonCount);
				//Break here because there is only one attribute for ribbons.
				CosmereAPI.logger.info("Setting entity {} ribbons to {}", spiritwebCapability.getLiving().getName().getString(), ribbonCount);
				break;
			}
		}
	}


	@SubscribeEvent
	public static void onLivingTick(LivingEvent.LivingTickEvent event)
	{
		SpiritwebCapability.get(event.getEntity()).ifPresent(ISpiritweb::tick);
	}


	@SubscribeEvent
	public static void onLootingLevelEvent(LootingLevelEvent event)
	{
		if (event.getDamageSource() == null)
		{
			return;
		}
		if (!event.getEntity().level.isClientSide && event.getDamageSource().getEntity() instanceof LivingEntity sourceLiving)
		{
			int total = (int) EntityHelper.getAttributeValue(sourceLiving, AttributesRegistry.COSMERE_FORTUNE.getAttribute());
			if (total != 0)
			{
				event.setLootingLevel(event.getLootingLevel() + total);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event)
	{
		if (event.isCanceled())
		{
			return;
		}

		int total = (int) EntityHelper.getAttributeValue(event.getEntity(), AttributesRegistry.DETERMINATION.getAttribute());

		//take less damage when tapping
		//always reduce damage by something
		//always increase damage by something
		final int i = Math.abs(total) + 1;
		//never able to reduce by 100%
		// 76% ish max? eg tap10 / 13 = 0.76
		//store 3 is the max so never able to increase damage to self by more than 23%?
		// 23% ish max? eg store3 / 13 = 0.23
		final float v = i / 13f;
		// leaving 24%
		// 1 - 0.76 = 0.24
		// So we add 76% extra damage
		// 1 + 0.23 = 1.23
		final float v1 = total > 0 ? (1 + v) : (1 - v);

		//eg 7 damage at tap 10 would be:
		// 7 * 0.24 = 1.68 damage remaining
		//eg 7 damage at store 3 would be:
		// 7 * 1.23 = 8.61

		//basically never let them have more than 80% damage reduction
		//but also why not let them increase taking damage, that's fine.
		final float clampedPercentage = Mth.clamp(v1, 0.2f, 2);
		event.setAmount(event.getAmount() * clampedPercentage);//todo convert to config
	}

}
