/*
 * File created ~ 14 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.patchouli.categories;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.datagen.patchouli.BookStuff;

import java.util.List;

public class PatchouliHemalurgy
{
	public static void collect(List<BookStuff.Category> categories, List<BookStuff.Entry> entries)
	{
		BookStuff.Category hemalurgy = new BookStuff.Category(
				"hemalurgy",
				"The forbidden field of magic that takes from others so that you may get stronger.",
				"cosmere:atium_spike");
		hemalurgy.sortnum = 3;
		hemalurgy.secret = true;


		categories.add(hemalurgy);


		//hemalurgy
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (!metalType.hasHemalurgicEffect())
			{
				continue;
			}

			String metalName = metalType.getName();
			BookStuff.Entry e = new BookStuff.Entry(
					"hemalurgic_" + metalName,
					hemalurgy,
					hemalurgy.icon.replace("atium", metalName));
			e.sortnum = metalType.getID();

			e.pages = new BookStuff.Page[]
					{
							new BookStuff.TextPage(metalType.getHemalurgicUseString())
					};

			entries.add(e);
		}
	}
}
