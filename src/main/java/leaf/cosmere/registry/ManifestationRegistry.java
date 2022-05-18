/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.registry;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.constants.Roshar;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.ManifestationBase;
import leaf.cosmere.manifestation.allomancy.*;
import leaf.cosmere.manifestation.feruchemy.*;
import leaf.cosmere.manifestation.surgebinding.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ManifestationRegistry
{
	public static final DeferredRegister<AManifestation> MANIFESTATIONS = DeferredRegister.create(ResourceKeyRegistry.MANIFESTATION_TYPES, Cosmere.MODID);
	public static Supplier<IForgeRegistry<AManifestation>> MANIFESTATION_REGISTRY = MANIFESTATIONS.makeRegistry(AManifestation.class, () -> new RegistryBuilder<AManifestation>().setMaxID(Integer.MAX_VALUE - 1));

	public static final RegistryObject<AManifestation> NONE = MANIFESTATIONS.register("none", () -> new ManifestationBase(Manifestations.ManifestationTypes.NONE, Color.WHITE.getRGB()));

	// Allomancy
	public static final Map<Metals.MetalType, RegistryObject<AllomancyBase>> ALLOMANCY_POWERS =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasAssociatedManifestation)
					.collect(Collectors.toMap(
							Function.identity(),
							metalType ->
									MANIFESTATIONS.register(
											metalType.getAllomancyRegistryName(),
											() -> makeAllomancyManifestation(metalType))//get the specific instance of the manifestation for allomancy
					));

	// Feruchemy powers
	public static final Map<Metals.MetalType, RegistryObject<AManifestation>> FERUCHEMY_POWERS =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasAssociatedManifestation)
					.collect(Collectors.toMap(
							Function.identity(),
							metalType ->
									MANIFESTATIONS.register(
											metalType.getFeruchemyRegistryName(),
											() -> makeFeruchemyManifestation(metalType))//feruchemy can be base manifestation type because it mostly functions through effects
					));

	// AonDor
	//todo AonDor
	//public static final RegistryObject<AbstractInvestitureManifestation> ELANTRIAN = MANIFESTATIONS.register(Constants.Manifestations.AonDor.ELANTRIAN, () -> new InvestitureManifestationBase(Color.WHITE.getRGB()));

	// Biochroma
	//todo biochroma
	//public static final RegistryObject<AbstractInvestitureManifestation> BIOCHROMA = MANIFESTATIONS.register(Constants.Manifestations.Biochroma.AWAKENING, () -> new InvestitureManifestationBase(Color.WHITE.getRGB()));

	// Surgebinder
	//todo knights radiant

	public static final Map<Roshar.Surges, RegistryObject<AManifestation>> SURGEBINDING_POWERS =
			Arrays.stream(Roshar.Surges.values())
					.collect(Collectors.toMap(
							Function.identity(),
							surge ->
									MANIFESTATIONS.register(
											surge.getName(),
											() -> makeSurgebindingManifestation(surge))//get the specific instance of the manifestation for allomancy
					));

	//Create Registry
	public static AManifestation fromID(String location)
	{
		ResourceLocation resourceLocation = new ResourceLocation(location);
		return fromID(resourceLocation);
	}

	public static AManifestation fromID(ResourceLocation location)
	{
		AManifestation value = MANIFESTATION_REGISTRY.get().getValue(location);
		if (value != null)
		{
			return value;
		}
		return ManifestationRegistry.NONE.get();
	}


	private static AllomancyBase makeAllomancyManifestation(Metals.MetalType metalType)
	{
		switch (metalType)
		{
			case IRON://todo pull and iron sight
			case STEEL://todo push and steel sight
				return new AllomancyIronSteel(metalType);
			case TIN://todo more than just night vision?
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
			case DURALUMIN://todo decide how to enhance other metals
				return new AllomancyDuralumin(metalType);
			case CHROMIUM://done?
				return new AllomancyChromium(metalType);
			case NICROSIL://todo decide how to enhance other metals
				return new AllomancyNicrosil(metalType);
			case CADMIUM://todo decide how to slow down time
				return new AllomancyCadmium(metalType);
			case BENDALLOY://done?
				return new AllomancyBendalloy(metalType);
			case GOLD://shows your past //todo
				return new AllomancyGold(metalType);
			case ELECTRUM://shows your future // todo
				return new AllomancyElectrum(metalType);
		}

		//others aren't valid allomancy manifestations... for now.
		return new AllomancyBase(metalType);
	}


	private static FeruchemyBase makeFeruchemyManifestation(Metals.MetalType metalType)
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
			case ELECTRUM:
				return new FeruchemyElectrum(metalType);
			default:
				return new FeruchemyBase(metalType);
		}
	}

	private static SurgebindingBase makeSurgebindingManifestation(Roshar.Surges surge)
	{
		switch (surge)
		{
			case ADHESION:
				return new SurgeAdhesion(surge);
			case GRAVITATION:
				return new SurgeGravitation(surge);
			case DIVISION:
				return new SurgeDivision(surge);
			case ABRASION:
				return new SurgeAbrasion(surge);
			case PROGRESSION:
				return new SurgeProgression(surge);
			case ILLUMINATION:
				return new SurgeIllumination(surge);
			case TRANSFORMATION:
				return new SurgeTransformation(surge);
			case TRANSPORTATION:
				return new SurgeTransportation(surge);
			case COHESION:
				return new SurgeCohesion(surge);
			case TENSION:
				return new SurgeTension(surge);
		}

		//shouldn't ever make it here
		return new SurgebindingBase(surge);
	}

	public static Map<ResourceLocation, String> getManifestations()
	{
		Map<ResourceLocation, String> map = new HashMap<>();

		for (AManifestation data : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
		{
			map.put(data.getRegistryName(), data.description().getKey());
		}
		return map;
	}
}
