/*
 * File created ~ 14 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.patchouli.categories;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.datagen.patchouli.BookStuff;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.allomancy.AllomancyBase;
import leaf.cosmere.utils.helpers.StringHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

import static leaf.cosmere.registry.ManifestationRegistry.ALLOMANCY_POWERS;

public class PatchouliAllomancy
{
	public static void collect(List<BookStuff.Category> categories, List<BookStuff.Entry> entries)
	{
		BookStuff.Category allomancy = new BookStuff.Category(
				"allomancy",
				"This is the art of consuming a piece of metal related to your power, and then \"burning\" it for an effect.",
				"cosmere:pewter_nugget");
		allomancy.sortnum = 1;


		categories.add(allomancy);


		BookStuff.Entry allomancyBasics = new BookStuff.Entry("allomancy_basics", allomancy, allomancy.icon);
		allomancyBasics.pages = new BookStuff.Page[]
				{
						new BookStuff.TextPage("If you entered this world with an allomantic ability, you're called a $(6)misting$().", allomancy.icon),
						new BookStuff.SpotlightPage("Metal vial", "cosmere:metal_vial"),
						new BookStuff.CraftingPage("Metal vial", "cosmere:metal_vial"),
				};
		allomancyBasics.priority = true;
		List<BookStuff.Page> pages = new ArrayList<>();

		entries.add(allomancyBasics);

		//allomancy
		for (RegistryObject<AManifestation> manifestation : ALLOMANCY_POWERS.values())
		{
			AManifestation aManifestation = manifestation.get();
			AllomancyBase allomancyManifestation = (AllomancyBase) aManifestation;
			Metals.MetalType metalType = allomancyManifestation.getMetalType();

			if (!metalType.hasFeruchemicalEffect())
			{
				continue;
			}

			String metalName = metalType.getName();

			String itemFullName = metalType.getNuggetItem().getRegistryName().toString();

			BookStuff.Entry entryForThisPower = new BookStuff.Entry(
					"allomantic_" + metalName,
					allomancy,
					itemFullName);
			entryForThisPower.sortnum = metalType.getID();

			pages.clear();
			String mistingName = StringHelper.fixCapitalisation(metalType.getMistingName());


			switch (metalType)
			{
				default:
					pages.add(new BookStuff.TextPage("A misting who burns " + metalName + " is known as a \"" + mistingName + "\"."));
					break;
				case ALUMINUM:
				case DURALUMIN:
					//add extra note so that these people will know of their shame.
					pages.add(new BookStuff.TextPage("A misting who can only burn " + metalName + " is known as a \"" + mistingName + "\" as they gain no discernible effect from burning their metal."));
					break;
			}

			pages.add(new BookStuff.TextPage(metalType.GetAllomancyDescription()));

			entryForThisPower.pages = pages.stream().toArray(BookStuff.Page[]::new);
			entries.add(entryForThisPower);
		}
	}
}
