/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.utils;

import leaf.cosmere.allomancy.common.capabilities.AllomancySpiritwebSubmodule;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
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
					if (manifestation.getManifestationType() == Manifestations.ManifestationTypes.ALLOMANCY)
					{
						//todo config allomancy strength
						spiritweb.giveManifestation(manifestation, 13);
					}
				}
			}
			else
			{//add to metal stored
				//todo add metal to reserves
				final int addAmount = metalType.getAllomancyBurnTimeSeconds() * amount;
				AllomancySpiritwebSubmodule allo = (AllomancySpiritwebSubmodule) spiritweb.spiritwebSubmodules.get(Manifestations.ManifestationTypes.ALLOMANCY);
				allo.adjustIngestedMetal(metalType, addAmount, true);
			}

			spiritweb.syncToClients(null);

		});
	}


}
