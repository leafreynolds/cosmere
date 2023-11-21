/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.example.patchouli;

import leaf.cosmere.patchouli.data.BookStuff;

import java.util.ArrayList;
import java.util.List;

public class PatchouliExampleCategory
{
	public static void collect(List<BookStuff.Category> categories, List<BookStuff.Entry> entries)
	{
		BookStuff.Category example = new BookStuff.Category(
				"example",
				"Birds and Bonds.",
				"example:bird");
		example.sortnum = 3;
		example.secret = true;
		categories.add(example);

		//Start a page list.
		List<BookStuff.Page> pages = new ArrayList<>();

		//example basics entry
		BookStuff.Entry exampleBasics = new BookStuff.Entry("example_basics", example, example.icon).setDisplayTitle("Example (For Dummies)");
		exampleBasics.priority = true;


		BookStuff.Page terminologyPage = new BookStuff.TextPage();
		terminologyPage.setTitle("Terminology");
		terminologyPage.setText(
				"In this journal, I shall explain all I have discovered about Example. $(br)" +
						"Firstly, some terminology:"
						+ "$(li) Example: A species of bird, native to the planet 'First of the Sun', in the Drominad system..");
		pages.add(terminologyPage);


		exampleBasics.pages = pages.toArray(BookStuff.Page[]::new);
		pages.clear();
		entries.add(exampleBasics);

	}
}
