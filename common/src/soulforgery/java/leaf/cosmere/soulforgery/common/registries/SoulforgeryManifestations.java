/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.soulforgery.common.registries;

import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.registration.impl.ManifestationDeferredRegister;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.soulforgery.common.Soulforgery;

public class SoulforgeryManifestations
{
	public static final ManifestationDeferredRegister MANIFESTATIONS = new ManifestationDeferredRegister(Soulforgery.MODID);

	public static final ManifestationRegistryObject<Manifestation> SOULFORGERY_MANIFESTATION = MANIFESTATIONS.register("soulforgery", Manifestation::new);

}
