/*
 * File updated ~ 9 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.utils;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.feruchemy.common.config.FeruchemyConfigs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class MiscHelper
{

	public static void consumeNugget(LivingEntity livingEntity, Metals.MetalType metalType)
	{
		if (metalType == null || livingEntity.level().isClientSide)
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
					//give feruchemy
					final boolean isFeruchemy = manifestation.getManifestationType() == Manifestations.ManifestationTypes.FERUCHEMY;
					final boolean notAtium = manifestation.getPowerID() != Metals.MetalType.ATIUM.getID();
					if (isFeruchemy && notAtium)//don't double up on improving electrum
					{
						final double strength = manifestation.getStrength(iSpiritweb, true);
						final int minimum = FeruchemyConfigs.SERVER.GOD_METAL_EAT_STRENGTH_MINIMUM.get();

						spiritweb.giveManifestation(manifestation, strength < minimum ? minimum : (int) (strength + 1));
					}
				}
			}

			if (livingEntity instanceof ServerPlayer serverPlayer)
			{
				spiritweb.syncToClients(serverPlayer);
			}

		});
	}


}
