/*
 * File created ~ 25 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.allomancy.AllomancyBase;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class EntityHelper
{
	public static List<LivingEntity> getLivingEntitiesInRange(LivingEntity selfEntity, int range, boolean includeSelf)
	{
		AABB areaOfEffect = new AABB(selfEntity.blockPosition());
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
		AABB areaOfEffect = new AABB(entity.blockPosition()).inflate(range, range, range);
		return entity.level.getEntitiesOfClass(Entity.class, areaOfEffect, e -> includeSelf || e != entity);
	}


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

			for (RegistryObject<AManifestation> power : ManifestationRegistry.MANIFESTATIONS.getEntries())
			{
				final AManifestation manifestation = power.get();
				final Manifestations.ManifestationTypes manifestationType = manifestation.getManifestationType();
				if ((isAllomancy && manifestationType == Manifestations.ManifestationTypes.ALLOMANCY)
						|| (!isAllomancy && manifestationType == Manifestations.ManifestationTypes.FERUCHEMY))
				{
					spiritwebCapability.giveManifestation(manifestation, 8);
				}
			}

			if (!isPlayerEntity)
			{
				/*//todo decide if we even want named entities

				//todo translations
				//todo grant random name
				entity.setCustomName(
						TextHelper.createTranslatedText(
								isAllomancy
								? "Mistborn"
								: "Feruchemist")
				);*/
			}
		}
		else
		{
			final AllomancyBase allomancyPower = ManifestationRegistry.ALLOMANCY_POWERS.get(allomancyMetal).get();
			final AManifestation feruchemyPower = ManifestationRegistry.FERUCHEMY_POWERS.get(feruchemyMetal).get();
			if (isTwinborn)
			{
				spiritwebCapability.giveManifestation(allomancyPower, 9);
				spiritwebCapability.giveManifestation(feruchemyPower, 9);

				if (!isPlayerEntity)
				{
/*//todo decide if we even want named entities
					//todo translations
					//todo grant random name
					entity.setCustomName(TextHelper.createTranslatedText("Twinborn"));
*/
				}
			}
			else
			{
				AManifestation manifestation =
						isAllomancy
						? allomancyPower
						: feruchemyPower;

				spiritwebCapability.giveManifestation(manifestation, 10);
				//todo translations
				//todo grant random name
				//entity.setCustomName(powerType.getManifestation(powerID).translation());

				if (!isPlayerEntity)
				{
/*//todo decide if we even want named entities
					//todo translations
					//todo grant random name
					final String s = StringHelper.fixCapitalisation(isAllomancy
					                 ? allomancyMetal.getMistingName()
					                 : feruchemyMetal.getFerringName());
					entity.setCustomName(TextHelper.createTranslatedText(
							s));*/
				}
			}
		}
	}
}
