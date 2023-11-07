/*
 * File updated ~ 23 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.client;

import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyAtium;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Feruchemy.MODID, value = Dist.CLIENT)
public class FeruchemyClientEvents
{
	@SubscribeEvent
	public static void onRenderNameplateEvent(RenderNameTagEvent event)
	{
		if (event.isCanceled() || !(event.getEntity() instanceof LivingEntity livingEntity))
		{
			return;
		}

		int connection = (int) EntityHelper.getAttributeValue(livingEntity, AttributesRegistry.CONNECTION.getAttribute());
		if (connection <= -2)
		{
			event.setResult(Event.Result.DENY);
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
}
