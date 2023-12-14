/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.awakening.patchouli;

import leaf.cosmere.patchouli.data.BookStuff;

import java.util.ArrayList;
import java.util.List;

public class PatchouliAwakeningCategory
{
	public static void collect(List<BookStuff.Category> categories, List<BookStuff.Entry> entries)
	{
		BookStuff.Category awakening = new BookStuff.Category(
				"awakening",
				"Awakening description that says some stuff.",
				"minecraft:paper");
		awakening.sortnum = 99;
		awakening.secret = true;
		categories.add(awakening);

		//Start a page list.
		List<BookStuff.Page> pages = new ArrayList<>();

		//awakening basics entry
		{
			BookStuff.Entry entry = new BookStuff.Entry("awakening_basics", awakening, awakening.icon).setDisplayTitle("Awakening (For Dummies)");
			entry.priority = true;


			BookStuff.Page terminologyPage = new BookStuff.TextPage();
			terminologyPage.setTitle("Terminology");
			terminologyPage.setText(
					"In this journal, I shall explain all I have discovered about Awakening. $(br)" +
							"Firstly, some terminology:");
			pages.add(terminologyPage);


			entry.pages = pages.toArray(BookStuff.Page[]::new);
			pages.clear();
			entries.add(entry);
		}

	}
}
