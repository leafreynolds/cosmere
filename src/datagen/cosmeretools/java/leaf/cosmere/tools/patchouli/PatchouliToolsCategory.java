/*
 * File updated ~ 22 - 3 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.patchouli;

import leaf.cosmere.patchouli.data.BookStuff;

import java.util.ArrayList;
import java.util.List;

public class PatchouliToolsCategory
{
	public static void collect(List<BookStuff.Category> categories, List<BookStuff.Entry> entries)
	{
		BookStuff.Category tools = new BookStuff.Category(
				"tools",
				"Metalworkers recently discovered that other metals besides iron and gold can be worked to make tools and armor.",
				"cosmere:pewter_pickaxe");
		tools.sortnum = 99;
		tools.secret = true;
		categories.add(tools);

		//Start a page list.
		List<BookStuff.Page> pages = new ArrayList<>();

		//tools basics entry
		{
			BookStuff.Entry entry = new BookStuff.Entry("tools_basics", tools, tools.icon).setDisplayTitle("Tools (For Dummies)");
			entry.priority = true;


			BookStuff.Page terminologyPage = new BookStuff.TextPage();
			terminologyPage.setTitle("Terminology");
			terminologyPage.setText(
					"In this journal, I shall explain all I have discovered about Tools. $(br)" +
							"Firstly, some terminology:");
			pages.add(terminologyPage);


			entry.pages = pages.toArray(BookStuff.Page[]::new);
			pages.clear();
			entries.add(entry);
		}

	}
}
