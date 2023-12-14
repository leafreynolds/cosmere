/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.common.registries;

import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.registration.impl.ManifestationDeferredRegister;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.aviar.common.Aviar;

public class AviarManifestations
{
	public static final ManifestationDeferredRegister MANIFESTATIONS = new ManifestationDeferredRegister(Aviar.MODID);

	public static final ManifestationRegistryObject<Manifestation> AVIAR_MANIFESTATION = MANIFESTATIONS.register("aviar", Manifestation::new);

}
