/*
 * File created ~ 13 - 6 - 2022 ~ Leaf
 */

package leaf.cosmere.datagen.compat.patchouli.categories;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.datagen.compat.patchouli.PatchouliTextFormat;
import leaf.cosmere.datagen.compat.patchouli.BookStuff;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.allomancy.AllomancyBase;
import leaf.cosmere.utils.helpers.StringHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

import static leaf.cosmere.registry.ManifestationRegistry.ALLOMANCY_POWERS;

public class PatchouliAllomancy
{
	public static void collect(List<BookStuff.Category> categories, List<BookStuff.Entry> entries)
	{
		BookStuff.Category allomancy = new BookStuff.Category(
				"allomancy",
				"This is the art of consuming a piece of metal related to your power, and then \"%s\" it for an effect.".formatted(PatchouliTextFormat.Thing("burning")),
				"cosmere:pewter_nugget");
		allomancy.sortnum = 1;


		categories.add(allomancy);


		BookStuff.Entry allomancyBasics = new BookStuff.Entry("allomancy_basics", allomancy, allomancy.icon);
		allomancyBasics.pages = new BookStuff.Page[]
				{
						new BookStuff.TextPage("If you entered this world with an allomantic ability, you're called a $(6)misting$().", allomancy.icon),
						new BookStuff.CraftingPage("Metal vial", "cosmere:metal_vial"),
				};
		allomancyBasics.priority = true;
		List<BookStuff.Page> pages = new ArrayList<>();

		entries.add(allomancyBasics);

		//allomancy
		for (RegistryObject<AllomancyBase> manifestation : ALLOMANCY_POWERS.values())
		{
			AManifestation aManifestation = manifestation.get();
			AllomancyBase allomancyManifestation = (AllomancyBase) aManifestation;
			Metals.MetalType metalType = allomancyManifestation.getMetalType();

			if (!metalType.hasFeruchemicalEffect())
			{
				continue;
			}

			String metalName = metalType.getName();

			String itemFullName = metalType.getNuggetItem().getRegistryName().toString();

			BookStuff.Entry entryForThisPower = new BookStuff.Entry(
					"allomantic_" + metalName,
					allomancy,
					itemFullName);
			entryForThisPower.sortnum = metalType.getID();

			pages.clear();
			String mistingName = StringHelper.fixCapitalisation(metalType.getMistingName());


			switch (metalType)
			{
				default:
					pages.add(new BookStuff.TextPage("A misting who burns " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\"."));
					break;
				case ALUMINUM:
				case DURALUMIN:
					//add extra note so that these people will know of their shame.
					pages.add(new BookStuff.TextPage("A misting who can only burn " + PatchouliTextFormat.Thing(metalName) + " is known as a \"" + PatchouliTextFormat.Thing(mistingName) + "\" as they gain no discernible effect from burning their metal."));
					break;
			}

			pages.add(new BookStuff.TextPage(GetAllomancyDescription(metalType)));

			entryForThisPower.pages = pages.toArray(BookStuff.Page[]::new);
			entries.add(entryForThisPower);
		}
	}


	public static String GetAllomancyDescription(Metals.MetalType metalType)
	{
		StringBuilder builder = new StringBuilder();
		switch (metalType)
		{
			case IRON:
				builder.append("Iron is an Allomantic metal that, when burned, allows the user to Pull on various metal objects around them, pulling any of these objects towards them.");//+
			//"The burning of Iron is affected by normal laws of physics, so when the user pulls upon something with more weight, it will take more iron to move this, and if the user pulls upon something with more weight than themselves, they will be pulled through the air towards the aforementioned object. " +
			//"The burning of Iron is used to fly, manipulate various objects, pull someone towards you (assuming that they have metal on their person), disarm somebody with a metal weapon, change the flight path of flying coins, and for a multitude of other things.";

			break;
			case STEEL:
				builder.append("Steel is an Allomantic metal and an alloy of Iron. When burnt, Steel allows the user to telekinetically push against a metal object.");//+
			//"Conservation of momentum still applies here, so if you trying to push against blocks, expect to be thrown back in the opposite direction." +
			//"Clever Allomancers use steel to allow themselves to fly through the air.";

			break;
			case TIN:
				builder.append("In allomancy, tin heightens the senses to super human levels. In this world, burning tin allows the allomancer to see clearly in the dark and detect where sounds are coming from.");

			break;
			case PEWTER:
				builder.append("Pewter is an allomantic metal that, when burnt, gives the user extreme strength, resistance and durability. Its uses include combat, moving quickly, surviving attacks and healing oneself.");//+
			//"The major problem with pewter is that when it runs out, a large portion of the pain and injury that you resisted using the pewter hits you at once, potentially resulting in death. $(#f00)(NYI)$()";

			break;
			case ZINC:
				builder.append("In allomancy, zinc gives the ability to intensify the emotions of others, an ability called \"rioting\"");

			break;
			case BRASS:
				builder.append("In allomancy, brass gives the ability to \"soothe\" the emotions of others.");

			break;
			case COPPER:
				builder.append("Copper is an elemental Allomantic metal that allows one to hide Allomancy. A misting or mistborn burning copper generates a 'coppercloud' which hides Allomancy from being detected by Bronze. ");//+
			//"Copperclouds are generally not piercible but those with exceptional strength in Bronze may do so.";

			break;
			case BRONZE:
				builder.append("When burning bronze, either Misting or Mistborn, the user can feel the uses of allomancy near them.");//+
			//"Copper neutralises the ability for a Seeker to track allomancy by hiding it in a copper cloud, but extremely powerful Seekers or Mistborn, may still be able to pierce said shields.";

			break;
			case ALUMINUM:
				builder.append("Aluminum is an internal enhancement metal that, when burned, clears out all the metals inside the allomancer, including itself.");

			break;
			case DURALUMIN:
				builder.append("An allomancer that burns duralumin causes an amazing burst of power from all currently burning metals, draining all of them rapidly.");

			break;
			case CHROMIUM:
				builder.append("Chromium allows the burner to, with physical contact, deplete the metals in another with an effect similar to that of $(l:cosmere:allomancy/allomantic_aluminum)aluminum$().");

			break;
			case NICROSIL:
				builder.append("It allows the burner to, with physical contact with another allomancer, have an effect on them as if they were burning $(l:cosmere:allomancy/allomantic_duralumin)duralumin$() themselves.");

			break;
			case CADMIUM:
				builder.append("Cadmium allows the burner to pull on time in a bubble around them, making time pass slowly within the bubble.");

			break;
			case BENDALLOY:
				builder.append("Bendalloy allows the burner to push on time in a bubble around them, making time pass quickly within the bubble. Expect to see your furnaces finish more quickly or crops grow faster!");

			break;
			case GOLD:
			case ELECTRUM:
			default:
				builder.append("$(#f00)(NYI)$()");
				break;
		}
		return builder.toString();
	}
}
