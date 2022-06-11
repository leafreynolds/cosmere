/*
 * File created ~ 11 - 6 - 2022 ~ Leaf
 */

package leaf.cosmere.handlers;

import leaf.cosmere.Cosmere;
import leaf.cosmere.manifestation.feruchemy.FeruchemyAtium;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FeruchemicalAtiumEventHandler
{
	@SubscribeEvent
	public static void changeSize(EntityEvent.Size event)
	{
		if (event.getEntity() instanceof LivingEntity livingEntity)
		{
			float scale = FeruchemyAtium.getScale(livingEntity);
			event.setNewSize(event.getNewSize().scale(scale));
			event.setNewEyeHeight(event.getNewEyeHeight() * scale);

		}
	}

	@SubscribeEvent
	public static void onRenderPlayerPre(RenderPlayerEvent.Pre event)
	{
		try
		{
			float scale = FeruchemyAtium.getScale(event.getEntityLiving());
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
			float scale = FeruchemyAtium.getScale(event.getEntityLiving());
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
