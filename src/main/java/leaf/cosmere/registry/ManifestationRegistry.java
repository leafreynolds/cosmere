/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.registry;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.FeruchemyEffectBase;
import leaf.cosmere.effects.store.*;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.ManifestationBase;
import leaf.cosmere.manifestation.allomancy.*;
import leaf.cosmere.manifestation.feruchemy.FeruchemyBase;
import leaf.cosmere.manifestation.feruchemy.FeruchemyCopper;
import leaf.cosmere.manifestation.feruchemy.FeruchemyZinc;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ManifestationRegistry
{
    public static final DeferredRegister<AManifestation> MANIFESTATIONS = DeferredRegister.create(AManifestation.class, Cosmere.MODID);
    public static Supplier<IForgeRegistry<AManifestation>> MANIFESTATION_REGISTRY = MANIFESTATIONS.makeRegistry("investiture_manifestations", () -> new RegistryBuilder<AManifestation>().setMaxID(Integer.MAX_VALUE - 1));

    public static final RegistryObject<AManifestation> NONE = MANIFESTATIONS.register(Manifestations.NONE, () -> new ManifestationBase(Manifestations.ManifestationTypes.NONE, Color.WHITE.getRGB()));

    // Allomancy
    public static final Map<Metals.MetalType, RegistryObject<AManifestation>> ALLOMANCY_POWERS =
            Arrays.stream(Metals.MetalType.values())
                    .filter(Metals.MetalType::hasAssociatedManifestation)
                    .collect(Collectors.toMap(
                            Function.identity(),
                            metalType ->
                                    MANIFESTATIONS.register(
                                            metalType.getMistingName(),
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
                                            metalType.getFerringName(),
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
    //public static final RegistryObject<AbstractInvestitureManifestation> WINDRUNNER = MANIFESTATIONS.register(Constants.Manifestations.Surgebinding.WINDRUNNER, () -> new InvestitureManifestationBase(Color.WHITE.getRGB()));
    //public static final RegistryObject<AbstractInvestitureManifestation> SKYBREAKER = MANIFESTATIONS.register(Constants.Manifestations.Surgebinding.SKYBREAKER, () -> new InvestitureManifestationBase(Color.WHITE.getRGB()));
    //public static final RegistryObject<AbstractInvestitureManifestation> DUSTBRINGER = MANIFESTATIONS.register(Constants.Manifestations.Surgebinding.DUSTBRINGER, () -> new InvestitureManifestationBase(Color.WHITE.getRGB()));
    //public static final RegistryObject<AbstractInvestitureManifestation> EDGEDANCER = MANIFESTATIONS.register(Constants.Manifestations.Surgebinding.EDGEDANCER, () -> new InvestitureManifestationBase(Color.WHITE.getRGB()));
    //public static final RegistryObject<AbstractInvestitureManifestation> TRUTHWATCHER = MANIFESTATIONS.register(Constants.Manifestations.Surgebinding.TRUTHWATCHER, () -> new InvestitureManifestationBase(Color.WHITE.getRGB()));
    //public static final RegistryObject<AbstractInvestitureManifestation> LIGHTWEAVER = MANIFESTATIONS.register(Constants.Manifestations.Surgebinding.LIGHTWEAVER, () -> new InvestitureManifestationBase(Color.WHITE.getRGB()));
    //public static final RegistryObject<AbstractInvestitureManifestation> ELSECALLER = MANIFESTATIONS.register(Constants.Manifestations.Surgebinding.ELSECALLER, () -> new InvestitureManifestationBase(Color.WHITE.getRGB()));
    //public static final RegistryObject<AbstractInvestitureManifestation> WILLSHAPER = MANIFESTATIONS.register(Constants.Manifestations.Surgebinding.WILLSHAPER, () -> new InvestitureManifestationBase(Color.WHITE.getRGB()));
    //public static final RegistryObject<AbstractInvestitureManifestation> STONEWARD = MANIFESTATIONS.register(Constants.Manifestations.Surgebinding.STONEWARD, () -> new InvestitureManifestationBase(Color.WHITE.getRGB()));
    //public static final RegistryObject<AbstractInvestitureManifestation> BONDSMITH = MANIFESTATIONS.register(Constants.Manifestations.Surgebinding.BONDSMITH, () -> new InvestitureManifestationBase(Color.WHITE.getRGB()));

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
                return new AllomancyIron(metalType);
            case STEEL://todo push and steel sight
                return new AllomancySteel(metalType);
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
        if (metalType == Metals.MetalType.COPPER)
            return new FeruchemyCopper(metalType);
        else if (metalType == Metals.MetalType.ZINC)
            return new FeruchemyZinc(metalType);
        else
            return new FeruchemyBase(metalType);
    }

    public static Map<ResourceLocation, String> getManifestations()
    {
        Map<ResourceLocation, String> map = new HashMap<>();

        IForgeRegistry<AManifestation> abstractInvestitureManifestations = ManifestationRegistry.MANIFESTATION_REGISTRY.get();
        for (AManifestation data : abstractInvestitureManifestations)
        {
            map.put(data.getRegistryName(), data.description().getKey());
        }
        return map;
    }
}
