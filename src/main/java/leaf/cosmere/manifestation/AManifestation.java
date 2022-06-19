/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Manifestations;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Locale;

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

	protected abstract void applyEffectTick(ISpiritweb data);

	public abstract boolean isActive(ISpiritweb data);

	public abstract double getStrength(ISpiritweb data, boolean getBaseStrength);

	public TranslatableComponent translation()
	{
		ResourceLocation regName = getRegistryName();
		return new TranslatableComponent("manifestation." + regName.getNamespace() + "." + regName.getPath());
	}

	public TranslatableComponent description()
	{
		ResourceLocation regName = getRegistryName();
		return new TranslatableComponent("manifestation." + regName.getNamespace() + "." + regName.getPath() + ".description");
	}

	@OnlyIn(Dist.CLIENT)
	public void renderWorldEffects(RenderLevelLastEvent event, ISpiritweb cap)
	{
	}

	public String getName()
	{
		return this.getRegistryName().getPath().toLowerCase(Locale.ROOT);
	}
}