/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aondor.common.registries;

import leaf.cosmere.aondor.common.AonDor;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.registration.impl.ManifestationDeferredRegister;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;

public class AonDorManifestations
{
	public static final ManifestationDeferredRegister MANIFESTATIONS = new ManifestationDeferredRegister(AonDor.MODID);

	public static final ManifestationRegistryObject<Manifestation> AONDOR_MANIFESTATION = MANIFESTATIONS.register("aondor", Manifestation::new);

}
