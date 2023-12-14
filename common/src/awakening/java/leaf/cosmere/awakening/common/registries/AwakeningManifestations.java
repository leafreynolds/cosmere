/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.awakening.common.registries;

import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.registration.impl.ManifestationDeferredRegister;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.awakening.common.Awakening;

public class AwakeningManifestations
{
	public static final ManifestationDeferredRegister MANIFESTATIONS = new ManifestationDeferredRegister(Awakening.MODID);

	public static final ManifestationRegistryObject<Manifestation> AWAKENING_MANIFESTATION = MANIFESTATIONS.register("awakening", Manifestation::new);

}
