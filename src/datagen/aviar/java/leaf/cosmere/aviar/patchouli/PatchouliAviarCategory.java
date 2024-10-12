/*
 * File updated ~ 21 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.patchouli;

import leaf.cosmere.patchouli.data.BookStuff;

import java.util.ArrayList;
import java.util.List;

public class PatchouliAviarCategory
{
	public static void collect(List<BookStuff.Category> categories, List<BookStuff.Entry> entries)
	{
		BookStuff.Category aviar = new BookStuff.Category(
				"aviar",
				"Of Birds and Bonds.$(br)" +
						"In this section, I shall explain all I have discovered about Aviar. While not typically found on this planet, world hoppers have been known to travel with these as companions, bringing knowledge of these creatures with them.",
				"aviar:fruit_of_patji");
		aviar.sortnum = 10;
		aviar.secret = false;
		categories.add(aviar);

		//Start a page list.
		List<BookStuff.Page> pages = new ArrayList<>();

		//aviar basics entry
		{
			BookStuff.Entry entry = new BookStuff.Entry("aviar_basics", aviar, "minecraft:book").setDisplayTitle("Aviar (Terminology)");
			entry.priority = true;
			entry.sortnum = 1;

			BookStuff.Page terminologyPage = new BookStuff.TextPage();
			terminologyPage.setTitle("Terminology");
			terminologyPage.setText("Some terminology:"
					+ "$(li) Aviar: A bird who has bonded a symbiote native to the planet 'First of the Sun', in the Drominad system.."
					+ "$(li) Fruit of Patji: A fruit from off world. Contains the symbiote that grants birds their abilities, turning them into Aviar."
					+ "$(li) Aviar Bond: A bond between an Aviar and a human. This bond allows the human to gain access to the Aviar's abilities."
			);
			pages.add(terminologyPage);
			pages.add(new BookStuff.EntityPage("", "", "aviar:aviar{Variant:0}"));
			entry.pages = pages.toArray(BookStuff.Page[]::new);
			pages.clear();
			entries.add(entry);
		}

		//Parrot Conversion
		{
			BookStuff.Entry entry = new BookStuff.Entry("how_to_get_aviar", aviar, "aviar:fruit_of_patji").setDisplayTitle("Fruit of Patji");
			entry.priority = false;
			entry.sortnum = 2;

			pages.add(
					new BookStuff.SpotlightPage(
							"A very rare fruit, native to the planet 'First of the Sun', in the Drominad system. It is said that the fruit contains a symbiote that can bond with a human, granting them their companions wondrous abilities." +
									"$(br)As this is not that planet, they're very hard to come by, with only the occaisional world hopper bringing them to this world." +
									"$(br)It is worth noting that the Fruit of Patji will not effect already tame parrots, rather you have to feed it to a parrot then tame the creature.",
							"aviar:fruit_of_patji"
					)
			);

			pages.add(new BookStuff.EntityPage(
					"For the cost of some emeralds (and a nether star), a wandering trader may be convinced to part with the rare fruit of Patji.",
					"Wandering Trader",
					"minecraft:wandering_trader"));


			entry.pages = pages.toArray(BookStuff.Page[]::new);
			pages.clear();
			entries.add(entry);
		}

		{
			BookStuff.Entry entry = new BookStuff.Entry("breed_abilities", aviar, "minecraft:writable_book").setDisplayTitle("Breed Abilities");
			entry.priority = false;
			entry.sortnum = 3;


			BookStuff.Page textPage = new BookStuff.TextPage();
			//the first page title is ignored
			//textPage.setTitle("Breeds");

			textPage.setText(
					"Strangely, the parrots around here appear to be compatible with the symbiote found within the fruit of Patji. $(br)$(br)" +
							"There are only a few known breeds nearby, but it seems that each different breed manifests a different ability."
			);
			pages.add(textPage);

			pages.add(new BookStuff.EntityPage("Grants Cognitive Concealment, hiding you from magic detection such as alllomantic bronze and white sand.", "", "aviar:aviar{Variant:0}"));
			pages.add(new BookStuff.EntityPage("Grants increased fortune and looting (but not chest loot luck)", "", "aviar:aviar{Variant:1}"));
			pages.add(new BookStuff.EntityPage("Grants doubled experience gain.", "", "aviar:aviar{Variant:2}"));
			pages.add(new BookStuff.EntityPage("Decreases incoming damage.", "", "aviar:aviar{Variant:3}"));
			pages.add(new BookStuff.EntityPage("Allows you to detect when mobs are targeting you. [currently doesn't work]", "", "aviar:aviar{Variant:4}"));

			entry.pages = pages.toArray(BookStuff.Page[]::new);
			entries.add(entry);
		}
	}
}
