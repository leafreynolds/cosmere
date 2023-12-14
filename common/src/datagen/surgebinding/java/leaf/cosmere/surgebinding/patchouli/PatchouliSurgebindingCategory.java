/*
 * File updated ~ 2 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.surgebinding.patchouli;

import leaf.cosmere.patchouli.data.BookStuff;

import java.util.ArrayList;
import java.util.List;

public class PatchouliSurgebindingCategory
{
	public static void collect(List<BookStuff.Category> categories, List<BookStuff.Entry> entries)
	{
		BookStuff.Category surgebindingCategory = new BookStuff.Category(
				"surgebinding",
				"The Children of Honor must once again speak the ancient oaths. The Knights Radiant have returned. (WIP)",
				"surgebinding:amethyst_broam");
		surgebindingCategory.sortnum = 5;
		categories.add(surgebindingCategory);

		List<BookStuff.Page> pages = new ArrayList<>();

		BookStuff.Entry surgebindingBasics = new BookStuff.Entry("surgebinding_basics", surgebindingCategory, surgebindingCategory.icon);
		surgebindingBasics.priority = true;
		pages.add(new BookStuff.TextPage("One who enters a Nahel Bond with a spren will gain the surgebinding abilities of their specific order. (WIP)", surgebindingCategory.icon));


		surgebindingBasics.pages = pages.toArray(BookStuff.Page[]::new);
		entries.add(surgebindingBasics);
		pages.clear();

		BookStuff.Entry chullEntry = new BookStuff.Entry("chull", surgebindingCategory, surgebindingCategory.icon);
		pages.add(new BookStuff.TextPage("Chull exist, but don't do much as of yet. They are adorable though. (WIP)", surgebindingCategory.icon));
		pages.add(new BookStuff.EntityPage("Look how cute they are!", "Chull", "surgebinding:chull"));
		chullEntry.pages = pages.toArray(BookStuff.Page[]::new);
		entries.add(chullEntry);
	}
}
