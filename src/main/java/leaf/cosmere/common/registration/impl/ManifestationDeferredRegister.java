package leaf.cosmere.common.registration.impl;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.registration.WrappedDeferredRegister;

import java.util.function.Supplier;

public class ManifestationDeferredRegister extends WrappedDeferredRegister<Manifestation>
{
	public ManifestationDeferredRegister(String modid)
	{
		super(modid, CosmereAPI.manifestationRegistryName());
	}


	public <MANIFESTATION extends Manifestation> ManifestationRegistryObject<MANIFESTATION> register(String name, Supplier<? extends MANIFESTATION> sup)
	{
		return register(name, sup, ManifestationRegistryObject::new);
	}
}
