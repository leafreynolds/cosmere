/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.client;

import leaf.cosmere.Cosmere;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.client.gui.SpiritwebMenu;
import leaf.cosmere.network.Network;
import leaf.cosmere.network.packets.*;
import leaf.cosmere.registry.KeybindingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.MouseScrollEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, value = Dist.CLIENT)
public class ClientEvents
{

    @SubscribeEvent
    public static void handleScroll(MouseScrollEvent event)
    {
        final ClientPlayerEntity player = Minecraft.getInstance().player;
        final ItemStack held = player.getHeldItem(Hand.MAIN_HAND);

        SpiritwebCapability.get(player).ifPresent(spiritweb ->
        {

            if (held.isEmpty() && player.isCrouching() && event.isRightDown())
            {
                final int delta = MathHelper.clamp((int) Math.round(event.getScrollDelta()), -1, 1);

                Network.sendToServer(new ChangeManifestationModeMessage(spiritweb.manifestation().getManifestationType(), spiritweb.manifestation().getPowerID(), delta));

                event.setCanceled(true);

            }
        });
    }

    @SubscribeEvent
    public static void input(InputUpdateEvent event)
    {
        if (event.getMovementInput().jump)
        {
        }
    }

    @SubscribeEvent
    public static void onKey(InputEvent.KeyInputEvent event)
    {
        final ClientPlayerEntity player = Minecraft.getInstance().player;

        if (player == null)
        {
            return;
        }

        SpiritwebCapability.get(player).ifPresent(spiritweb ->
        {
            if (isKeyPressed(event, KeybindingRegistry.MANIFESTATION_TOGGLE))
            {
                //if crouching, only turn off.
                if (player.isCrouching() && spiritweb.selectedManifestationActive())
                {
                    Network.sendToServer(new DeactivateCurrentManifestationsMessage());
                }
                //otherwise do a normal toggle
                else if (!player.isCrouching())
                {
                    //todo decide if there is an activation state?
                }
            }
            //check keybinds with modifiers first?
            else if (isKeyPressed(event, KeybindingRegistry.MANIFESTATION_MODE_PREVIOUS))
            {
                if (spiritweb.hasSingleManifestation())
                {
                    Network.sendToServer(new ChangeManifestationModeMessage(spiritweb.manifestation().getManifestationType(), spiritweb.manifestation().getPowerID(), -1));
                }
                else
                {
                    Network.sendToServer(new ChangeSelectedManifestationMessage(-1));
                }
            }
            else if (isKeyPressed(event, KeybindingRegistry.MANIFESTATION_MODE_NEXT))
            {
                if (spiritweb.hasSingleManifestation())
                {
                    Network.sendToServer(new ChangeManifestationModeMessage(spiritweb.manifestation().getManifestationType(), spiritweb.manifestation().getPowerID(), 1));
                }
                else
                {
                    Network.sendToServer(new ChangeSelectedManifestationMessage(1));
                }
            }
        });
    }

    private static boolean isKeyPressed(InputEvent.KeyInputEvent event, KeyBinding keyBinding)
    {
        return event.getKey() == keyBinding.getKey().getKeyCode() && keyBinding.isPressed();
    }

    @SubscribeEvent
    public static void onRenderGUI(final RenderGameOverlayEvent.Post event)
    {
        SpiritwebCapability.get(Minecraft.getInstance().player).ifPresent(cap ->
        {
            SpiritwebCapability spiritweb = (SpiritwebCapability) cap;

            final RenderGameOverlayEvent.ElementType type = event.getType();
            if (type == RenderGameOverlayEvent.ElementType.ALL && spiritweb.getNumPowers() > 0)
            {
                SpiritwebMenu.instance.onPostRender(event, spiritweb);
            }
        });
    }

}
