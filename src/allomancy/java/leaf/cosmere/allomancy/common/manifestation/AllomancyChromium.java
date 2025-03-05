/*
 * File updated ~ 5 - 3 - 2025 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.cosmereEffect.CosmereEffectInstance;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.registry.CosmereEffectsRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class AllomancyChromium extends AllomancyManifestation
{
	public AllomancyChromium(Metals.MetalType metalType)
	{
		super(metalType);
	}

	public static void onLivingHurtEvent(LivingHurtEvent event)
	{
		if (event.isCanceled())
		{
			return;
		}

		Entity trueSource = event.getSource().getEntity();
		LivingEntity targetEntity = event.getEntity();
		if (trueSource instanceof Player trueSourcePlayer)
		{
			SpiritwebCapability.get(trueSourcePlayer).ifPresent(player ->
			{
				final AllomancyManifestation aChromium = AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.CHROMIUM).get();
				if (aChromium.isActive(player))
				{
					SpiritwebCapability.get(targetEntity).ifPresent(targetISpiritweb ->
					{
						final SpiritwebCapability targetSpiritweb = (SpiritwebCapability) targetISpiritweb;
						if (targetSpiritweb.getLiving() != null)
						{
							/*AllomancySpiritwebSubmodule allo = (AllomancySpiritwebSubmodule) targetSpiritweb.getSubmodule(Manifestations.ManifestationTypes.ALLOMANCY);
							for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
							{
								float drainAmount = allo.getIngestedMetal(metalType) * 0.1f;
								allo.adjustIngestedMetal(metalType, (int) -drainAmount, true);
							}*/

							CosmereEffectInstance newEffect = EffectsHelper.getNewEffect(
									CosmereEffectsRegistry.DRAIN_INVESTITURE.get(),
									targetEntity,
									(aChromium.getStrength(player, false))
							);

							targetSpiritweb.addEffect(newEffect, trueSourcePlayer);
						}
					});
				}
			});
		}
	}
}
