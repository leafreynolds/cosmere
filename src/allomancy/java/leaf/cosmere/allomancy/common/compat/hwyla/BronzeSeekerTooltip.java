/*
 * File updated ~ 27 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.compat.hwyla;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.manifestation.AllomancyBronze;
import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class BronzeSeekerTooltip implements IEntityComponentProvider
{
	static final BronzeSeekerTooltip INSTANCE = new BronzeSeekerTooltip();

	@Override
	public ResourceLocation getUid()
	{
		return Allomancy.rl("bronze_seeker_tooltip");
	}

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig iPluginConfig)
	{
		final boolean playerCreativeMode = accessor.getPlayer().isCreative();
		//check the entity we are trying to
		SpiritwebCapability.get(accessor.getPlayer()).ifPresent(clientPlayer ->
		{
			AllomancyBronze allomancyBronze = (AllomancyBronze) AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.BRONZE).get();
			if (allomancyBronze.isMetalBurning(clientPlayer) || playerCreativeMode)
			{
				//check the entity we are trying to
				final LivingEntity targetEntity = (LivingEntity) accessor.getEntity();

				if (!AllomancyBronze.contestConcealment(clientPlayer, targetEntity))
				{
					return;
				}

				SpiritwebCapability.get(targetEntity).ifPresent(targetSpiritweb ->
				{
					final boolean targetIsPlayer = targetSpiritweb.getLiving() instanceof Player;

					//show all manifestations, including hemalurgic based ones.
					for (Manifestation manifestation : targetSpiritweb.getAvailableManifestations())
					{
						//if player is creative mode
						//or target is a mob
						//or player has this manifestation active
						if (playerCreativeMode || !targetIsPlayer || manifestation.isActive(targetSpiritweb))
						{
							tooltip.add(manifestation.getTextComponent());
						}
					}
				});
			}
		});
	}

}
