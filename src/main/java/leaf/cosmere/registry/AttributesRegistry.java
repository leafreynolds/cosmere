/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.registry;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Metals;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AttributesRegistry
{
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Cosmere.MODID);

    public static final Map<Metals.MetalType, RegistryObject<Attribute>> FERUCHEMICAL_STRENGTH_ATTRIBUTES =
            Arrays.stream(Metals.MetalType.values())
                    .filter(Metals.MetalType::hasAssociatedManifestation)
                    .collect(Collectors.toMap(
                            Function.identity(),
                            type -> ATTRIBUTES.register(
                                    "feruchemical_" + type.name().toLowerCase(),
                                    () -> (new RangedAttribute(
                                            Cosmere.MODID + ".feruchemical_" + type.name().toLowerCase(),
                                            0,
                                            0,
                                            1000)).setShouldWatch(true)
                            )));

    public static final Map<Metals.MetalType, RegistryObject<Attribute>> ALLOMANTIC_STRENGTH_ATTRIBUTES =
            Arrays.stream(Metals.MetalType.values())
                    .filter(Metals.MetalType::hasAssociatedManifestation)
                    .collect(Collectors.toMap(
                            Function.identity(),
                            type -> ATTRIBUTES.register(
                                    "allomantic_" + type.name().toLowerCase(),
                                    () -> (new RangedAttribute(
                                            Cosmere.MODID + ".allomantic_" + type.name().toLowerCase(),
                                            0,
                                            0,
                                            1000)).setShouldWatch(true)
                            )));
}
