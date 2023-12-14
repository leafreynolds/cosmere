/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.common.registry;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registration.impl.ManifestationDeferredRegister;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import net.minecraft.resources.ResourceLocation;

public class ManifestationRegistry
{
	public static final ManifestationDeferredRegister MANIFESTATIONS = new ManifestationDeferredRegister(Cosmere.MODID);

	public static final ManifestationRegistryObject<Manifestation> NONE = MANIFESTATIONS.register("none", Manifestation::new);

	public static Manifestation fromID(String location)
	{
		ResourceLocation resourceLocation = new ResourceLocation(location);
		return fromID(resourceLocation);
	}

	public static Manifestation fromID(ResourceLocation location)
	{
		Manifestation value = CosmereAPI.manifestationRegistry().getValue(location);
		if (value != null)
		{
			return value;
		}
		return ManifestationRegistry.NONE.get();
	}

}
