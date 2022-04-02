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
import leaf.cosmere.network.packets.DeactivateCurrentManifestationsMessage;
import leaf.cosmere.registry.KeybindingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.MouseScrollEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, value = Dist.CLIENT)
public class ClientEvents
{

    @SubscribeEvent
    public static void handleScroll(MouseScrollEvent event)
    {
        final ClientPlayerEntity player = Minecraft.getInstance().player;
        final ItemStack held = player.getItemInHand(Hand.MAIN_HAND);

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
        if (event.getMovementInput().jumping)
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
                if (player.isCrouching() && spiritweb.canTickSelectedManifestation())
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
                Network.sendToServer(new ChangeManifestationModeMessage(spiritweb.manifestation().getManifestationType(), spiritweb.manifestation().getPowerID(), dir));
            }
            if (isKeyPressed(event, KeybindingRegistry.MANIFESTATION_MODE_DECREASE))
            {
                int dir = (player.isCrouching() ? -3 : -1);
                Network.sendToServer(new ChangeManifestationModeMessage(spiritweb.manifestation().getManifestationType(), spiritweb.manifestation().getPowerID(), dir));
            }
        });
    }

    private static boolean isKeyPressed(InputEvent.KeyInputEvent event, KeyBinding keyBinding)
    {
        return event.getKey() == keyBinding.getKey().getValue() && keyBinding.consumeClick();
    }

    @SubscribeEvent
    public static void onRenderGUI(final RenderGameOverlayEvent.Post event)
    {
        renderSpiritwebHUD(event);
    }

    @SubscribeEvent
    public static void onRenderWorldLastEvent(final RenderWorldLastEvent event)
    {
        renderManifestationsHUD(event);
    }

    public static void renderManifestationsHUD(final RenderWorldLastEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        IProfiler profiler = mc.getProfiler();
        ClientPlayerEntity playerEntity = mc.player;
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

            RenderSystem.color4f(1F, 1F, 1F, 1F);
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
            spiritweb.renderSelectedHUD(event.getMatrixStack());

            if (spiritweb.hasAnyPowers())
            {
                SpiritwebMenu.instance.postRender(event, spiritweb);
            }
        });

    }

}
