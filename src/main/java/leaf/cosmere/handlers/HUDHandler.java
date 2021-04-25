/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to Vazkii and their Mod Botania for providing the GUI render example!
 * https://github.com/Vazkii/Botania
 */

package leaf.cosmere.handlers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.profiler.IProfiler;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public final class HUDHandler
{

    private HUDHandler()
    {
    }


    public static void onDrawScreenPost(RenderGameOverlayEvent.Post event)
    {
        Minecraft mc = Minecraft.getInstance();
        IProfiler profiler = mc.getProfiler();
        ClientPlayerEntity playerEntity = mc.player;
        MatrixStack ms = event.getMatrixStack();

        if (event.getType() == ElementType.ALL)
        {
            profiler.startSection("cosmere-hud");

            if (Minecraft.getInstance().playerController.shouldDrawHUD())
            {
                SpiritwebCapability.get(playerEntity).ifPresent(spiritweb ->
                {
                    profiler.startSection(spiritweb.manifestation().getRegistryName().getNamespace());
                    //spiritweb.renderHUD(ms, playerEntity, spiritweb);
                    spiritweb.manifestation().renderHUD(ms, playerEntity, spiritweb);
                    profiler.endSection();

                });
            }
            profiler.endSection();

            RenderSystem.color4f(1F, 1F, 1F, 1F);
        }
    }
}
