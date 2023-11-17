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

import java.awt.print.Book;
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

		BookStuff.Entry qido = new BookStuff.Entry("qido", sandmasteryCategory, "sandmastery:qido");
		pages.add(new BookStuff.CraftingPage("sandmastery:qido"));
		pages.add(new BookStuff.TextPage("As any good Mastrell knows, hydration is one of the most important aspects of sand mastery. " +
				"The Qido is the most common way of carrying water on dayside. Crouch (" + PatchouliTextFormat.Keybind("key.sneak") + ") and use (" + PatchouliTextFormat.Keybind("key.use") + ") on a water source to fill it, and use it like any other bottle to drink from it."));
		qido.pages = pages.toArray(BookStuff.Page[]::new);
		pages.clear();
		entries.add(qido);

		BookStuff.Entry sandJar = new BookStuff.Entry("sand_jar", sandmasteryCategory, "sandmastery:sand_jar");
		pages.add(new BookStuff.TextPage("As mentioned previously, the sand from Tal'Dain reacts to physical investiture, which means that it can be measured. " +
				"Common technology on this planet seems to use a substance called 'redstone' in order to power their fabrials. A device like the 'Comparator' " +
				"Can measure the investiture in the sand jar, and release a redstone signal."));
		pages.add(new BookStuff.TextPage("You can place the sand jar item on the ground as a block by crouching (holding " + PatchouliTextFormat.Keybind("key.sneak") + ") while placing, or you can get the sand out by placing normally. Removing the sand is how you get the starting sand for the sand spreader."));
		sandJar.pages = pages.toArray(BookStuff.Page[]::new);
		pages.clear();
		entries.add(sandJar);

		BookStuff.Entry sandSpreader = new BookStuff.Entry("sand_spreader", sandmasteryCategory, "sandmastery:sand_spreading_tub");
		pages.add(new BookStuff.CraftingPage("While you can grow the micro-organisms that contribute to sand mastery in the world, the sand spreading tub creates optimal conditions for the growth, greatly speeding up the process.", "sandmastery:sand_spreading_tub"));
		sandSpreader.pages = pages.toArray(BookStuff.Page[]::new);
		pages.clear();
		entries.add(sandSpreader);

		BookStuff.Entry sandPouch = new BookStuff.Entry("sand_pouch", sandmasteryCategory, "sandmastery:sand_pouch");
		pages.add(new BookStuff.TextPage("The sand pouch is the only way to ensure you always have the required sand on you, something about this world seems to prevent using sand that is laying in the world. ((In world sand usage is WIP)) It has also been noted that the pouch is seemingly bottomless, nearly impossible to tell how much sand is inside. ((Known bug: sand inside the pouch is invisible upon re-opening the pouch on a server))"));
		pages.add(new BookStuff.CraftingPage("sandmastery:sand_pouch"));
		sandPouch.pages = pages.toArray(BookStuff.Page[]::new);
		pages.clear();
		entries.add(sandPouch);

		// Sandmastery Basics Entry
		BookStuff.Entry sandmasteryBasics = new BookStuff.Entry("sandmastery_basics", sandmasteryCategory, "sandmastery:qido");

		BookStuff.Page firstPage = new BookStuff.TextPage();
		firstPage.setTitle("Sand Mastery (For Dummies)");
		// TODO
		firstPage.setText(
				"Here is where I shall explain Sand Mastery, a system of Investiture native to Taldain. " +
						"The sand from Taldain is rare to find on this planet- perhaps delving into abandoned pyramids will aid in your search. " +
						"While normally black, Whenever in the presence of physical investiture- Allomancy and Surgebinding, to name a few- the sand will turn white and spread to nearby sand." +
						"This has been observed to be most consistent in a sand spreading tub.");
		pages.add(firstPage);

		BookStuff.Page secondPage = new BookStuff.TextPage();
		secondPage.setTitle("Sand Mastery (For Dummies)");
		secondPage.setText("To get started, you will need to be hydrated. You can accomplish this simply by drinking water bottles, but to hold even more water on you at a time consider looking into a Qido. " +
				"You will also need to keep sand on you, for that you will need to get a sand pouch, the more sand you put inside, the more charge it will hold." +
				"And finally, you will need to charge the sand inside the jar. The sand will charge when in the presence of kinetic investiture, as long as it is not being hidden by something like copper allomancy.");
		pages.add(secondPage);

		sandmasteryBasics.pages = pages.toArray(BookStuff.Page[]::new);
		pages.clear();
		entries.add(sandmasteryBasics);
	}
}
