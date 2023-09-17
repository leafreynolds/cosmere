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
		BookStuff.Category sandmasteryCategory = new BookStuff.Category(
				"sandmastery",
				"A system of Investiture from Tal'Dain, Possibly involving a Luhel Bond. (WIP)",
				"sandmastery:qido");
		categories.add(sandmasteryCategory);
		sandmasteryCategory.sortnum = 4;

		// Start a page list
		List<BookStuff.Page> pages = new ArrayList<>();
		// Sandmastery Basics Entry
		BookStuff.Entry sandmasteryBasics = new BookStuff.Entry("sandmastery_basics", sandmasteryCategory, "sandmastery:qido");


		BookStuff.Page firstPage = new BookStuff.TextPage();
		firstPage.setTitle("Sand Mastery (For Dummies)");
		// TODO
		firstPage.setText(
				"Here is where I shall explain Sand Mastery, a system of Investiture native to Taldain. " +
						"The sand from Taldain is rare to find on this planet- perhaps delving into abandoned pyramids will aid in your search. " +
				"While normally black, Whenever in the presence of physical investiture- Allomancy and Surgebinding, to name a few- the sand will turn white.");
		pages.add(firstPage);
		BookStuff.Page secondPage = new BookStuff.TextPage();
		secondPage.setTitle("Sand Mastery (For Dummies)");
		secondPage.setText(
				"WIP");
		pages.add(secondPage);
		pages.add(new BookStuff.CraftingPage("sandmastery:qido"));
		sandmasteryBasics.pages = pages.toArray(BookStuff.Page[]::new);
		pages.clear();
		entries.add(sandmasteryBasics);

		BookStuff.Entry sandJar = new BookStuff.Entry("sand_jar", sandmasteryCategory, "sandmastery:sand_jar_block");
		pages.add(new BookStuff.TextPage("As mentioned previously, the sand from Tal'Dain reacts to physical investiture, which means that it can be measured. " +
				"Common technology on this planet seems to use a substance called 'redstone' in order to power their fabrials. A device like the 'Comparator' " +
				"Can measure the investiture in the sand jar, and release a redstone signal."));
		//pages.add(new BookStuff.TextPage("Here is an attempt at a keybind for activate selected. \"%s\"".formatted(PatchouliTextFormat.Keybind("cosmere.powers.use")) +
		//		"."));

		sandJar.pages = pages.toArray(BookStuff.Page[]::new);
		pages.clear();
		entries.add(sandJar);
	}
}
