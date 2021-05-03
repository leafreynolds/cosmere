/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to the Suff and their mod Regeneration.
 * That mod taught me how to add ticking capabilities to entities and have them sync
 * https://github.com/WhoCraft/Regeneration
 *
 */

package leaf.cosmere.manifestation;

import com.mojang.blaze3d.matrix.MatrixStack;
import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Manifestations;
import net.minecraft.block.Blocks;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

//Manifestation of investiture
public class ManifestationBase extends AManifestation
{
    final protected Manifestations.ManifestationTypes manifestationType;
    final protected int color;

    public ManifestationBase(Manifestations.ManifestationTypes manifestationType, int color)
    {
        this.manifestationType = manifestationType;
        this.color = color;
    }

    @Override
    public Manifestations.ManifestationTypes getManifestationType()
    {
        return manifestationType;
    }

    @Override
    public int getPowerID()
    {
        return 0;
    }

    @Override
    public void onModeChange(ISpiritweb data)
    {

    }

    @Override
    public int modeMax(ISpiritweb data)
    {
        return 0;
    }

    @Override
    public int modeMin(ISpiritweb data)
    {
        return 0;
    }

    @Override
    public boolean modeWraps(ISpiritweb data)
    {
        return false;
    }

    @Override
    public void tick(ISpiritweb data)
    {

    }

    //todo clean this up
    final ItemStack positiveActiveStack = new ItemStack(Blocks.SOUL_TORCH);
    final ItemStack negativeActiveStack = new ItemStack(Blocks.REDSTONE_TORCH);
    final ItemStack inactiveStack = new ItemStack(Items.STICK);

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderHUD(MatrixStack ms, ClientPlayerEntity playerEntity, ISpiritweb cap)
    {
        Minecraft mc = Minecraft.getInstance();
        MainWindow mainWindow = mc.getMainWindow();
        int x = 10;
        int y = mainWindow.getScaledHeight() / 5;

        //todo translations
        String stringToDraw = "Selected Power: " + I18n.format(this.translation().getString());
        mc.fontRenderer.drawStringWithShadow(ms, stringToDraw, x + 18, y, 0xFF4444);


        int mode = cap.getMode(manifestationType, this.getPowerID());
        String stringToDraw2 = "Mode: " + mode;
        mc.fontRenderer.drawStringWithShadow(ms, stringToDraw2, x + 18, y + 10, 0xFF4444);


        ItemStack stack;

        if (mode > 0)
            stack = positiveActiveStack;
        else if (mode < 0)
            stack = negativeActiveStack;
        else
            stack = inactiveStack;

        mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, x, y);
    }
}
