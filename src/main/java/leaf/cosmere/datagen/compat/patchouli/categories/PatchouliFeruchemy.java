/*
 * File created ~ 13 - 6 - 2022 ~ Leaf
 */

package leaf.cosmere.datagen.compat.patchouli.categories;

import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.datagen.compat.patchouli.BookStuff;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.feruchemy.FeruchemyBase;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
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

		List<BookStuff.Page> pages = new ArrayList<>();

		BookStuff.Entry feruchemyBasics = new BookStuff.Entry("feruchemy_basics", feruchemy, feruchemy.icon);
		feruchemyBasics.priority = true;
		pages.add(new BookStuff.TextPage("If you entered this world with an feruchemical ability, you're called a $(6)ferring$().", feruchemy.icon));
		pages.add(new BookStuff.CraftingPage("cosmere:atium_bracelet_metalmind").setText("Depending on what kind of metal you have access to, a metalmind of the corresponding metal will let you store and tap attributes."));

		feruchemyBasics.pages = pages.toArray(BookStuff.Page[]::new);
		entries.add(feruchemyBasics);


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
			pages.clear();


			String metalName = metalType.getName();

			BookStuff.Entry entryForThisMetal = new BookStuff.Entry(
					"feruchemical_" + metalName,
					feruchemy,
					feruchemy.icon.replace("copper", metalName));
			entryForThisMetal.sortnum = metalType.getID();


			final String itemLoc = String.format("cosmere:%s", metalName);

			pages.add(new BookStuff.TextPage(feruchemyManifestation.description().getKey()));
			pages.add(new BookStuff.CraftingPage(itemLoc + Constants.RegNameStubs.BRACELET + Constants.RegNameStubs.METALMIND));
			pages.add(new BookStuff.CraftingPage(itemLoc + Constants.RegNameStubs.RING + Constants.RegNameStubs.METALMIND));
			pages.add(new BookStuff.CraftingPage(itemLoc + Constants.RegNameStubs.NECKLACE + Constants.RegNameStubs.METALMIND));

			entryForThisMetal.pages = pages.toArray(BookStuff.Page[]::new);
			entries.add(entryForThisMetal);
		}
	}
}
