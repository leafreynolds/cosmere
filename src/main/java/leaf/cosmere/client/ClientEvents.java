/*
 * File updated ~ 10 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.client;

import leaf.cosmere.client.gui.SpiritwebMenu;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.fog.FogManager;
import leaf.cosmere.common.network.packets.ChangeManifestationModeMessage;
import leaf.cosmere.common.network.packets.ChangeSelectedManifestationMessage;
import leaf.cosmere.common.network.packets.DeactivateManifestationsMessage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.MouseScrollingEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, value = Dist.CLIENT)
public class ClientEvents
{

	@SubscribeEvent
	public static void handleScroll(MouseScrollingEvent event)
	{
		final LocalPlayer player = Minecraft.getInstance().player;
		final ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);

		SpiritwebCapability.get(player).ifPresent(spiritweb ->
		{

			if (held.isEmpty() && player.isCrouching() && event.isRightDown())
			{
				final int delta = Mth.clamp((int) Math.round(event.getScrollDelta()), -1, 1);

				Cosmere.packetHandler().sendToServer(new ChangeManifestationModeMessage(spiritweb.getSelectedManifestation(), delta));

				event.setCanceled(true);

			}
		});
	}

	@SubscribeEvent
	public static void onKey(InputEvent.Key event)
	{
		final LocalPlayer player = Minecraft.getInstance().player;

		if (player == null)
		{
			return;
		}

		SpiritwebCapability.get(player).ifPresent(spiritweb ->
		{
			if (isKeyPressed(event, Keybindings.MANIFESTATIONS_DEACTIVATE))
			{
				//if crouching, only turn off.
				if (Screen.hasShiftDown())
				{
					Cosmere.packetHandler().sendToServer(new DeactivateManifestationsMessage());
				}
				//otherwise do a normal toggle
				else
				{
					//todo decide if there is an activation state?
				}
			}

			//check keybinds with modifiers first?
			if (isKeyPressed(event, Keybindings.MANIFESTATION_PREVIOUS))
			{
				Cosmere.packetHandler().sendToServer(new ChangeSelectedManifestationMessage(-1));
			}
			else if (isKeyPressed(event, Keybindings.MANIFESTATION_NEXT))
			{
				Cosmere.packetHandler().sendToServer(new ChangeSelectedManifestationMessage(1));
			}

			final boolean modeIncreasePressed = isKeyPressed(event, Keybindings.MANIFESTATION_MODE_INCREASE);
			final boolean modeDecreasedPressed = isKeyPressed(event, Keybindings.MANIFESTATION_MODE_DECREASE);

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
				Cosmere.packetHandler().sendToServer(new ChangeManifestationModeMessage(spiritweb.getSelectedManifestation(), dir * (
						modeIncreasePressed ? 1 : -1)));
			}
		});
	}

	private static boolean isKeyPressed(InputEvent.Key event, KeyMapping keyBinding)
	{
		return event.getKey() == keyBinding.getKey().getValue() && keyBinding.consumeClick();
	}

	@SubscribeEvent
	public static void onRenderGUI(final RenderGuiOverlayEvent.Post event)
	{
		renderSpiritwebHUD(event);
	}

	@SubscribeEvent
	public static void onRenderLevelLastEvent(final RenderLevelStageEvent event)
	{
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES)
		{
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		ProfilerFiller profiler = mc.getProfiler();
		LocalPlayer playerEntity = mc.player;
		{
			profiler.push("cosmere-world-effects");
			{
				SpiritwebCapability.get(playerEntity).ifPresent(spiritweb ->
				{
					spiritweb.renderWorldEffects(event);
				});
			}
			profiler.pop();
		}

	}

	public static void renderSpiritwebHUD(final RenderGuiOverlayEvent.Post event)
	{
		SpiritwebCapability.get(Minecraft.getInstance().player).ifPresent(cap ->
		{
			SpiritwebCapability spiritweb = (SpiritwebCapability) cap;

			//normal hud stuff
			spiritweb.renderSelectedHUD(event.getPoseStack());

			//actual menu stuff
			SpiritwebMenu.instance.postRender(event, spiritweb);
		});

	}

	@SubscribeEvent
	public static void onClientPlayerClone(ClientPlayerNetworkEvent.Clone event)
	{
		FogManager.resetFog();
	}
}
