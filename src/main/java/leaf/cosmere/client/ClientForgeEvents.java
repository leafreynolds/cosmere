/*
 * File updated ~ 12 - 11 - 2023 ~ Leaf
 * File updated ~ 5 - 2 - 2025 ~ SoaringEaqle
 */

package leaf.cosmere.client;

import com.mojang.blaze3d.platform.InputConstants;
import leaf.cosmere.api.Activator;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.client.gui.SpiritwebHud;
import leaf.cosmere.client.gui.SpiritwebMenu;
import leaf.cosmere.client.gui.SpiritwebRegistry;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.fog.FogManager;
import leaf.cosmere.common.network.packets.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
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
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, value = Dist.CLIENT)
public class ClientForgeEvents
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
	public static void onInput(InputEvent event)
	{
		final LocalPlayer player = Minecraft.getInstance().player;

		if (player == null)
		{
			return;
		}

		SpiritwebCapability.get(player).ifPresent(spiritweb ->
		{
			if (Keybindings.MANIFESTATION_MENU.consumeClick())
			{
				SpiritwebRegistry.getInstance().clear();
				SpiritwebCapability.get(player).ifPresent( (iSpiritweb ->
				{
					iSpiritweb.getSubmodules().forEach( ((manifestationTypes, iSpiritwebSubmodule) -> {
						iSpiritwebSubmodule.registerMenu();
					}));
				}));
				Minecraft.getInstance().setScreen(new SpiritwebMenu(Component.literal("Spiritweb Menu"), spiritweb));
			}

			Manifestation selected = spiritweb.getSelectedManifestation();
			if (Keybindings.MANIFESTATIONS_DEACTIVATE.consumeClick())
			{
				// just deactivate
				Cosmere.packetHandler().sendToServer(new DeactivateManifestationsMessage());
				//if all powers are deactivated, the power save state is off.

			}

			//check keybinds with modifiers first?
			if (Keybindings.MANIFESTATION_PREVIOUS.consumeClick())
			{
				Cosmere.packetHandler().sendToServer(new ChangeSelectedManifestationMessage(-1));
			}
			else if (Keybindings.MANIFESTATION_NEXT.consumeClick())
			{
				Cosmere.packetHandler().sendToServer(new ChangeSelectedManifestationMessage(1));
			}

			final boolean modeIncreasePressed = Keybindings.MANIFESTATION_MODE_INCREASE.consumeClick();
			final boolean modeDecreasedPressed = Keybindings.MANIFESTATION_MODE_DECREASE.consumeClick();

			if (modeIncreasePressed || modeDecreasedPressed)
			{
				int modifier;
				if (Screen.hasShiftDown())
				{
					modifier = 5;
				}
				else if (Screen.hasControlDown())
				{
					modifier = 10;
				}
				else
				{
					modifier = 1;
				}
				Cosmere.packetHandler().sendToServer(new ChangeManifestationModeMessage(selected, modeIncreasePressed ? modifier : -modifier));
			}

			for (Activator activator : Keybindings.activators)
			{
				if (activator.getKeyMapping().consumeClick())
				{
					Manifestation manifestation = activator.getManifestation();
					Cosmere.packetHandler().sendToServer(new SetSelectedManifestationMessage(manifestation));
					selected = manifestation;
					//not changing sandmastery mode because ribbon allotment. no need for the rest. might be implemented later.
					if (activator.getCategory().equals("sandmastery"))
					{
						break;
					}
					int modifier = -selected.getMode(spiritweb);

					//if inactive turn on
					if (!selected.isActive(spiritweb))
					{
						//if inactive and feruchemic ability tap 5
						//else level one
						modifier += activator.getCategory().equals("feruchemy")? -5: 1;
						Cosmere.packetHandler().sendToServer(new ChangeManifestationModeMessage(selected,modifier));
						spiritweb.getLiving().sendSystemMessage(Component.literal("Activated " +
								Component.translatable(selected.getTranslationKey())));

					}
					else
					{
                        Cosmere.packetHandler().sendToServer(new ChangeManifestationModeMessage(selected,modifier));
						spiritweb.getLiving().sendSystemMessage(Component.literal("Deactivated " +
								Component.translatable(selected.getTranslationKey())));
                    }
                }
			}

			//PowerSaveActivator/Saver
			boolean activateSave = Keybindings.ACTIVATE_POWER_SAVE.isDown();
			boolean savePowerState = Keybindings.SAVE_POWER_SAVE.isDown();
			if (activateSave || savePowerState)
			{
				for (ClientPowerSaveState.PowerSaves powerSave : ClientPowerSaveState.PowerSaves.values())
				{
					boolean numKeyPressed = Keybindings.getKey(powerSave.getNum()).consumeClick();
					if (numKeyPressed)
					{
						if (activateSave)
						{
							Cosmere.packetHandler().sendToServer(new TogglePowerStateMessage(powerSave.getNum()));
						}
						else if (savePowerState)
						{
							Cosmere.packetHandler().sendToServer(new SavePowerStateMessage(powerSave.getNum()));
						}
					}
				}
			}
		});
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
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

	@SubscribeEvent
	public static void onRenderGuiOverlayPost(RenderGuiOverlayEvent.Post event)
	{
		// make sure it only renders once per frame
		if (event.getOverlay().id().equals(VanillaGuiOverlay.HOTBAR.id()))
		{
			Minecraft mc = Minecraft.getInstance();
			ProfilerFiller profiler = mc.getProfiler();
			LocalPlayer playerEntity = mc.player;
			profiler.push("cosmere-spiritweb-hud");
			{
				SpiritwebCapability.get(playerEntity).ifPresent(spiritweb ->
				{
					// Shouldn't need mouse location, will only render as a HUD element
					spiritweb.getSpiritwebHud().render(event.getGuiGraphics(), 0, 0, event.getPartialTick());
				});
			}
			profiler.pop();
		}
	}


	@SubscribeEvent
	public static void onClientPlayerClone(ClientPlayerNetworkEvent.Clone event)
	{
		FogManager.resetFog();
	}
}
