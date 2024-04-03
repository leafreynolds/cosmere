/*
 * File updated ~ 22 - 3 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.common.registries;

import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.registration.impl.ManifestationDeferredRegister;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.tools.common.CosmereTools;

public class ToolsManifestations
{
	public static final ManifestationDeferredRegister MANIFESTATIONS = new ManifestationDeferredRegister(CosmereTools.MODID);

	public static final ManifestationRegistryObject<Manifestation> TOOLS_MANIFESTATION = MANIFESTATIONS.register("tools", Manifestation::new);

}
