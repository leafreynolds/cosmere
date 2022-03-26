/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Manifestations;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class AManifestation extends ForgeRegistryEntry<AManifestation>
{

    public abstract Manifestations.ManifestationTypes getManifestationType();

    public abstract int getPowerID();

    public abstract void onModeChange(ISpiritweb data);

    public abstract int getMode(ISpiritweb data);

    public abstract int modeMax(ISpiritweb data);

    public abstract int modeMin(ISpiritweb data);

    public abstract boolean modeWraps(ISpiritweb data);

    public abstract void tick(ISpiritweb data);

    public abstract boolean isActive(ISpiritweb data);

    public abstract double getStrength(ISpiritweb data);

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

    @OnlyIn(Dist.CLIENT)
    public void renderWorldEffects(RenderWorldLastEvent event, ISpiritweb cap){}

}