/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.compat.hwyla;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.allomancy.AllomancyBase;
import leaf.cosmere.manifestation.allomancy.AllomancyBronze;
import leaf.cosmere.registry.EffectsRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import leaf.cosmere.utils.helpers.TextHelper;
import mcp.mobius.waila.api.EntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class SpiritWebTooltip implements IEntityComponentProvider
{
	static final SpiritWebTooltip INSTANCE = new SpiritWebTooltip();


	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig iPluginConfig)
	{
		final boolean playerCreativeMode = accessor.getPlayer().isCreative();
		//check the entity we are trying to
		SpiritwebCapability.get(accessor.getPlayer()).ifPresent(clientPlayer ->
		{
			AllomancyBronze allomancyBronze = (AllomancyBronze) ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.BRONZE).get();
			if (allomancyBronze.isMetalBurning(clientPlayer) || playerCreativeMode)
			{
				final double playerBronzeStrength = allomancyBronze.getStrength(clientPlayer, false);
				//check the entity we are trying to
				SpiritwebCapability.get((LivingEntity) accessor.getEntity()).ifPresent(targetSpiritweb ->
				{
					MobEffectInstance effect = targetSpiritweb.getLiving().getEffect(EffectsRegistry.ALLOMANTIC_COPPER.get());

					final double copperCloudStrength =
							effect!= null && effect.getDuration() > 0
							? effect.getAmplifier() : 0;

					if (!playerCreativeMode && (copperCloudStrength >= playerBronzeStrength))
					{
						return;
					}
					final boolean targetIsPlayer = targetSpiritweb.getLiving() instanceof Player;

					//show all manifestations, including hemalurgic based ones.
					for (AManifestation manifestation : targetSpiritweb.getAvailableManifestations())
					{
						if (!targetIsPlayer  || (!manifestation.isActive(targetSpiritweb)) && !playerCreativeMode)
						{
							continue;
						}
						tooltip.add(manifestation.translation());
					}
				});
			}
		});
	}
}
