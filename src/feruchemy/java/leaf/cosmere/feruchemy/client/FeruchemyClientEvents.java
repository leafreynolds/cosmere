/*
 * File updated ~ 9 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.client;

import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.client.utils.FeruchemyChargeThread;
import leaf.cosmere.feruchemy.common.Feruchemy;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = Feruchemy.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class FeruchemyClientEvents
{
	@SubscribeEvent
	public static void onRenderNameplateEvent(RenderNameTagEvent event)
	{
		if (!(event.getEntity() instanceof LivingEntity livingEntity))
		{
			return;
		}

		int connection = (int) EntityHelper.getAttributeValue(livingEntity, AttributesRegistry.CONNECTION.getAttribute());
		if (connection <= -2)
		{
			event.setCanRender(TriState.FALSE);
		}
	}

	@SubscribeEvent
	public static void onEntityJoinLevelEvent(EntityJoinLevelEvent event)
	{
		if (event.getEntity().level().isClientSide && event.getEntity() instanceof Player)
		{
			FeruchemyChargeThread.getInstance().start();
		}
	}
}
