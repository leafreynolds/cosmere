/*
 * File updated ~ 4 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.patchouli;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyManifestation;
import leaf.cosmere.feruchemy.common.registries.FeruchemyManifestations;
import leaf.cosmere.patchouli.data.BookStuff;
import leaf.cosmere.patchouli.data.PatchouliTextFormat;

import java.util.ArrayList;
import java.util.List;

public class PatchouliFeruchemyCategory
{
	public static void collect(List<BookStuff.Category> categories, List<BookStuff.Entry> entries)
	{
		BookStuff.Category feruchemy = new BookStuff.Category(
				"feruchemy",
				"The art of equivalent exchange when it comes to the body. ",
				"feruchemy:copper_bracelet_metalmind");

		feruchemy.sortnum = 2;
		categories.add(feruchemy);

		List<BookStuff.Page> pages = new ArrayList<>();

		BookStuff.Entry feruchemyBasics = new BookStuff.Entry("feruchemy_basics", feruchemy, feruchemy.icon);
		feruchemyBasics.priority = true;
		pages.add(new BookStuff.TextPage("The art requires storing an attribute in a piece of metal. Depending on what kind of metal you have access to, a metalmind of the corresponding metal will let you store and tap different attributes.", feruchemy.icon));
		pages.add(new BookStuff.CraftingPage("", "feruchemy:steel_ring_metalmind", "feruchemy:steel_bracelet_metalmind"));
		pages.add(new BookStuff.CraftingPage("feruchemy:steel_necklace_metalmind").setText("Metalminds can store varying amounts of charges, depending on the size."));

		feruchemyBasics.pages = pages.toArray(BookStuff.Page[]::new);
		pages.clear();
		entries.add(feruchemyBasics);

		BookStuff.Entry Compounding = new BookStuff.Entry("Compounding", feruchemy, feruchemy.icon);
		pages.add(new BookStuff.TextPage("A curious loophole in the systems of investiture on this planet has been discovered. It involves a metalborn with the allomantic and feruchemical " +
				"abilities of the same metal to burn one of their feruchemical metalminds, essentially creating a new allomantic metal that releases the stored feruchemical charge tenfold."));
		pages.add(new BookStuff.TextPage("This is activated by setting your allomantic power to a negative mode called Compounding and Flared Compounding, which spends allomantic charges to gain feruchemical stores quickly. " + "This is only accessible to Twinborn who have the same Allomantic and Feruchemic metals."));
		Compounding.pages = pages.toArray(BookStuff.Page[]::new);
		pages.clear();
		entries.add(Compounding);

		//feruchemy
		for (ManifestationRegistryObject<FeruchemyManifestation> manifestationRegistryObject : FeruchemyManifestations.FERUCHEMY_POWERS.values())
		{
			FeruchemyManifestation feruchemyManifestation = manifestationRegistryObject.get();
			Metals.MetalType metalType = feruchemyManifestation.getMetalType();

			if (!metalType.hasFeruchemicalEffect())
			{
				continue;
			}

			String metalName = metalType.getName();
			BookStuff.Entry entryForThisMetal = new BookStuff.Entry(
					"feruchemical_" + metalName,
					feruchemy,
					feruchemy.icon.replace("copper", metalName));
			entryForThisMetal.sortnum = metalType.getID();

			pages.clear();
			String ferringName = StringHelper.fixCapitalisation(metalType.getFerringName());


			switch (metalType)
			{
				case IRON:
					pages.add(new BookStuff.TextPage("A ferring who taps " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(ferringName) + "\", and stores Weight. " +
							"While storing, the Skimmer will find themselves getting lighter, and falling slower. Tapping makes them fall far faster, and take less knockback."));
					break;
				case STEEL:
					pages.add(new BookStuff.TextPage("A ferring who taps " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(ferringName) + "\", and stores Speed. " +
							"While storing, the Steelrunner will grow far slower. Tapping makes them move faster. Some reports say a Steelrunner can even run across water."));
					break;
				case TIN:
					pages.add(new BookStuff.TextPage("A ferring who taps " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(ferringName) + "\", and stores Senses. " +
							"While storing, the Windwhisperer will find themselves growing increasingly shortsighted. Tapping allows them to magnify their vision, as if looking through a spyglass."));
					break;
				case PEWTER:
					pages.add(new BookStuff.TextPage("A ferring who taps " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(ferringName) + "\", and stores Strength. " +
							"While storing, the Brute will find their attacks do little damage. Tapping allows them to gain increased damage on their attacks."));
					break;
				case ZINC:
					pages.add(new BookStuff.TextPage("A ferring who taps " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(ferringName) + "\", and stores Mental Speed. " +
							"While storing, the Sparker will find themselves learning less from their experiences. Tapping allows them to gain further insight into their experiences."));
					break;
				case BRASS:
					pages.add(new BookStuff.TextPage("A ferring who taps " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(ferringName) + "\", and stores Warmth. " +
							"While storing, the Firesoul will find themselves gaining a resistant to intense heat. Some reports say they can swim in lava. Tapping allows them to inflict burns onto others."));
					break;
				case COPPER:
					pages.add(new BookStuff.TextPage("A ferring who taps " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(ferringName) + "\", and stores Memories. " +
							"While storing, the Archivist is able to store their memories and experiences away. Tapping allows them to regain their stored knowledge."));
					break;
				case BRONZE:
					pages.add(new BookStuff.TextPage("A ferring who taps " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(ferringName) + "\", and stores Wakefulness. " +
							"While storing, the Sentry is able to fall asleep almost anywhere. Reports say that Tapping can keep away deadly creatures on this Shardworld called " + PatchouliTextFormat.Thing("Phantoms") + ". More research is required."));
					break;
				case ALUMINUM:
					pages.add(new BookStuff.TextPage("A ferring who taps " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(ferringName) + "\", and stores Identity. " +
							"A trueself is somewhat redundant by itself. Reports say that a feruchemist with access to both Feruchmical Aluminum and any other Feruchemical ability can store their identity before storing another attribute, creating what is called an Unkeyed Metalmind."));
					pages.add(new BookStuff.TextPage("This would allow others to access the reserves stored in this metalmind. More research is required."));
					break;
				case DURALUMIN:
					pages.add(new BookStuff.TextPage("A ferring who taps " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(ferringName) + "\", and stores Connection. " +
							"While storing, a Connector can make themselves more difficult for the native creatures of this shardworld to detect. Tapping allows them to create a closer bond with the native Villagers, granting them better trades. More research is required."));
					break;
				case CHROMIUM:
					pages.add(new BookStuff.TextPage("A ferring who taps " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(ferringName) + "\", and stores Fortune. " +
							"While storing, a Spinner will find that things left to chance generally end up against them. Ores they mine drop less usable chunks, Animals can't be harvested for as much usable meat, etc. Tapping seems to make fate bend in their favor."));
					break;
				case NICROSIL:
					pages.add(new BookStuff.TextPage("A ferring who taps " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(ferringName) + "\", and stores Investiture. " +
							"Little is known- more research required. (Not yet implemented)"));
					break;
				case CADMIUM:
					pages.add(new BookStuff.TextPage("A ferring who taps " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(ferringName) + "\", and stores Breaths. " +
							"While storing, a Gasper will find it difficult to breathe. Storing too much may make them suffocate. tapping allows them to hold their breath for far longer periods of time."));
					break;
				case BENDALLOY:
					pages.add(new BookStuff.TextPage("A ferring who taps " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(ferringName) + "\", and stores Saturation. " +
							"While storing, a Subsumer is able to store away calories, which can be tapped at a later time."));
					break;
				case GOLD:
					pages.add(new BookStuff.TextPage("A ferring who taps " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(ferringName) + "\", and stores Health. " +
							"While storing, a Bloodmaker is able to store away health, growing frail and weak. Tapping allows them to recover from injuries with ease."));
					break;
				case ELECTRUM:
					pages.add(new BookStuff.TextPage("A ferring who taps " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(ferringName) + "\", and stores Determination. " +
							"While storing, a Pinnacle will find themselves growing more unsure. Tapping fills them with determination. Reports say that they can shrug off greater injury while tapping Determination. More research is required."));
					break;
				case ATIUM:
					pages.add(new BookStuff.TextPage("A ferring who taps " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(ferringName) + "\", and stores Age. " +
							"While storing age, the ferring will decrease in size. Tapping allows them to grow in size."));
					break;
			}

			final String itemLoc = String.format("feruchemy:%s", metalName);

			pages.add(new BookStuff.CraftingPage(itemLoc + Constants.RegNameStubs.RING + Constants.RegNameStubs.METALMIND));
			pages.add(new BookStuff.CraftingPage(itemLoc + Constants.RegNameStubs.BRACELET + Constants.RegNameStubs.METALMIND));
			pages.add(new BookStuff.CraftingPage(itemLoc + Constants.RegNameStubs.NECKLACE + Constants.RegNameStubs.METALMIND));

			entryForThisMetal.pages = pages.toArray(BookStuff.Page[]::new);
			entries.add(entryForThisMetal);
		}
	}
}
