/*
 * File updated ~ 3 - 6 - 2023 ~ Leaf
 */

package leaf.cosmere.api.manifestation;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.providers.IManifestationProvider;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class Manifestation implements IManifestationProvider
{
	final protected Manifestations.ManifestationTypes manifestationType;

	public Manifestation()
	{
		this.manifestationType = Manifestations.ManifestationTypes.NONE;
	}

	public Manifestation(Manifestations.ManifestationTypes manifestationType)
	{
		this.manifestationType = manifestationType;
	}

	public Manifestations.ManifestationTypes getManifestationType()
	{
		return manifestationType;
	}

	public int getPowerID()
	{
		return 0;
	}

	public void onModeChange(ISpiritweb data, int lastMode)
	{

	}

	public int getMode(ISpiritweb data)
	{
		return data.getMode(this);
	}

	public int modeMax(ISpiritweb data)
	{
		return 0;
	}

	public int modeMin(ISpiritweb data)
	{
		return 0;
	}

	public boolean modeWraps(ISpiritweb data)
	{
		return false;
	}

	/**
	 * @param data : spiritweb of whatever entity that's using this manifestation
	 * @return true if this manifestation should trigger a {@link net.minecraft.world.level.gameevent.GameEvent} for sculk sensors and wardens to pick up
	 * @see leaf.cosmere.common.cap.entity.SpiritwebCapability#tick()
	 */
	@SuppressWarnings("JavadocReference")
	public boolean tick(ISpiritweb data)
	{
		return false;
	}

	protected void applyEffectTick(ISpiritweb data)
	{

	}

	public boolean isActive(ISpiritweb data)
	{
		return data.canTickManifestation(this);
	}

	public double getStrength(ISpiritweb cap, boolean getBaseStrength)
	{
		AttributeInstance attribute = cap.getLiving().getAttribute(getAttribute());
		if (attribute != null)
		{
			return getBaseStrength ? attribute.getBaseValue() : attribute.getValue();
		}
		return 0;
	}

	@Override
	public ResourceLocation getRegistryName()
	{
		//May be null if called before the object is registered
		IForgeRegistry<Manifestation> registry = CosmereAPI.manifestationRegistry();
		return registry == null ? null : registry.getKey(this);
	}

	@Override
	public String getTranslationKey()
	{
		ResourceLocation regName = getRegistryName();
		return "manifestation." + regName.getNamespace() + "." + regName.getPath();
	}

	@Override
	public Manifestation getManifestation()
	{
		return this;
	}


	public Attribute getAttribute()
	{
		return ForgeRegistries.ATTRIBUTES.getValue(getRegistryName());
	}
}