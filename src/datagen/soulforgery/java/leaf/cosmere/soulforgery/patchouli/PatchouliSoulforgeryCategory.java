/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.soulforgery.patchouli;

import leaf.cosmere.patchouli.data.BookStuff;

import java.util.ArrayList;
import java.util.List;

public class PatchouliSoulforgeryCategory
{
	public static void collect(List<BookStuff.Category> categories, List<BookStuff.Entry> entries)
	{
		BookStuff.Category soulforgery = new BookStuff.Category(
				"soulforgery",
				"Soulforgery description that says some stuff.",
				"minecraft:paper");
		soulforgery.sortnum = 99;
		soulforgery.secret = true;
		categories.add(soulforgery);

		//Start a page list.
		List<BookStuff.Page> pages = new ArrayList<>();

		//soulforgery basics entry
		{
			BookStuff.Entry entry = new BookStuff.Entry("soulforgery_basics", soulforgery, soulforgery.icon).setDisplayTitle("Soulforgery (For Dummies)");
			entry.priority = true;


			BookStuff.Page terminologyPage = new BookStuff.TextPage();
			terminologyPage.setTitle("Terminology");
			terminologyPage.setText(
					"In this journal, I shall explain all I have discovered about Soulforgery. $(br)" +
							"Firstly, some terminology:");
			pages.add(terminologyPage);


			entry.pages = pages.toArray(BookStuff.Page[]::new);
			pages.clear();
			entries.add(entry);
		}

	}
}
