/*
 * File updated ~ 9 - 1 - 2025 ~ Leaf
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
import leaf.cosmere.common.config.CosmereServerConfig;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
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
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEventHandler
{

	@SubscribeEvent
	public static void onEntityJoinWorldEvent(EntityJoinLevelEvent event)
	{
		Entity eventEntity = event.getEntity();

		if (eventEntity.level().isClientSide || !(eventEntity instanceof LivingEntity livingEntity))
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
				if (CosmereConfigs.SERVER_CONFIG.POWER_GENERATION.get() == CosmereServerConfig.PowerGeneration.NONE)
				{
					// no powers
					spiritweb.setHasBeenInitialized();
				}
				else if (CosmereConfigs.SERVER_CONFIG.POWER_GENERATION.get() == CosmereServerConfig.PowerGeneration.RANDOM)
				{
					//give random power
					giveEntityStartingManifestation(livingEntity, spiritweb);
					spiritweb.setHasBeenInitialized();
				}
				else
				{
					// spiritweb not initialized yet in this case
					Player player = (Player) eventEntity;

					String command = "/cosmere choose_metalborn_powers ";
					MutableComponent instructionComponent = Component.literal("To choose powers, use ");
					instructionComponent.append(Component.literal("§6§n/cosmere choose_metalborn_powers [allomancy] [feruchemy]")
							.setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command))));

					player.sendSystemMessage(instructionComponent);
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

				spiritweb.setHasBeenInitialized();
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

				spiritweb.setHasBeenInitialized();
			}
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

		if (isPlayerEntity)
		{
			if (!MathHelper.chance(CosmereConfigs.SERVER_CONFIG.PLAYER_METALBORN_CHANCE.get()))
			{
				// if player isn't metalborn, no need to continue
				// a bit messy to do this but oh well, we want to change it anyway      // tech debt? what's that?
				addOtherPowers(spiritwebCapability);
				return;
			}
		}

		final Integer chanceOfFullPowers = CosmereConfigs.SERVER_CONFIG.FULLBORN_POWERS_CHANCE.get();
		final Integer chanceOfTwinborn = isPlayerEntity ? CosmereConfigs.SERVER_CONFIG.TWINBORN_POWERS_CHANCE_PLAYER.get() : CosmereConfigs.SERVER_CONFIG.TWINBORN_POWERS_CHANCE_MOB.get();
		//low chance of having full powers of one type
		//0-15 inclusive is normal powers.
		boolean isFullPowersFromOneType = MathHelper.chance(chanceOfFullPowers);

		//small chance of being twin born, but only if not having full powers above
		//except for players who are guaranteed having at least two powers.
		boolean isTwinborn = MathHelper.chance(chanceOfTwinborn);

		//randomise the given powers from allomancy and feruchemy
		int allomancyPowerID = MathHelper.randomInt(0, 15);
		int feruchemyPowerID = MathHelper.randomInt(0, 15);

		final Metals.MetalType allomancyMetal = Metals.MetalType.valueOf(allomancyPowerID).get();
		final Metals.MetalType feruchemyMetal = Metals.MetalType.valueOf(feruchemyPowerID).get();

		final boolean allomancyLoaded = ModList.get().isLoaded("allomancy");
		final boolean feruchemyLoaded = ModList.get().isLoaded("feruchemy");

		//if not twinborn, pick one power
		boolean isAllomancy = MathHelper.randomBool();

		if (isFullPowersFromOneType)
		{
			//ooh full powers

			final Manifestations.ManifestationTypes manifestationType;
			if (allomancyLoaded && feruchemyLoaded)
			{
				manifestationType = isAllomancy
									? Manifestations.ManifestationTypes.ALLOMANCY
									: Manifestations.ManifestationTypes.FERUCHEMY;
			}
			else if (allomancyLoaded)
			{
				manifestationType = Manifestations.ManifestationTypes.ALLOMANCY;
			}
			else if (feruchemyLoaded)
			{
				manifestationType = Manifestations.ManifestationTypes.FERUCHEMY;
			}
			else
			{
				// ...why are we here?
				return;
			}


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
				if (allomancyLoaded)
				{
					spiritwebCapability.giveManifestation(allomancyPower, 9);
					if (spiritwebCapability.getLiving() instanceof Player player)
					{
						spiritwebCapability.getSubmodule(Manifestations.ManifestationTypes.ALLOMANCY).GiveStartingItem(player, allomancyPower);
					}
					CosmereAPI.logger.info(
							"Entity {} has been granted allomantic {}!",
							spiritwebCapability.getLiving().getName().getString(),
							allomancyMetal);
				}
				if (feruchemyLoaded)
				{
					spiritwebCapability.giveManifestation(feruchemyPower, 9);

					if (spiritwebCapability.getLiving() instanceof Player player)
					{
						spiritwebCapability.getSubmodule(Manifestations.ManifestationTypes.FERUCHEMY).GiveStartingItem(player, feruchemyPower);
					}
					CosmereAPI.logger.info(
						"Entity {} has been granted feruchemical {}!",
						spiritwebCapability.getLiving().getName().getString(),
						feruchemyMetal);
				}
			}
			else
			{
				Manifestation manifestation;
				isAllomancy = isPlayerEntity ? MathHelper.randomInt(0, 99) < CosmereConfigs.SERVER_CONFIG.PLAYER_MISTING_TO_FERRING_DISTRIBUTION.get() : MathHelper.randomBool();
				if (allomancyLoaded && feruchemyLoaded)
				{
					manifestation =
						isAllomancy
						? allomancyPower
						: feruchemyPower;
				}
				else if (allomancyLoaded)
				{
					manifestation = allomancyPower;
				}
				else if (feruchemyLoaded)
				{
					manifestation = feruchemyPower;
				}
				else
				{
					// again, why are we here?
					return;
				}

				spiritwebCapability.giveManifestation(manifestation, 9);
				if (spiritwebCapability.getLiving() instanceof Player player)
				{
					spiritwebCapability.getSubmodule(isAllomancy ? Manifestations.ManifestationTypes.ALLOMANCY : Manifestations.ManifestationTypes.FERUCHEMY).GiveStartingItem(player, manifestation);
				}
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
		addOtherPowers(spiritwebCapability);
	}

	public static void addOtherPowers(SpiritwebCapability spiritwebCapability)
	{
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
		if (!event.getEntity().level().isClientSide && event.getDamageSource().getEntity() instanceof LivingEntity sourceLiving)
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

		float total = (float) EntityHelper.getAttributeValue(event.getEntity(), AttributesRegistry.DETERMINATION.getAttribute());

		//ignore if no determination changes, players default to 0
		//should we todo config this?
		if (total > 0.1)
		{
			final float maxDetermination = 23.125f;//can we detect this properly? not really
			final float percentageOfMaxDetermination = total / maxDetermination;

			//increase damage reduction
			float damageReduction = 1 - Mth.lerp(percentageOfMaxDetermination, 0, 0.80f);
			event.setAmount(event.getAmount() * damageReduction);
		}
		else if (total < -0.1)
		{
			//increase damage taken
			final float minDetermination = 3f;//can we detect this properly? not really
			final float percentageOfMinDetermination = Math.abs(total) / minDetermination;
			float damageIncrease = Mth.lerp(percentageOfMinDetermination, 1, 1.25f);
			event.setAmount(event.getAmount() * damageIncrease);
		}
	}
}
