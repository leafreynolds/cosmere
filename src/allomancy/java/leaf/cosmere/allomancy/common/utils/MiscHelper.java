/*
 * File updated ~ 29 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.utils;

import leaf.cosmere.allomancy.common.capabilities.AllomancySpiritwebSubmodule;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Llama;

public class MiscHelper
{

	public static void consumeNugget(LivingEntity livingEntity, Metals.MetalType metalType, int amount)
	{
		if (metalType == null || livingEntity.level.isClientSide)
		{
			return;
		}

		SpiritwebCapability.get(livingEntity).ifPresent(iSpiritweb ->
		{
			SpiritwebCapability spiritweb = (SpiritwebCapability) iSpiritweb;

			if (metalType == Metals.MetalType.LERASIUM)
			{
				//https://www.theoryland.com/intvmain.php?i=977#43
				if (livingEntity instanceof Llama && !livingEntity.hasCustomName())
				{
					//todo translations
					livingEntity.setCustomName(TextHelper.createTranslatedText("Mistborn Llama"));
				}

				for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
				{
					//give allomancy
					final boolean isAllomancy = manifestation.getManifestationType() == Manifestations.ManifestationTypes.ALLOMANCY;
					final boolean notAtium = manifestation.getPowerID() != Metals.MetalType.ATIUM.getID();
					if (isAllomancy && notAtium)
					{
						//todo allomancy godmetal strength
						final double strength = manifestation.getStrength(iSpiritweb, true);
						spiritweb.giveManifestation(manifestation, strength < 13 ? 13 : (int) (strength + 1));
					}
				}
			}
			else if (metalType != Metals.MetalType.LERASATIUM)//ignore lerasatium, that's handled in feruchemy
			{
				//add to metal stored
				final int addAmount = metalType.getAllomancyBurnTimeSeconds() * amount;
				AllomancySpiritwebSubmodule allo = (AllomancySpiritwebSubmodule) spiritweb.getSubmodule(Manifestations.ManifestationTypes.ALLOMANCY);
				allo.adjustIngestedMetal(metalType, addAmount, true);
			}

			if (livingEntity instanceof ServerPlayer serverPlayer)
			{
				spiritweb.syncToClients(serverPlayer);
			}
		});
	}


}
