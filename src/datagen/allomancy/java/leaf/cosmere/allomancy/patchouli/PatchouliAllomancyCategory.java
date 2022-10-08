/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.patchouli;

import leaf.cosmere.allomancy.common.manifestation.AllomancyManifestation;
import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.Constants;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.patchouli.data.BookStuff;
import leaf.cosmere.patchouli.data.PatchouliTextFormat;

import java.util.ArrayList;
import java.util.List;

public class PatchouliAllomancyCategory
{
	public static void collect(List<BookStuff.Category> categories, List<BookStuff.Entry> entries)
	{
		BookStuff.Category allomancyCategory = new BookStuff.Category(
				"allomancy",
				"Despite first documentation only occurring approximately one thousand years before the Catacendre, Allomancy has been explored in far greater detail than it's older counterparts. The art requires consuming a piece of metal, then burning it for an effect.",
				"cosmere:pewter_nugget");
		categories.add(allomancyCategory);
		allomancyCategory.sortnum = 1;

		// Start a page list
		List<BookStuff.Page> pages = new ArrayList<>();
		// Allomancy Basics Entry
		BookStuff.Entry allomancyBasics = new BookStuff.Entry("allomancy_basics", allomancyCategory, "allomancy:metal_vial");


		BookStuff.Page firstPage = new BookStuff.TextPage();
		firstPage.setTitle("Allomancy (For Dummies)");
		firstPage.setText(
				"Here is where I shall explain Allomancy, a system of Investiture native to Scadrial. " +
						"Despite first documentation only occurring approximately one thousand years before the Catacendre, Allomancy has been explored in much greater detail than it's two older counterparts.");
		pages.add(firstPage);
		BookStuff.Page secondPage = new BookStuff.TextPage();
		secondPage.setTitle("Allomancy (For Dummies)");
		secondPage.setText(
				"The art requires consuming a piece of metal, upon which time you can \"%s\" it. ".formatted(PatchouliTextFormat.Thing("Burn")) +
						"Those who are genetically gifted with a singular allomantic ability are generally referred to as a \"%s\". ".formatted(PatchouliTextFormat.Thing("Misting")) +
						"For someone who has access to all sixteen abilities, they are referred to as a \"%s\".".formatted(PatchouliTextFormat.Thing("Mistborn")));
		pages.add(secondPage);

		pages.add(new BookStuff.CraftingPage("allomancy:metal_vial"));
		allomancyBasics.pages = pages.toArray(BookStuff.Page[]::new);
		pages.clear();
		entries.add(allomancyBasics);
		allomancyBasics.priority = true;

		//allomancy
		for (ManifestationRegistryObject<AllomancyManifestation> manifestationRegistryObject : AllomancyManifestations.ALLOMANCY_POWERS.values())
		{
			AllomancyManifestation manifestation = manifestationRegistryObject.get();
			Metals.MetalType metalType = manifestation.getMetalType();

			if (!metalType.hasFeruchemicalEffect())
			{
				continue;
			}

			String metalName = metalType.getName();

			String namespace = metalType.hasMaterialItem() ? "cosmere:" : "minecraft:";
			String itemFullName = namespace + metalType.getName() + Constants.RegNameStubs.NUGGET;

			BookStuff.Entry entryForThisPower = new BookStuff.Entry(
					"allomantic_" + metalName,
					allomancyCategory,
					itemFullName);
			entryForThisPower.sortnum = metalType.getID();

			pages.clear();
			String mistingName = StringHelper.fixCapitalisation(metalType.getMistingName());


			switch (metalType)
			{
				case IRON:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"Ironpulling is affected by the normal laws of physics. If a lurcher were to pull on something that weighs more than them, they'd be pulled towards it. If they pulled on something lighter, the object would move. If both were of similar weight, they'd both move."));
					break;
				case STEEL:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". Steel is an Allomantic metal and an alloy of Iron. When burnt, Steel allows the user to push against nearby metal objects. Steelpushing is affected by the laws of physics. If a coinshot were to push on something that weighs more than them, they'd be pushed away. "));
					pages.add(new BookStuff.TextPage("If they pushed on something lighter, the object would move. Clever use of Steelpushing can lead to a pseudo flight through use of small metal objects, such as coins or nuggets. Many coinshots generally carry around a pouch of coins, for ease of access."));
					pages.add(new BookStuff.CraftingPage("allomancy:coin_pouch"));
					break;
				case TIN:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"In allomancy, Tin heightens the senses to super human levels. It allows you to see clearly in the dark, and- (the page appears to be smudged, the letters indecipherable.)"));
					break;
				case PEWTER:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"While burning pewter, a pewterarm becomes faster, considerably stronger, and more resistant to punishment."));
					//"The major problem with pewter is that when it runs out, a large portion of the pain and injury that you resisted using the pewter hits you at once, potentially resulting in death. $(#f00)(NYI)$()";
					break;
				case ZINC:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"A creature being manipulated by a rioter will act far more aggressive, turning even the most pacified of animals into bloodthirsty beasts."));
					break;
				case BRASS:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"A creature being manipulated by a soother will act far more pacified, reducing their willingness to engage in combat."));
					break;
				case COPPER:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"A smoker burning copper generates a 'coppercloud,' which hides Allomancy from being detected by Bronze. Although I've never witnessed it, I've heard that a seeker of considerable strength could pierce a coppercloud. More research is required."));
					break;
				case BRONZE:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"A seeker burning bronze is able to detect the nearby use of allomancy or feruchemy. A skilled seeker is capable of detecting what specific abilities are being used. Keep in mind that a smoker can neutralise the ability for a seeker to track allomancy or feruchemy."));
					//"Copper neutralises the ability for a Seeker to track allomancy by hiding it in a copper cloud, but extremely powerful Seekers or Mistborn, may still be able to pierce said shields.";
					break;
				case ALUMINUM:
					pages.add(new BookStuff.TextPage("A misting who can only burn " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\" as they gain no discernible effect from burning their metal. " +
							"A mistborn burning aluminum will wipe all their metal reserves, including aluminum."));
					break;
				case DURALUMIN:
					//add extra note so that these people will know of their shame.
					pages.add(new BookStuff.TextPage("A misting who can only burn " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\" as they gain no discernible effect from burning their metal. " +
							"The metal reserves of a mistborn burning duralumin will deplete faster, but will have a much more prominent effect."));
					break;
				case CHROMIUM:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"A leecher gazing upon an allomancer can deplete their metal reserves, as if they were burning aluminum themselves."));
					break;
				case NICROSIL:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"A nicroburst can empower the allomancy of another with physical contact, as if they were burning duralumin themselves."));
					break;
				case CADMIUM:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"a pulser creates a bubble around them in which everyone moves faster around them."));
					break;
				case BENDALLOY:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"A slider creates a bubble around them in which everyone moves slower around them."));
					break;
				case GOLD:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"(The rest of the page is scratched out.)"));
					break;
				case ELECTRUM:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"(The rest of the page is missing...)"));
					break;
				case ATIUM:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"(The rest of the page is blank...)"));
					break;
			}
			entryForThisPower.pages = pages.toArray(BookStuff.Page[]::new);
			entries.add(entryForThisPower);
		}
	}
}
