/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.registries;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.manifestation.*;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registration.impl.ManifestationDeferredRegister;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AllomancyManifestations
{
	public static final ManifestationDeferredRegister MANIFESTATIONS = new ManifestationDeferredRegister(Allomancy.MODID);


	// Allomancy
	public static final Map<Metals.MetalType, ManifestationRegistryObject<AllomancyManifestation>> ALLOMANCY_POWERS =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasAssociatedManifestation)
					.collect(Collectors.toMap(
							Function.identity(),
							metalType ->
									MANIFESTATIONS.register(
											metalType.getName(),
											() -> makeAllomancyManifestation(metalType))//get the specific instance of the manifestation for allomancy
					));


	private static AllomancyManifestation makeAllomancyManifestation(Metals.MetalType metalType)
	{
		switch (metalType)
		{
			case IRON:
			case STEEL:
				return new AllomancyIronSteel(metalType);
			case TIN:
				return new AllomancyTin(metalType);
			case PEWTER://done?
				return new AllomancyPewter(metalType);
			case ZINC://done?
				return new AllomancyZinc(metalType);
			case BRASS://done?
				return new AllomancyBrass(metalType);
			case COPPER://done?
				return new AllomancyCopper(metalType);
			case BRONZE://done?
				return new AllomancyBronze(metalType);
			case ALUMINUM:// done?
				return new AllomancyAluminum(metalType);
			case DURALUMIN:
				return new AllomancyDuralumin(metalType);
			case CHROMIUM://done?
				return new AllomancyChromium(metalType);
			case NICROSIL:
				return new AllomancyNicrosil(metalType);
			case CADMIUM:
				return new AllomancyCadmium(metalType);
			case BENDALLOY://done?
				return new AllomancyBendalloy(metalType);
			case GOLD://shows your past //todo
				return new AllomancyGold(metalType);
			case ELECTRUM://shows your future // todo
				return new AllomancyElectrum(metalType);
			case ATIUM://prevents taking damage from others
				return new AllomancyAtium(metalType);
			case MALATIUM://shows other's past // todo
				return new AllomancyMalatium(metalType);
		}

		//others aren't valid allomancy manifestations... for now.
		return new AllomancyManifestation(metalType);
	}


}
