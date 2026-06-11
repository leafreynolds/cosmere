/*
 * File updated ~ 9 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.client;

import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.client.utils.FeruchemyChargeThread;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyAtium;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = Feruchemy.MODID, value = Dist.CLIENT)
public class FeruchemyClientEvents
{
	@SubscribeEvent
	public static void onRenderNameplateEvent(RenderNameTagEvent event)
	{
		if (!(event.getEntity() instanceof LivingEntity livingEntity))
		{
			return;
		}

		int connection = (int) EntityHelper.getAttributeValue(livingEntity, AttributesRegistry.CONNECTION.getHolder());
		if (connection <= -2)
		{
			event.setCanRender(TriState.FALSE);
		}

		final float atiumScale = FeruchemyAtium.getScale(livingEntity);
		if (atiumScale < 1)
		{
			double scale = atiumScale;
			event.getPoseStack().translate(0.0D, scale, 0.0D);
		}
	}

	@SubscribeEvent
	public static void onRenderPlayerPre(RenderPlayerEvent.Pre event)
	{
		try
		{
			float scale = FeruchemyAtium.getScale(event.getEntity());
			if (scale > 1.01 || scale < 0.99)
			{
				event.getPoseStack().pushPose();
				event.getPoseStack().scale(scale, scale, scale);
				if (event.getEntity().isCrouching() && scale < 0.2F)
				{
					event.getPoseStack().translate(0, 1.0, 0);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public static void onRenderPlayerPost(RenderPlayerEvent.Post event)
	{
		try
		{
			float scale = FeruchemyAtium.getScale(event.getEntity());
			if (scale > 1.01 || scale < 0.99)
			{
				event.getPoseStack().popPose();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public static void onLivingRenderPre(RenderLivingEvent.Pre event)
	{
		if (event.getEntity() instanceof Player)
		{
			return;
		}

		try
		{
			float scale = FeruchemyAtium.getScale(event.getEntity());
			if (scale > 1.01 || scale < 0.99)
			{
				event.getPoseStack().pushPose();
				event.getPoseStack().scale(scale, scale, scale);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public static void onLivingRenderPost(RenderLivingEvent.Post event)
	{
		if (event.getEntity() instanceof Player)
		{
			return;
		}

		try
		{
			float scale = FeruchemyAtium.getScale(event.getEntity());
			if (scale > 1.01 || scale < 0.99)
			{
				event.getPoseStack().popPose();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
