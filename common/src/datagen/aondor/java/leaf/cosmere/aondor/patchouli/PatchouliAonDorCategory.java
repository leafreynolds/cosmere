/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aondor.patchouli;

import leaf.cosmere.patchouli.data.BookStuff;

import java.util.ArrayList;
import java.util.List;

public class PatchouliAonDorCategory
{
	public static void collect(List<BookStuff.Category> categories, List<BookStuff.Entry> entries)
	{
		BookStuff.Category aondor = new BookStuff.Category(
				"aondor",
				"AonDor description that says some stuff.",
				"minecraft:paper");
		aondor.sortnum = 99;
		aondor.secret = true;
		categories.add(aondor);

		//Start a page list.
		List<BookStuff.Page> pages = new ArrayList<>();

		//aondor basics entry
		{
			BookStuff.Entry entry = new BookStuff.Entry("aondor_basics", aondor, aondor.icon).setDisplayTitle("AonDor (For Dummies)");
			entry.priority = true;


			BookStuff.Page terminologyPage = new BookStuff.TextPage();
			terminologyPage.setTitle("Terminology");
			terminologyPage.setText(
					"In this journal, I shall explain all I have discovered about AonDor. $(br)" +
							"Firstly, some terminology:");
			pages.add(terminologyPage);


			entry.pages = pages.toArray(BookStuff.Page[]::new);
			pages.clear();
			entries.add(entry);
		}

	}
}
