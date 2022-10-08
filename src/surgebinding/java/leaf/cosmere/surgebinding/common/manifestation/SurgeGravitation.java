/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.manifestation;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Roshar;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class SurgeGravitation extends SurgebindingManifestation
{
	public SurgeGravitation(Roshar.Surges surge)
	{
		super(surge);
	}

	//gravitational attraction


	//@SubscribeEvent
	public void onLivingHurtEvent(LivingHurtEvent event)
	{
		if (event.getSource().getEntity() instanceof Player player)
		{
			CosmereAPI.logger.info(player.getName() + " has attacked a " + event.getEntity().getName());
			//ISurgeState SState = player.getCapability(CosmereCapabilities.SURGE_STATE, null);

			//String activeSurges = SState.getActiveSurgeName();
			ItemStack itemInHand = player.getMainHandItem();

			//windrunners like Szeth could launch enemies into the air to die cruelly by fall damage
			if (false)//activeSurges.equals(Names.KnightOrders.WINDRUNNER) && itemInHand == null)
			{
				event.getEntity().setPos(event.getEntity().getX(), event.getEntity().getY() + 0.1d, event.getEntity().getZ());
				event.getEntity().setOnGround(false);
				event.getEntity().setJumping(true);

				event.getEntity().setDeltaMovement(0, 5, 0);

				//IPlayerStats PS = player.getCapability(CosmereCapabilities.PLAYER_STATS, null);
				//PS.remSL(100);//remove some stormlight as if it costs to hit
				//PacketDispatcher.sendTo(new SyncSurgeData(player.getCapability(CosmereCapabilities.SURGE_STATE, null), player.getCapability(CosmereCapabilities.PLAYER_STATS, null)), (PlayerEntityMP) player);
			}
		}


	}
}
