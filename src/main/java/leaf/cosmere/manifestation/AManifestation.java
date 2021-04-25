/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation;

import com.mojang.blaze3d.matrix.MatrixStack;
import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Manifestations;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class AManifestation extends ForgeRegistryEntry<AManifestation>
{

    public abstract Manifestations.ManifestationTypes getManifestationType();

    public abstract int getPowerID();

    public abstract void onModeChange(ISpiritweb data);

    public abstract int modeMax(ISpiritweb data);

    public abstract int modeMin(ISpiritweb data);

    public abstract boolean modeWraps(ISpiritweb data);

    public abstract void tick(ISpiritweb data);

    public TranslationTextComponent translation()
    {
        ResourceLocation regName = getRegistryName();
        return new TranslationTextComponent("manifestation." + regName.getNamespace() + "." + regName.getPath());
    }

    public TranslationTextComponent description()
    {
        ResourceLocation regName = getRegistryName();
        return new TranslationTextComponent("manifestation." + regName.getNamespace() + "." + regName.getPath() + ".description");
    }

    public abstract void renderHUD(MatrixStack ms, ClientPlayerEntity playerEntity, ISpiritweb cap);

}