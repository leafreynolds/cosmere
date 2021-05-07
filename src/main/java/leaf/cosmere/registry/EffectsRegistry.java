/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.registry;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.allomancy.*;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import leaf.cosmere.effects.feruchemy.store.*;
import leaf.cosmere.effects.feruchemy.tap.*;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EffectsRegistry
{

    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Cosmere.MODID);

    public static final RegistryObject<Effect> ALLOMANTIC_COPPER = EFFECTS.register(
            "burning_" + Metals.MetalType.COPPER.name().toLowerCase(),
            () -> new AllomancyEffectBase(Metals.MetalType.COPPER, EffectType.BENEFICIAL));

    public static final RegistryObject<Effect> ALLOMANCY_BOOST = EFFECTS.register(
            "allomancy_boost" ,
            () -> new AllomancyBoostEffect(Metals.MetalType.DURALUMIN, EffectType.BENEFICIAL));

    public static final Map<Metals.MetalType, RegistryObject<Effect>> TAPPING_EFFECTS =
            Arrays.stream(Metals.MetalType.values())
                    .filter(Metals.MetalType::hasFeruchemicalEffect)
                    .collect(Collectors.toMap(
                            Function.identity(),
                            type -> EFFECTS.register(
                                    "tapping_" + type.name().toLowerCase(),
                                    () -> makeTappingEffect(type))
                            )
                    );

    public static final Map<Metals.MetalType, RegistryObject<Effect>> STORING_EFFECTS =
            Arrays.stream(Metals.MetalType.values())
                    .filter(Metals.MetalType::hasFeruchemicalEffect)
                    .collect(Collectors.toMap(
                            Function.identity(),
                            type -> EFFECTS.register(
                                    "storing_" + type.name().toLowerCase(),
                                    () -> makeStoringEffect(type))
                            )
                    );


    private static Effect makeStoringEffect(Metals.MetalType metalType)
    {
        switch (metalType)
        {
            case IRON:
                return new IronStoreEffect(metalType, EffectType.BENEFICIAL);
            case STEEL:
                return (new SteelStoreEffect(metalType, EffectType.BENEFICIAL));
            case PEWTER:
                return new PewterStoreEffect(metalType, EffectType.BENEFICIAL);
            case BRASS:
                return new BrassStoreEffect(metalType, EffectType.BENEFICIAL);
            case COPPER:
                return new CopperStoreEffect(metalType, EffectType.BENEFICIAL);
            case BRONZE:
                return new BronzeStoreEffect(metalType, EffectType.BENEFICIAL);
            case DURALUMIN:
                return new DuraluminStoreEffect(metalType, EffectType.BENEFICIAL);
            case CHROMIUM:
                return new ChromiumStoreEffect(metalType, EffectType.BENEFICIAL);
            case NICROSIL:
                return new NicrosilStoreEffect(metalType, EffectType.BENEFICIAL);
            case GOLD:
                return new GoldStoreEffect(metalType, EffectType.BENEFICIAL);
            case CADMIUM:
                return new CadmiumStoreEffect(metalType, EffectType.BENEFICIAL);
            case BENDALLOY:
                return new BendalloyStoreEffect(metalType, EffectType.BENEFICIAL);
                //todo atium
            // handled as part of the manifestation
            case ELECTRUM:
            case TIN:
            case ALUMINUM:
            case ZINC:
            default:
                return new FeruchemyEffectBase(metalType, EffectType.BENEFICIAL);
        }
    }

    private static Effect makeTappingEffect(Metals.MetalType metalType)
    {
        switch (metalType)
        {
            case IRON:
                return new IronTapEffect(metalType, EffectType.BENEFICIAL);
            case STEEL:
                return new SteelTapEffect(metalType, EffectType.BENEFICIAL);
            case PEWTER:
                return new PewterTapEffect(metalType, EffectType.BENEFICIAL);
            case BRASS:
                return new BrassTapEffect(metalType, EffectType.BENEFICIAL);
            case COPPER:
                return new CopperTapEffect(metalType, EffectType.BENEFICIAL);
            case BRONZE:
                return new BronzeTapEffect(metalType, EffectType.BENEFICIAL);
            case DURALUMIN:
                return new DuraluminTapEffect(metalType, EffectType.BENEFICIAL);
            case CHROMIUM:
                return new ChromiumTapEffect(metalType, EffectType.BENEFICIAL);
            case NICROSIL:
                return new NicrosilTapEffect(metalType, EffectType.BENEFICIAL);
            case GOLD:
                return new GoldTapEffect(metalType, EffectType.BENEFICIAL);
            case CADMIUM:
                return new CadmiumTapEffect(metalType, EffectType.BENEFICIAL);
            case BENDALLOY:
                return new BendalloyTapEffect(metalType, EffectType.BENEFICIAL);
                //todo atium
            // handled as part of the manifestation
            case ELECTRUM:
            case TIN:
            case ALUMINUM:
            case ZINC:
            default:
                return new FeruchemyEffectBase(metalType, EffectType.BENEFICIAL);
        }
    }
}
