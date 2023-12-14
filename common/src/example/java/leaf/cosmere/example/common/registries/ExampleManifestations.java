/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.example.common.registries;

import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.registration.impl.ManifestationDeferredRegister;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.example.common.Example;

public class ExampleManifestations
{
	public static final ManifestationDeferredRegister MANIFESTATIONS = new ManifestationDeferredRegister(Example.MODID);

	public static final ManifestationRegistryObject<Manifestation> EXAMPLE_MANIFESTATION = MANIFESTATIONS.register("example", Manifestation::new);

}
