/*
 * File created ~ 14 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.patchouli.categories;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.datagen.patchouli.BookStuff;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.feruchemy.FeruchemyBase;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

import static leaf.cosmere.registry.ManifestationRegistry.FERUCHEMY_POWERS;

public class PatchouliFeruchemy
{
	public static void collect(List<BookStuff.Category> categories, List<BookStuff.Entry> entries)
	{


		BookStuff.Category feruchemy = new BookStuff.Category(
				"feruchemy",
				"The art of equivalent exchange when it comes to the body. ",
				"cosmere:copper_bracelet_metalmind");
		feruchemy.sortnum = 2;


		categories.add(feruchemy);


		//feruchemy
		for (RegistryObject<AManifestation> manifestation : FERUCHEMY_POWERS.values())
		{
			AManifestation aManifestation = manifestation.get();
			FeruchemyBase feruchemyManifestation = (FeruchemyBase) aManifestation;
			Metals.MetalType metalType = feruchemyManifestation.getMetalType();

			if (!metalType.hasFeruchemicalEffect())
			{
				continue;
			}

			String metalName = metalType.getName();

			BookStuff.Entry e = new BookStuff.Entry(
					"feruchemical_" + metalName,
					feruchemy,
					feruchemy.icon.replace("copper", metalName));
			e.sortnum = metalType.getID();

			e.pages = new BookStuff.Page[]
					{
							new BookStuff.TextPage(feruchemyManifestation.description().getKey())
					};

			entries.add(e);
		}
	}
}
