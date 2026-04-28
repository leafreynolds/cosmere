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
				"surgebinding:cut_amethyst_medium");
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
		pages.clear();

		BookStuff.Entry adhesionSurge = new BookStuff.Entry("adhesion_surge", surgebindingCategory, "surgebinding:sapphire");
		pages.add(new BookStuff.TextPage("The Surge of Adhesion can be manipulated to bind things together. The two orders of Knights Radiant with access to this Surge are the Bondsmiths and the Windrunners.","Adhesion"));
		adhesionSurge.pages = pages.toArray(BookStuff.Page[]::new);
		entries.add(adhesionSurge);
		pages.clear();

		BookStuff.Entry gravitationSurge = new BookStuff.Entry("gravitation_surge", surgebindingCategory, "surgebinding:smokestone");
		pages.add(new BookStuff.TextPage("The Surge of Gravitation can be manipulated to change the direction and strength of an object's gravitational attraction. The two orders of Knights Radiant with access to this Surge are the Windrunners and the Skybreakers","Gravitation"));
		gravitationSurge.pages = pages.toArray(BookStuff.Page[]::new);
		entries.add(gravitationSurge);
		pages.clear();

		BookStuff.Entry divisionSurge = new BookStuff.Entry("division_surge", surgebindingCategory, "surgebinding:ruby");
		pages.add(new BookStuff.TextPage("The Surge of Division can be manipulated to have power over destruction and decay. The two orders of Knights Radiant with access to this Surge are the Dustbringers and the Skybreakers.","Division"));
		divisionSurge.pages = pages.toArray(BookStuff.Page[]::new);
		entries.add(divisionSurge);
		pages.clear();

		BookStuff.Entry abrasionSurge = new BookStuff.Entry("abrasion_surge", surgebindingCategory, "surgebinding:rosharan_diamond");
		pages.add(new BookStuff.TextPage("The Surge of Abrasion can be manipulated to alter the frictional force between two surfaces. The two orders of Knights Radiant with access to this Surge are the Dustbringers and the Edgedancers.","Abrasion"));
		abrasionSurge.pages = pages.toArray(BookStuff.Page[]::new);
		entries.add(abrasionSurge);
		pages.clear();

		BookStuff.Entry progressionSurge = new BookStuff.Entry("progression_surge", surgebindingCategory, "minecraft:emerald");
		pages.add(new BookStuff.TextPage("The Surge of Progression can be manipulated to alter the growth and healing of organisms. The two orders of Knights Radiant with access to this Surge are the Edgedancers and the Truthwatchers.","Progression"));
		progressionSurge.pages = pages.toArray(BookStuff.Page[]::new);
		entries.add(progressionSurge);
		pages.clear();
	}
}
