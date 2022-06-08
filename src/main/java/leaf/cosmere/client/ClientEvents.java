/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.client;

import com.mojang.blaze3d.systems.RenderSystem;
import leaf.cosmere.Cosmere;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.client.gui.SpiritwebMenu;
import leaf.cosmere.network.Network;
import leaf.cosmere.network.packets.ChangeManifestationModeMessage;
import leaf.cosmere.network.packets.ChangeSelectedManifestationMessage;
import leaf.cosmere.network.packets.DeactivateManifestationsMessage;
import leaf.cosmere.registry.KeybindingRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.MouseScrollEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLevelLastEvent;
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
			if (isKeyPressed(event, KeybindingRegistry.MANIFESTATION_DEACTIVATE))
			{
				//if crouching, only turn off.
				if (player.isCrouching() && spiritweb.canTickSelectedManifestation())
				{
					Network.sendToServer(new DeactivateManifestationsMessage());
				}
				//otherwise do a normal toggle
				else if (!player.isCrouching())
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


			if (isKeyPressed(event, KeybindingRegistry.MANIFESTATION_MODE_INCREASE))
			{
				int dir = (player.isCrouching() ? 3 : 1);
				Network.sendToServer(new ChangeManifestationModeMessage(spiritweb.manifestation(), dir));
			}
			if (isKeyPressed(event, KeybindingRegistry.MANIFESTATION_MODE_DECREASE))
			{
				int dir = (player.isCrouching() ? -3 : -1);
				Network.sendToServer(new ChangeManifestationModeMessage(spiritweb.manifestation(), dir));
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

}
