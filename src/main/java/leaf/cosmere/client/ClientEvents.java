/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.client;

import com.mojang.blaze3d.systems.RenderSystem;
import leaf.cosmere.Cosmere;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.client.gui.SpiritwebMenu;
import leaf.cosmere.manifestation.feruchemy.FeruchemyAtium;
import leaf.cosmere.network.Network;
import leaf.cosmere.network.packets.ChangeManifestationModeMessage;
import leaf.cosmere.network.packets.ChangeSelectedManifestationMessage;
import leaf.cosmere.network.packets.DeactivateManifestationsMessage;
import leaf.cosmere.registry.KeybindingRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.InputEvent.MouseScrollEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, value = Dist.CLIENT)
public class ClientEvents
{

	@SubscribeEvent
	public static void handleScroll(MouseScrollEvent event)
	{
		final LocalPlayer player = Minecraft.getInstance().player;
		final ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);

		SpiritwebCapability.get(player).ifPresent(spiritweb ->
		{

			if (held.isEmpty() && player.isCrouching() && event.isRightDown())
			{
				final int delta = Mth.clamp((int) Math.round(event.getScrollDelta()), -1, 1);

				Network.sendToServer(new ChangeManifestationModeMessage(spiritweb.manifestation(), delta));

				event.setCanceled(true);

			}
		});
	}

	@SubscribeEvent
	public static void onKey(InputEvent.KeyInputEvent event)
	{
		final LocalPlayer player = Minecraft.getInstance().player;

		if (player == null)
		{
			return;
		}

		SpiritwebCapability.get(player).ifPresent(spiritweb ->
		{
			if (isKeyPressed(event, KeybindingRegistry.MANIFESTATIONS_DEACTIVATE))
			{
				//if crouching, only turn off.
				if (Screen.hasShiftDown())
				{
					Network.sendToServer(new DeactivateManifestationsMessage());
				}
				//otherwise do a normal toggle
				else
				{
					//todo decide if there is an activation state?
				}
			}

			//check keybinds with modifiers first?
			if (isKeyPressed(event, KeybindingRegistry.MANIFESTATION_PREVIOUS))
			{
				Network.sendToServer(new ChangeSelectedManifestationMessage(-1));
			}
			else if (isKeyPressed(event, KeybindingRegistry.MANIFESTATION_NEXT))
			{
				Network.sendToServer(new ChangeSelectedManifestationMessage(1));
			}

			final boolean modeIncreasePressed = isKeyPressed(event, KeybindingRegistry.MANIFESTATION_MODE_INCREASE);
			final boolean modeDecreasedPressed = isKeyPressed(event, KeybindingRegistry.MANIFESTATION_MODE_DECREASE);

			if (modeIncreasePressed || modeDecreasedPressed)
			{
				int dir;
				if (Screen.hasShiftDown())
				{
					dir = 5;
				}
				else if (Screen.hasControlDown())
				{
					dir = 10;
				}
				else
				{
					dir = 1;
				}
				Network.sendToServer(new ChangeManifestationModeMessage(spiritweb.manifestation(), dir * (modeIncreasePressed ? 1 : -1)));
			}
		});
	}

	private static boolean isKeyPressed(InputEvent.KeyInputEvent event, KeyMapping keyBinding)
	{
		return event.getKey() == keyBinding.getKey().getValue() && keyBinding.consumeClick();
	}

	@SubscribeEvent
	public static void onRenderGUI(final RenderGameOverlayEvent.Post event)
	{
		renderSpiritwebHUD(event);
	}

	@SubscribeEvent
	public static void onRenderLevelLastEvent(final RenderLevelLastEvent event)
	{
		renderManifestationsHUD(event);
	}

	public static void renderManifestationsHUD(final RenderLevelLastEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		ProfilerFiller profiler = mc.getProfiler();
		LocalPlayer playerEntity = mc.player;
		{
			profiler.push("cosmere-hud");

			if (Minecraft.getInstance().gameMode.canHurtPlayer())
			{
				SpiritwebCapability.get(playerEntity).ifPresent(spiritweb ->
				{
					profiler.push(spiritweb.manifestation().getName());
					spiritweb.renderWorldEffects(event);
					profiler.pop();

				});
			}
			profiler.pop();

			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		}

	}

	public static void renderSpiritwebHUD(final RenderGameOverlayEvent.Post event)
	{
		final RenderGameOverlayEvent.ElementType type = event.getType();
		if (type != RenderGameOverlayEvent.ElementType.ALL)
		{
			return;
		}

		SpiritwebCapability.get(Minecraft.getInstance().player).ifPresent(cap ->
		{
			SpiritwebCapability spiritweb = (SpiritwebCapability) cap;

			//if (!SpiritwebMenu.instance.isVisible())
			{
				spiritweb.renderSelectedHUD(event.getMatrixStack());
			}

			SpiritwebMenu.instance.postRender(event, spiritweb);
		});

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
