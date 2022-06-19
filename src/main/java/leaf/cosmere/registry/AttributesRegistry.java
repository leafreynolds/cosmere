/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.registry;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.constants.Roshar;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class AttributesRegistry
{
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Cosmere.MODID);

	public static final Map<String, RegistryObject<Attribute>> COSMERE_ATTRIBUTES = makeAttributeMap();


	public static final MobType SPREN = new MobType();

	public static Map<String, RegistryObject<Attribute>> makeAttributeMap()
	{
		Map<String, RegistryObject<Attribute>> attributeModifiers = new HashMap<>();

		for (Roshar.Surges surge : Roshar.Surges.values())
		{
			String attributeName = surge.getRegistryName();
			RegistryObject<Attribute> attribute = ATTRIBUTES.register(
					attributeName,
					() -> (new RangedAttribute(
							"manifestation." + Cosmere.MODID + "." + attributeName,
							0,
							0,
							10)).setSyncable(true)
			);

			attributeModifiers.put(attributeName, attribute);
		}

		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (metalType.hasAssociatedManifestation())
			{
				String mistingNamePath = metalType.getAllomancyRegistryName();
				String ferringNamePath = metalType.getFeruchemyRegistryName();

				RegistryObject<Attribute> mistingAttribute = ATTRIBUTES.register(
						mistingNamePath,
						() -> (new RangedAttribute(
								"manifestation." + Cosmere.MODID + "." + mistingNamePath,
								0,
								0,
								20)).setSyncable(true)
				);

				RegistryObject<Attribute> ferringAttribute = ATTRIBUTES.register(
						ferringNamePath,
						() -> (new RangedAttribute(
								"manifestation." + Cosmere.MODID + "." + ferringNamePath,
								0,
								0,
								20)).setSyncable(true)
				);

				attributeModifiers.put(mistingNamePath, mistingAttribute);
				attributeModifiers.put(ferringNamePath, ferringAttribute);
			}

			if (!metalType.hasAttribute())// != Metals.MetalType.COPPER && metalType != Metals.MetalType.TIN)
			{
				continue;
			}

			final String metalName = metalType.getName();

			int defaultVal = 0;
			double min = 0;
			double max = 0;


			switch (metalType)
			{
				case TIN:
				{
					defaultVal = 0;
					min = 0;
					max = 1;
				}
				break;
				case COPPER:
				{
					defaultVal = 1;
					min = 0;
					max = 20;
				}
				break;
				case ATIUM:
				{
					defaultVal = 1;
					min = 0.1f;
					max = 20;
				}
				break;
			}

			//requires effectively final values
			final int finalDefaultVal = defaultVal;
			final double finalMin = min;
			final double finalMax = max;
			attributeModifiers.put(metalName, ATTRIBUTES.register(
					metalName,
					() -> (new RangedAttribute(
							Cosmere.MODID + "." + metalName,
							finalDefaultVal,
							finalMin,
							finalMax))
							.setSyncable(true)
			));
		}


		return attributeModifiers;
	}


}
