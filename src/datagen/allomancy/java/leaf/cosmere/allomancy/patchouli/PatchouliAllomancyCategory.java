/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
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

		{        // Allomancy Basics Entry
			BookStuff.Entry allomancyBasics = new BookStuff.Entry("allomancy_basics", allomancyCategory, "allomancy:metal_vial");
			allomancyBasics.priority = true;

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
							"For someone who has access to all sixteen abilities, they are referred to as a \"%s\".".formatted(PatchouliTextFormat.Thing("Mistborn")) +
							" Someone being born a Mistborn is extremely rare, but not unheard of.");
			pages.add(secondPage);

			pages.add(new BookStuff.CraftingPage("allomancy:metal_vial"));
			allomancyBasics.pages = pages.toArray(BookStuff.Page[]::new);
			pages.clear();
			entries.add(allomancyBasics);
		}

		{        // coin pouch
			BookStuff.Entry coinPouchEntry = new BookStuff.Entry("coin_pouch", allomancyCategory, "allomancy:coin_pouch");
			coinPouchEntry.priority = true;

			BookStuff.Page firstPage = new BookStuff.TextPage();
			firstPage.setTitle("Coin Pouch");
			firstPage.setText(
					"The coin pouch is the most common way of ammo for your steelpushing abilities. " +
							"Crouch (" + PatchouliTextFormat.Keybind("key.sneak") + ") and use (" + PatchouliTextFormat.Keybind("key.use") + ") to add metal nuggets to it." +
							" Don't crouch if you want to shoot ammo with steelpushes. Hold the steelpush keybind (" + PatchouliTextFormat.Keybind("key.cosmere.allomancy.push") + ") and then tap (" + PatchouliTextFormat.Keybind("key.use") + ") to shoot. " +
							"If it gets stuck in a block, you will automatically get pushed away." +
							"If you hit a mob and keep pushing both you and the mob will be pushed away relative to your respective weights."
			);
			pages.add(firstPage);

			pages.add(new BookStuff.CraftingPage("allomancy:coin_pouch"));
			pages.add(new BookStuff.RelationsPage("", "cosmere:allomancy/allomantic_steel")); // links to cosmere book, coin_pouch entry
			coinPouchEntry.pages = pages.toArray(BookStuff.Page[]::new);
			pages.clear();
			entries.add(coinPouchEntry);
		}

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
			String[] physicalMetals = {
					"cosmere:allomancy/allomantic_steel",
					"cosmere:allomancy/allomantic_iron",
					"cosmere:allomancy/allomantic_pewter",
					"cosmere:allomancy/allomantic_tin"
			};
			String[] mentalMetals = {
					"cosmere:allomancy/allomantic_zinc",
					"cosmere:allomancy/allomantic_brass",
					"cosmere:allomancy/allomantic_copper",
					"cosmere:allomancy/allomantic_bronze"
			};
			String[] enhancementMetals = {
					"cosmere:allomancy/allomantic_aluminum",
					"cosmere:allomancy/allomantic_duralumin",
					"cosmere:allomancy/allomantic_chromium",
					"cosmere:allomancy/allomantic_nicrosil"
			};
			String[] temporalMetals = {
					"cosmere:allomancy/allomantic_cadmium",
					"cosmere:allomancy/allomantic_bendalloy",
					"cosmere:allomancy/allomantic_gold",
					"cosmere:allomancy/allomantic_electrum"
			};

			switch (metalType)
			{
				case IRON:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\"." +
							" Burning Iron allows you to pull metal objects towards you." +
							"$(br2)Iron is the external pulling metal and one of four physical metals (Alongside Steel, Pewter, and Tin) "));
					pages.add(new BookStuff.TextPage("Ironpulling is affected by the normal laws of physics. If a lurcher were to pull on something that weighs more than them, they'd be pulled towards it. If they pulled on something lighter, the object would move. If both were of similar weight, they'd both move." +
							" A lurcher can pull on items that contain metal by pressing " + PatchouliTextFormat.Keybind("key.cosmere.allomancy.pull")));
					pages.add(new BookStuff.RelationsPage("", "The physical metals:",physicalMetals));
					break;
				case STEEL:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". Steel is an Allomantic metal and an alloy of Iron. When burnt, Steel allows the user to push against nearby metal objects. Steelpushing is affected by the laws of physics. If a coinshot were to push on something that weighs more than them, they'd be pushed away. "));
					pages.add(new BookStuff.TextPage("If they pushed on something lighter, the object would move. Clever use of Steelpushing can lead to a pseudo flight through use of small metal objects, such as coins or nuggets. Many coinshots generally carry around a pouch of coins, for ease of access. A Coinshot can access their abilities by pressing " + PatchouliTextFormat.Keybind("key.cosmere.allomancy.push") +
							" Steel is the external pushing metal and one of four physical metals (alongside Iron, Pewter, and Tin)"));
					//pages.add(new BookStuff.CraftingPage("allomancy:coin_pouch")); -- redirect to coin pouch page instead.
					pages.add(new BookStuff.RelationsPage("", "cosmere:allomancy/coin_pouch")); // links to cosmere book, coin_pouch entry
					pages.add(new BookStuff.RelationsPage("", "The physical metals:",physicalMetals));
					break;
				case TIN:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"In allomancy, Tin heightens the senses to super human levels. It allows you to see clearly in the dark, and the mists. There might be more here to discover..." +
							" Tin is the internal pulling metal and one of four physical metals (alongside Iron, Pewter, and Steel)"));
					pages.add(new BookStuff.RelationsPage("", "The physical metals:",physicalMetals));
					break;
				case PEWTER:
					pages.add(new BookStuff.TextPage("A misting who burns" + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"While burning pewter, a pewterarm becomes faster, considerably stronger, and more resistant to punishment. However, they need to be careful because if they run out of pewter to burn, they will immediately feel all the pain they were shrugging off. This can mean instant death in extreme cases. Pewter is the internal pushing metal and one of the four physical metals (alongside Tin, Iron, and Steel)" +
							" [Do note that there isn't currently a way to tell how much damage you take when you stop burning pewter, be cautious.]"));
					pages.add(new BookStuff.RelationsPage("", "The physical metals:",physicalMetals));
					//"The major problem with pewter is that when it runs out, a large portion of the pain and injury that you resisted using the pewter hits you at once, potentially resulting in death. $(#f00)(NYI)$()";
					break;
				case ZINC:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"A creature being manipulated by a rioter will act far more aggressive, turning even the most pacified of animals into bloodthirsty beasts." + " A rioter can use their powers by pressing either " + PatchouliTextFormat.Keybind("key.cosmere.allomacy.riot") + " or " + PatchouliTextFormat.Keybind("key.cosmere.manifestation.use.active") + "."));
					pages.add(new BookStuff.TextPage("Rioters have reported recently that their allomancy has been acting different, resulting in creatures that seem confused. (This feature is bugged.) " +
							"Zinc is the external pulling metal and one of four mental metals (alongside Brass, Copper, and Bronze)"));
					pages.add(new BookStuff.RelationsPage("", "The mental metals:",mentalMetals));
					break;
				case BRASS:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"A creature being manipulated by a soother will act far more pacified, reducing their willingness to engage in combat." +
							" A soother can use their powers by pressing either " + PatchouliTextFormat.Keybind("k:cosmere.allomacy.soothe") + " or " + PatchouliTextFormat.Keybind("key.cosmere.manifestation.use.active") + "."));
					pages.add(new BookStuff.TextPage("Soothers have reported recently that their allomancy has been acting different, resulting in creatures that seem confused. (This feature is bugged.) " +
							"Brass is the external pushing metal and one of four mental metals (alongside Zinc, Copper, and Bronze)"));
					pages.add(new BookStuff.RelationsPage("", "The mental metals:",mentalMetals));
					break;
				case COPPER:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + "is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"$(br)A smoker burning copper generates a 'coppercloud,' which hides Allomancy from being detected by Bronze. Although I've never witnessed it, I've heard that a seeker of considerable strength could pierce a coppercloud. More research is required." +
							"$(br)Copper is the internal pulling metal and one of four mental metals (alongside Zinc, Brass, and Bronze)"));
					pages.add(new BookStuff.RelationsPage("", "The mental metals:",mentalMetals));
					break;
				case BRONZE:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"A seeker burning bronze is able to detect the nearby use of allomancy or feruchemy. A skilled seeker is capable of detecting what specific abilities are being used. Keep in mind that a smoker can neutralise the ability for a seeker to track allomancy or feruchemy. " +
							"Bronze is the internal pushing metal and one of four mental metals (alongside Brass, Copper, and Zinc)"));
					pages.add(new BookStuff.TextPage("[If you use the Jade mod, it shows you the metal the creature is burning, though you can do this on sound alone]"));
					//"Copper neutralises the ability for a Seeker to track allomancy by hiding it in a copper cloud, but extremely powerful Seekers or Mistborn, may still be able to pierce said shields.";
					pages.add(new BookStuff.RelationsPage("", "The mental metals:",mentalMetals));
					break;
				case ALUMINUM:
					pages.add(new BookStuff.TextPage("A misting who can only burn " + PatchouliTextFormat.Thing(metalName) + " is known as an $(br)\"" + PatchouliTextFormat.Thing(mistingName) + "\" as they gain no discernible effect from burning their metal." +
							"$(br)Burning aluminum will wipe all their metal reserves, including aluminum." + "I have heard rumors that an Aluminum Gnat of considerable power could clear the effects of other sources of investiture or impurities from their bodies, " +
							"but this has not been confirmed to me. (Not yet implemented)" +
							"$(br)Aluminum is the internal pulling metal and one of four enhancement metals (alongside Duralumin, Chromium, and Nicrosil)"));
					pages.add(new BookStuff.RelationsPage("", "The enhancement metals:", enhancementMetals));
					break;
				case DURALUMIN:
					//add extra note so that these people will know of their shame.
					pages.add(new BookStuff.TextPage("A misting who can only burn " + PatchouliTextFormat.Thing(metalName) + " is known as a $(br)\"" + PatchouliTextFormat.Thing(mistingName) + "\" as they gain no discernible effect from burning their metal. The metal reserves of a misting burning duralumin will deplete faster, but will have a much more prominent effect. You might note that this feature is useless on its own, as it needs to be used in tandom with a second metal."));
					pages.add(new BookStuff.TextPage("Duralumin is the internal pushing metal and one of four enhancement metals (alongside Aluminum, Chromium, and Nicrosil"));
					pages.add(new BookStuff.RelationsPage("", "The enhancement metals:", enhancementMetals));
					break;
				case CHROMIUM:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"A leecher can deplete the metal reserves of another allomancer with physical contact, as if the target were burning aluminum themselves." +
							" Chromium is the external pulling metal and one of four enhancement metals (alongside Aluminum, Duralumin, and Nicrosil)"));
					pages.add(new BookStuff.RelationsPage("", "The enhancement metals:", enhancementMetals));
					break;
				case NICROSIL:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\"." +
							"$(br)A nicroburst can empower the allomancy of another with physical contact, as if the target were burning duralumin themselves. This can be used to throw other allomancers off, such as giving a coinshot a particularly strong steelpush when they don't expect it."+
							" Nicrosil is the external pushing metal and one of four enhancement metals (alongside Aluminum, Chromium, and Duralumin)"));
					pages.add(new BookStuff.RelationsPage("", "The enhancement metals:", enhancementMetals));
					break;
				case CADMIUM:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"A pulser creates a bubble around them in which time slows down. To anyone inside this time bubble, everything outside appears to move faster." +
							" Cadmium is the external pulling metal and one of four temporal metals (alongside Bendalloy, Electrum, and Gold)" +
							" [Cadmium doesn't work properly yet and is still in development]"));
					pages.add(new BookStuff.RelationsPage("", "The temporal metals:", temporalMetals));
					break;
				case BENDALLOY:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"A slider creates a bubble around them in which time speeds up. To anyone inside of the time bubble, everything outside appears to move much more slowly." +
							" Bendalloy is the external pushing metal and one of four temporal metals (alongside Cadmium, Electrum, and Gold)" +
							" [Bendalloy doesn't work properly yet and is still in development]"));
					pages.add(new BookStuff.RelationsPage("", "The temporal metals:", temporalMetals));
					break;
				case GOLD:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"Gold is the internal pulling metal and one of four temporal metals (alongside Bendalloy, Cadmium, and Electrum)"+
							" The rest of the page is scratched out. (Not yet implemented)"));
					pages.add(new BookStuff.RelationsPage("", "The temporal metals:", temporalMetals));
					break;
				case ELECTRUM:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". " +
							"Most of the page is missing... (Not yet fully implemented)" +
							" The primary use for electrum is to counter the godmetal Atium's ability to see your attacks and thus dodge out of the way. This metal evens the playing field." +
							" Electrum is the internal pushing metal and one of four temporal metals (alongside Bendalloy, Cadmium, and Gold)"));
					pages.add(new BookStuff.RelationsPage("", "The temporal metals:", temporalMetals));
					break;
				case ATIUM:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\". $(br)" +
							"Most of the page is missing... (Not yet fully implemented)$(br)The metal atium is extraordinarily rare. Seers who can burn it can see into the future a small amount, and thus can dodge every attack that might befall them (unless the attacker is burning electrum of course)."));
					pages.add(new BookStuff.TextPage("Interestingly atium doesnt fit the normal metal categories... $(br)I will have to further pursue the subject."));
					break;
			}
			entryForThisPower.pages = pages.toArray(BookStuff.Page[]::new);
			entries.add(entryForThisPower);
		}
	}
}
