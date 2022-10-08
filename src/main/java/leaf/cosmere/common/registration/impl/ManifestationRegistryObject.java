package leaf.cosmere.common.registration.impl;

import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.providers.IManifestationProvider;
import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class ManifestationRegistryObject<MANIFESTATION extends Manifestation> extends WrappedRegistryObject<MANIFESTATION> implements IManifestationProvider
{

	public ManifestationRegistryObject(RegistryObject<MANIFESTATION> registryObject)
	{
		super(registryObject);
	}

	@NotNull
	@Override
	public MANIFESTATION getManifestation()
	{
		return get();
	}
}