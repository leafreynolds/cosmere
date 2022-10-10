/*
 * File updated ~ 10 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.utils;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class MiscHelper
{

	public static void consumeNugget(LivingEntity livingEntity, Metals.MetalType metalType)
	{
		if (metalType == null || livingEntity.level.isClientSide)
		{
			return;
		}

		SpiritwebCapability.get(livingEntity).ifPresent(iSpiritweb ->
		{
			SpiritwebCapability spiritweb = (SpiritwebCapability) iSpiritweb;

			if (metalType == Metals.MetalType.LERASATIUM)
			{
				for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
				{
					//give allomancy
					if (manifestation.getManifestationType() == Manifestations.ManifestationTypes.FERUCHEMY)
					{
						//todo config allomancy strength
						spiritweb.giveManifestation(manifestation, 13);
					}
				}
			}

			spiritweb.syncToClients((ServerPlayer) livingEntity);

		});
	}


}
