/*
 * File updated ~ 27 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.registries;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.registration.impl.ManifestationDeferredRegister;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.manifestation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FeruchemyManifestations
{
	public static final ManifestationDeferredRegister MANIFESTATIONS = new ManifestationDeferredRegister(Feruchemy.MODID);


	// Feruchemy powers
	public static final Map<Metals.MetalType, ManifestationRegistryObject<Manifestation>> FERUCHEMY_POWERS =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasAssociatedManifestation)
					.collect(Collectors.toMap(
							Function.identity(),
							metalType ->
									MANIFESTATIONS.register(
											metalType.getName(),
											() -> makeFeruchemyManifestation(metalType))//feruchemy can be base manifestation type because it mostly functions through effects
					));

	private static FeruchemyManifestation makeFeruchemyManifestation(Metals.MetalType metalType)
	{
		switch (metalType)
		{
			case COPPER:
				return new FeruchemyCopper(metalType);
			case BRONZE:
				return new FeruchemyBronze(metalType);
			case ALUMINUM:
				return new FeruchemyAluminum(metalType);
			case NICROSIL:
				return new FeruchemyNicrosil(metalType);
			case GOLD:
				return new FeruchemyGold(metalType);
			case BENDALLOY:
				return new FeruchemyBendalloy(metalType);
			case ATIUM:
				return new FeruchemyAtium(metalType);
			default:
				return new FeruchemyManifestation(metalType);
		}
	}

}
