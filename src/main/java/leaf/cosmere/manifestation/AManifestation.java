/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import org.jetbrains.annotations.Nullable;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;

public abstract class AManifestation
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

	public ResourceLocation getResourceLocation()
	{
		return ManifestationRegistry.MANIFESTATION_REGISTRY.get().getKey(this);
	}

	public String translationKey()
	{
		ResourceLocation regName = getResourceLocation();
		return "manifestation." + regName.getNamespace() + "." + regName.getPath();
	}

	public MutableComponent translation()
	{
		return Component.translatable(translationKey());
	}

	public String descriptionKey()
	{
		ResourceLocation regName = getResourceLocation();
		return "manifestation." + regName.getNamespace() + "." + regName.getPath() + ".description";
	}

	public MutableComponent description()
	{
		return Component.translatable(descriptionKey());
	}

	@OnlyIn(Dist.CLIENT)
	public void renderWorldEffects(RenderLevelLastEvent event, ISpiritweb cap)
	{
	}

	public String getName()
	{
		ResourceLocation regName = ManifestationRegistry.MANIFESTATION_REGISTRY.get().getKey(this);

		return regName.getPath().toLowerCase(Locale.ROOT);
	}

	public abstract RegistryObject<Attribute> getAttribute();
}