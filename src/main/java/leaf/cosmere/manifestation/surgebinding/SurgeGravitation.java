/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.surgebinding;

import leaf.cosmere.constants.Roshar;
import leaf.cosmere.utils.helpers.LogHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

//@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SurgeGravitation extends SurgebindingBase
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
			LogHelper.info(player.getName() + " has attacked a " + event.getEntityLiving().getName());
			//ISurgeState SState = player.getCapability(CosmereCapabilities.SURGE_STATE, null);

			//String activeSurges = SState.getActiveSurgeName();
			ItemStack itemInHand = player.getMainHandItem();

			//windrunners like Szeth could launch enemies into the air to die cruelly by fall damage
			if (false)//activeSurges.equals(Names.KnightOrders.WINDRUNNER) && itemInHand == null)
			{
				event.getEntityLiving().setPos(event.getEntityLiving().getX(), event.getEntityLiving().getY() + 0.1d, event.getEntityLiving().getZ());
				event.getEntityLiving().setOnGround(false);
				event.getEntityLiving().setJumping(true);

				event.getEntityLiving().setDeltaMovement(0, 5, 0);

				//IPlayerStats PS = player.getCapability(CosmereCapabilities.PLAYER_STATS, null);
				//PS.remSL(100);//remove some stormlight as if it costs to hit
				//PacketDispatcher.sendTo(new SyncSurgeData(player.getCapability(CosmereCapabilities.SURGE_STATE, null), player.getCapability(CosmereCapabilities.PLAYER_STATS, null)), (PlayerEntityMP) player);
			}
		}


	}
}
