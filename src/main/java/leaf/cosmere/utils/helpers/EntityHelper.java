/*
 * File created ~ 25 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
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
		AABB areaOfEffect = new AABB(entity.blockPosition());
		areaOfEffect = areaOfEffect.expandTowards(range, range, range);

		List<Entity> entitiesFound = entity.level.getEntitiesOfClass(Entity.class, areaOfEffect);

		if (!includeSelf)
		{
			entitiesFound.remove(entity);
		}

		return entitiesFound;
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
		int allomancyPower = MathHelper.randomInt(0, 15);
		int feruchemyPower = MathHelper.randomInt(0, 15);

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
					spiritwebCapability.giveManifestation(manifestation);
				}
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
			spiritwebCapability.giveManifestation(ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.valueOf(allomancyPower).get()).get());
			spiritwebCapability.giveManifestation(ManifestationRegistry.FERUCHEMY_POWERS.get(Metals.MetalType.valueOf(feruchemyPower).get()).get());

			if (!isPlayerEntity)
			{
				//todo translations
				//todo grant random name
				entity.setCustomName(TextHelper.createTranslatedText("Twinborn"));
			}
		}
		else
		{
			AManifestation manifestation =
					isAllomancy
					? ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.valueOf(allomancyPower).get()).get()
					: ManifestationRegistry.FERUCHEMY_POWERS.get(Metals.MetalType.valueOf(feruchemyPower).get()).get();

			spiritwebCapability.giveManifestation(manifestation);
			//todo translations
			//todo grant random name
			//entity.setCustomName(powerType.getManifestation(powerID).translation());
		}
	}
}
