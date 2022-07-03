/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.registry;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.constants.Roshar;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AttributesRegistry
{
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Cosmere.MODID);

	public static final Map<String, RegistryObject<Attribute>> COSMERE_ATTRIBUTES = new HashMap<>();

	public static final Map<Metals.MetalType, RegistryObject<Attribute>> ALLOMANCY_ATTRIBUTES =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasAssociatedManifestation)
					.collect(Collectors.toMap(
							Function.identity(),
							metalType ->
							{
								final String attributeName = metalType.getAllomancyRegistryName();
								return createAttribute(attributeName, 0, 0, 20);
							}));

	public static final Map<Metals.MetalType, RegistryObject<Attribute>> FERUCHEMY_ATTRIBUTES =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasAssociatedManifestation)
					.collect(Collectors.toMap(
							Function.identity(),
							metalType ->
							{
								String attributeName = metalType.getFeruchemyRegistryName();
								return createAttribute(attributeName, 0, 0, 20);
							}));

	public static final Map<Roshar.Surges, RegistryObject<Attribute>> SURGEBINDING_ATTRIBUTES =
			Arrays.stream(Roshar.Surges.values())
					.collect(Collectors.toMap(
							Function.identity(),
							surge ->
							{
								String attributeName = surge.getRegistryName();
								return createAttribute(attributeName, 0, 0, 10);
							}));

	public static final RegistryObject<Attribute> NIGHT_VISION_ATTRIBUTE = createAttribute(Metals.MetalType.TIN.getName(), 0, 0, 1);
	public static final RegistryObject<Attribute> XP_RATE_ATTRIBUTE = createAttribute(Metals.MetalType.COPPER.getName(), 1, 0, 20);
	public static final RegistryObject<Attribute> SIZE_ATTRIBUTE = createAttribute(Metals.MetalType.ATIUM.getName(), 1, 0.1f, 20);

	private static RegistryObject<Attribute> createAttribute(String attributeName, double defaultVal, double min, double max)
	{
		final RegistryObject<Attribute> attribute = ATTRIBUTES.register(
				attributeName,
				() -> (new RangedAttribute(
						"manifestation." + Cosmere.MODID + "." + attributeName,
						defaultVal,
						min,
						max)).setSyncable(true)
		);
		
		COSMERE_ATTRIBUTES.put(attributeName, attribute);

		return attribute;
	}

}
