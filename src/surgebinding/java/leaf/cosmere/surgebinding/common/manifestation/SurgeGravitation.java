/*
 * File updated ~ 23 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.manifestation;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class SurgeGravitation extends SurgebindingManifestation
{
	public SurgeGravitation(Roshar.Surges surge)
	{
		super(surge);
	}

	//gravitational attraction


	public static void onLivingAttackEvent(LivingAttackEvent event)
	{
		if (event.getSource().getEntity() instanceof Player player && !event.getSource().isProjectile() && player.getMainHandItem().isEmpty())
		{
			SpiritwebCapability.get(player).ifPresent(iSpiritweb ->
			{
				//windrunners like Szeth could launch enemies into the air to die cruelly by fall damage
				if (iSpiritweb.hasManifestation(SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.GRAVITATION).get()))
				{
					final LivingEntity entity = event.getEntity();
					CosmereAPI.logger.info("%s has launched %s into the sky".formatted(player.getName().getString(), entity.getName().getString()));


					entity.stopRiding();
					entity.setPos(event.getEntity().getX(), event.getEntity().getY() + 0.1d, event.getEntity().getZ());
					entity.setOnGround(false);
					entity.setJumping(true);

					entity.setDeltaMovement(0, 50, 0);


					entity.hurtMarked = true;
				}
			});
		}


	}
}
