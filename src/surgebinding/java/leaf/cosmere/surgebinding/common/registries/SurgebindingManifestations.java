/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.registration.impl.ManifestationDeferredRegister;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.manifestation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SurgebindingManifestations
{
	public static final ManifestationDeferredRegister MANIFESTATIONS = new ManifestationDeferredRegister(Surgebinding.MODID);


	public static final Map<Roshar.Surges, ManifestationRegistryObject<Manifestation>> SURGEBINDING_POWERS =
			Arrays.stream(Roshar.Surges.values())
					.collect(Collectors.toMap(
							Function.identity(),
							surge ->
									MANIFESTATIONS.register(
											surge.getName(),
											() -> makeSurgebindingManifestation(surge))
					));


	private static SurgebindingManifestation makeSurgebindingManifestation(Roshar.Surges surge)
	{
		return switch (surge)
				{
					case ADHESION -> new SurgeAdhesion(surge);
					case GRAVITATION -> new SurgeGravitation(surge);
					case DIVISION -> new SurgeDivision(surge);
					case ABRASION -> new SurgeAbrasion(surge);
					case PROGRESSION -> new SurgeProgression(surge);
					case ILLUMINATION -> new SurgeIllumination(surge);
					case TRANSFORMATION -> new SurgeTransformation(surge);
					case TRANSPORTATION -> new SurgeTransportation(surge);
					case COHESION -> new SurgeCohesion(surge);
					case TENSION -> new SurgeTension(surge);
				};
	}
}
