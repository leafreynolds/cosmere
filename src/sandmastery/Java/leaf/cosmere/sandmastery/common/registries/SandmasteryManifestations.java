/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.registries;

import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.registration.impl.ManifestationDeferredRegister;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.manifestation.SandmasteryManifestation;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SandmasteryManifestations
{
	public static final ManifestationDeferredRegister MANIFESTATIONS = new ManifestationDeferredRegister(Sandmastery.MODID);

	public static final Map<Taldain.Investiture, ManifestationRegistryObject<Manifestation>> SANDMASTERY_POWERS =
			Arrays.stream(Taldain.Investiture.values())
					.collect(Collectors.toMap(
							Function.identity(),
							investiture ->
									MANIFESTATIONS.register(
											investiture.getName(),
											() -> makeSandmasteryManifestation(investiture))
					));


	private static SandmasteryManifestation makeSandmasteryManifestation(Taldain.Investiture investiture)
	{
		return new SandmasteryManifestation(investiture);
	}
}
