/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.patchouli;

import leaf.cosmere.sandmastery.common.manifestation.SandmasteryManifestation;
import leaf.cosmere.sandmastery.common.registries.SandmasteryManifestations;
import leaf.cosmere.api.Constants;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.patchouli.data.BookStuff;
import leaf.cosmere.patchouli.data.PatchouliTextFormat;

import java.util.ArrayList;
import java.util.List;

public class PatchouliSandmasteryCategory
{
	public static void collect(List<BookStuff.Category> categories, List<BookStuff.Entry> entries)
	{
		// TODO
		BookStuff.Category allomancyCategory = new BookStuff.Category(
				"sandmastery",
				"TODO",
				"cosmere:pewter_nugget");
		categories.add(allomancyCategory);
		allomancyCategory.sortnum = 1;

		// Start a page list
		List<BookStuff.Page> pages = new ArrayList<>();
		// Allomancy Basics Entry
		BookStuff.Entry allomancyBasics = new BookStuff.Entry("allomancy_basics", allomancyCategory, "allomancy:metal_vial");


		BookStuff.Page firstPage = new BookStuff.TextPage();
		firstPage.setTitle("Sand Mastery (For Dummies)");
		// TODO
		firstPage.setText(
				"Here is where I shall explain Sand Mastery, a system of Investiture native to Taldain. " +
						"TODO");
		pages.add(firstPage);
		BookStuff.Page secondPage = new BookStuff.TextPage();
		secondPage.setTitle("Sand Mastery (For Dummies)");
		secondPage.setText(
				"TODO");
		pages.add(secondPage);
		// TODO Change icon
		pages.add(new BookStuff.CraftingPage("allomancy:metal_vial"));
		allomancyBasics.pages = pages.toArray(BookStuff.Page[]::new);
		pages.clear();
		entries.add(allomancyBasics);
	}
}
