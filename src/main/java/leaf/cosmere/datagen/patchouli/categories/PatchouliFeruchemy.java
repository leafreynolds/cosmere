/*
 * File created ~ 14 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.patchouli.categories;

import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.datagen.patchouli.BookStuff;
import leaf.cosmere.datagen.patchouli.PatchouliTextFormat;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.feruchemy.FeruchemyBase;
import leaf.cosmere.utils.helpers.StringHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

import static leaf.cosmere.registry.ManifestationRegistry.FERUCHEMY_POWERS;

public class PatchouliFeruchemy
{
	public static void collect(List<BookStuff.Category> categories, List<BookStuff.Entry> entries)
	{
		BookStuff.Category feruchemy = new BookStuff.Category(
				"feruchemy",
				"The art of equivalent exchange when it comes to the body. ",
				"cosmere:copper_bracelet_metalmind");
		feruchemy.sortnum = 2;
		categories.add(feruchemy);

		List<BookStuff.Page> pages = new ArrayList<>();

		BookStuff.Entry feruchemyBasics = new BookStuff.Entry("feruchemy_basics", feruchemy, feruchemy.icon);
		feruchemyBasics.priority = true;
		pages.add(new BookStuff.TextPage("If you entered this world with an feruchemical ability, you're called a $(6)ferring$().", feruchemy.icon));
		pages.add(new BookStuff.CraftingPage("cosmere:brass_bracelet_metalmind").setText("Depending on what kind of metal you have access to, a metalmind of the corresponding metal will let you store and tap attributes."));

		feruchemyBasics.pages = pages.toArray(BookStuff.Page[]::new);
		entries.add(feruchemyBasics);

		//feruchemy
		for (RegistryObject<AManifestation> manifestation : FERUCHEMY_POWERS.values())
		{
			AManifestation aManifestation = manifestation.get();
			FeruchemyBase feruchemyManifestation = (FeruchemyBase) aManifestation;
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
							"Little known- more research required."));
					break;
				case CADMIUM:
					pages.add(new BookStuff.TextPage("A ferring who taps " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(ferringName) + "\", and stores Breaths. " +
							"While storing, a Gasper will find it difficult to breath. Storing too much may make them suffocate. tapping allows them to hold their breath for far longer periods of time."));
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

			final String itemLoc = String.format("cosmere:%s", metalName);

			pages.add(new BookStuff.CraftingPage(itemLoc + Constants.RegNameStubs.BRACELET + Constants.RegNameStubs.METALMIND));
			pages.add(new BookStuff.CraftingPage(itemLoc + Constants.RegNameStubs.RING + Constants.RegNameStubs.METALMIND));
			pages.add(new BookStuff.CraftingPage(itemLoc + Constants.RegNameStubs.NECKLACE + Constants.RegNameStubs.METALMIND));

			entryForThisMetal.pages = pages.toArray(BookStuff.Page[]::new);
			entries.add(entryForThisMetal);
		}
	}
}
