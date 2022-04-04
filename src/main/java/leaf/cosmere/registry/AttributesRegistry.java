/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.registry;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Metals;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class AttributesRegistry
{
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Cosmere.MODID);

	public static final Map<String, RegistryObject<Attribute>> COSMERE_ATTRIBUTES = makeAttributeMap();


	public static final CreatureAttribute SPREN = new CreatureAttribute();

	public static Map<String, RegistryObject<Attribute>> makeAttributeMap()
	{
		Map<String, RegistryObject<Attribute>> attributeModifiers = new HashMap<>();

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

			if (metalType != Metals.MetalType.COPPER && metalType != Metals.MetalType.TIN)
			{
				continue;
			}

			final String metalName = metalType.getName();

			int defaultVal = 0;
			int min = 0;
			int max = 0;


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
			}

			//requires effectively final values
			int finalDefaultVal = defaultVal;
			int finalMin = min;
			int finalMax = max;
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
