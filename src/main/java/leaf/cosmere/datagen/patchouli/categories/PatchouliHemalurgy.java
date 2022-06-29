/*
 * File created ~ 14 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.patchouli.categories;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.datagen.patchouli.BookStuff;
import leaf.cosmere.datagen.patchouli.PatchouliTextFormat;

import java.util.ArrayList;
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

		//Start a page list.
		List<BookStuff.Page> pages = new ArrayList<>();

		//hemalurgy basics entry
		BookStuff.Entry hemalurgyBasics = new BookStuff.Entry("hemalurgy_basics", hemalurgy, hemalurgy.icon).setDisplayTitle("Hemalurgy (For Dummies)");
		hemalurgyBasics.priority = true;


		BookStuff.Page terminologyPage = new BookStuff.TextPage();
		terminologyPage.setTitle("Terminology");
		terminologyPage.setText(
				"In this journal, I shall explain all I have discovered about Hemalurgy. $(br)" +
						"Firstly, some terminology:" +
						"$(li) \"%s\" is when you kill something with a metal spike, hemalurgically charging that spike.".formatted(PatchouliTextFormat.Thing("Spiking")) +
						"$(li) The \"%s\" is the one who is spiked, from whom the spike is hemalurgically charged.".formatted(PatchouliTextFormat.Thing("donor")) +
						"$(li) The \"%s\" is the one who receives the spike after it has been hemalurgically charged.".formatted(PatchouliTextFormat.Thing("recipient"))
		);
		pages.add(terminologyPage);

		BookStuff.Page rulesObservations = new BookStuff.TextPage();
		rulesObservations.setTitle("Rules & Observations");
		rulesObservations.setText(
				"During my research, I have been able to outline some general rules of Hemalurgy, and some general observations, which I have written below." +
						"$(li) The metal the spike is made out of determines the attribute or power taken from the donor." +
						"$(li) A single spike can only hold a single hemalurgic charge." +
						"$(li) Spiking a misting/ferring has a predictable effect, if the right metal is used for the spike, it steals their allomantic/feruchemical power, with which the hemalurgic spike is now charged." +
						"$()$(br) Cont. Next Page$(br)"

		);
		pages.add(rulesObservations);

		BookStuff.Page rulesObservationsCont = new BookStuff.TextPage();
		rulesObservationsCont.setTitle("Rules & Observations");
		rulesObservationsCont.setText(
				"$(li) Spiking full mistborn and feruchemists, in my experience, has inconsistent results." +
						" The power taken is always one from the relevant quadrant of the respective table, steel spikes taking physical allomantic powers, gold spikes taking hybrid feruchemical powers, and so on." +
						" The inconsistency lies in that the specific power stolen seems to be random, although there may be pattern to the apparent randomness - further research is necessary.$(br)"

		);
		pages.add(rulesObservationsCont);

		pages.add(new BookStuff.CraftingPage("Spike Recipe", "cosmere:steel_spike", "cosmere:pewter_spike"));


		hemalurgyBasics.pages = pages.toArray(BookStuff.Page[]::new);
		pages.clear();
		entries.add(hemalurgyBasics);

		//hemalurgy
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (!metalType.hasHemalurgicEffect())
			{
				continue;
			}
			//clear before use
			pages.clear();

			String metalName = metalType.getName();
			final String spikeItemLocation = String.format("cosmere:%s_spike", metalName);

			BookStuff.Entry entryForThisMetal = new BookStuff.Entry(
					"hemalurgic_" + metalName,
					hemalurgy,
					spikeItemLocation);
			entryForThisMetal.sortnum = metalType.getID();


			pages.add(new BookStuff.TextPage(getHemalurgicUse(metalType)));
			pages.add(new BookStuff.CraftingPage(spikeItemLocation));


			entryForThisMetal.pages = pages.toArray(BookStuff.Page[]::new);

			entries.add(entryForThisMetal);
		}
	}

	private static String getHemalurgicUse(Metals.MetalType metalType)
	{
		StringBuilder builder = new StringBuilder();
		final String allomanticPageLink = "cosmere:allomancy/allomantic_";
		final String feruchemicalPageLink = "cosmere:feruchemy/feruchemical_";

		switch (metalType)
		{
			case IRON:
				builder.append("Iron spikes steal physical strength: $(p)");
				builder.append("My research suggests that iron spikes transfer around 70%% of the donor's attack strength.");
				break;
			case STEEL://Iron//Steel//Tin//Pewter
			{
				builder.append("Steel spikes steal the physical allomantic powers: $(p)");
				String a = "Iron";
				String b = "Steel";
				String c = "Tin";
				String d = "Pewter";

				builder.append("Allomantically burning %s, %s, %s and %s."
						.formatted(
								PatchouliTextFormat.LinkEntry(a, allomanticPageLink + a),
								PatchouliTextFormat.LinkEntry(b, allomanticPageLink + b),
								PatchouliTextFormat.LinkEntry(c, allomanticPageLink + c),
								PatchouliTextFormat.LinkEntry(d, allomanticPageLink + d)
						));
			}
			break;
			case TIN:
				builder.append("Tin spikes steal the senses: $(p)");
				builder.append("As a result, tin spikes grant better vision in the dark. $(p)");
				builder.append("It seems that donors with a better sense of their surroundings provide a more potent effect.");
				break;
			case PEWTER://Iron//Steel//Tin//Pewter
			{
				builder.append("Pewter spikes steal the physical feruchemical powers: $(p)");
				String a = "Iron";
				String b = "Steel";
				String c = "Tin";
				String d = "Pewter";

				builder.append("Feruchemically storing and tapping %s, %s, %s and %s, the feruchemical attributes keyed to each of these are:  $(p)"
						.formatted(
								PatchouliTextFormat.LinkEntry(a, feruchemicalPageLink + a),
								PatchouliTextFormat.LinkEntry(b, feruchemicalPageLink + b),
								PatchouliTextFormat.LinkEntry(c, feruchemicalPageLink + c),
								PatchouliTextFormat.LinkEntry(d, feruchemicalPageLink + d)
						));
				builder.append("Weight(Slow Fall/Slowness, Resistance), Speed(Slowness/Speed), Senses(Blindness/Night Vision), and Strength(Weakness/Strength).");
			}
			break;
			case ZINC:
				builder.append("[Not Yet Implemented] $(p)");
				builder.append("My research has yet to determine any Hemalurgic properties of zinc - further research necessary.");
				break;
			case BRASS://Zinc//Brass//Copper//Bronze
			{
				builder.append("Brass spikes steal the cognitive feruchemical powers: $(p)");
				String a = "Zinc";
				String b = "Brass";
				String c = "Copper";
				String d = "Bronze";
				builder.append("Feruchemically storing and tapping %s, %s, %s and %s, the feruchemical attributes keyed to each of these are:  $(p)"
						.formatted(
								PatchouliTextFormat.LinkEntry(a, feruchemicalPageLink + a),
								PatchouliTextFormat.LinkEntry(b, feruchemicalPageLink + b),
								PatchouliTextFormat.LinkEntry(c, feruchemicalPageLink + c),
								PatchouliTextFormat.LinkEntry(d, feruchemicalPageLink + d)
						));
				builder.append("Mental Speed(XP Gain Rate), Warmth(Fire Resistance/Fire Aspect), Memories(Current XP), and Wakefulness(Phantom Spawn Timer). $(p)");
			}
			break;
			case COPPER:
				builder.append("Copper spikes steal mental fortitude, memory, and intelligence. $(p)");
				builder.append("As a result, they can be used to increase base XP gain rate. $(p)");
				builder.append("I wonder what would happen if you were to spike the Ender Dragon... Surely the first kill of such a beast would give a marvelous reward.");
				break;
			case BRONZE://Zinc//Brass//Copper//Bronze
			{
				builder.append("Bronze spikes steal the mental allomantic powers: $(p)");
				String a = "Zinc";
				String b = "Brass";
				String c = "Copper";
				String d = "Bronze";

				builder.append("Allomantically burning %s, %s, %s and %s."
						.formatted(
								PatchouliTextFormat.LinkEntry(a, allomanticPageLink + a),
								PatchouliTextFormat.LinkEntry(b, allomanticPageLink + b),
								PatchouliTextFormat.LinkEntry(c, allomanticPageLink + c),
								PatchouliTextFormat.LinkEntry(d, allomanticPageLink + d)
						));
			}
			break;
			case ALUMINUM:
				builder.append("Prevents recipient from using their powers.");
				break;
			case DURALUMIN:
				builder.append("In the past, hemalurgic scholars have theorised that duralumin spikes may transfer the donor's spiritual connection or identity. However, due to the rarity of duralumin during the eras they all worked in, all were unable to obtain enough duralumin to prove their theories. $(p)");
				builder.append("(The rest of the page appears to have been ripped out)");
				break;
			case CHROMIUM:
				builder.append("I have heard it said that Chromium spikes 'might steal destiny...', in my experience, they seem to affect one's luck. $(p)");
				builder.append("Like all hemalurgic spikes, the effect of a chromium spike depends on the donor, though in a different way to iron, for example: $(p)");
				builder.append("If the donor is a Rabbit, or completely White cat, the recipient would become 75%%, or 35%% luckier, respectively. $(p)");
				builder.append("In contrast, if the donor is a black cat, wither skeleton, or pufferfish, the recipient's luck would decrease, by 100%%, 55%%, or 5%%, respectively");
				break;
			case NICROSIL:
				builder.append("[Not Yet Implemented] $(p)");
				builder.append("Some theorise, due to the similarities hemalurgy shares with feruchemy, that nicrosil spikes steal investiture, potentially similarly to how soulbearer ferrings store investiture $(p)");
				builder.append("(The rest of this page has ink spilled on it, obscuring the writing)");
				break;
			case CADMIUM://Cadmium//Bendalloy//Gold//Electrum
			{
				builder.append("Cadmium spikes steal the temporal allomantic powers: $(p)");
				String a = "Gold";
				String b = "Electrum";
				String c = "Cadmium";
				String d = "Bendalloy";

				builder.append("Allomantically burning %s, %s, %s and %s."
						.formatted(
								PatchouliTextFormat.LinkEntry(a, allomanticPageLink + a),
								PatchouliTextFormat.LinkEntry(b, allomanticPageLink + b),
								PatchouliTextFormat.LinkEntry(c, allomanticPageLink + c),
								PatchouliTextFormat.LinkEntry(d, allomanticPageLink + d)
						));
			}
			break;
			case GOLD://Cadmium//Bendalloy//Gold//Electrum
			{
				builder.append("Gold spikes steal the hybrid feruchemical powers: $(p)");
				String a = "Gold";
				String b = "Electrum";
				String c = "Cadmium";
				String d = "Bendalloy";
				builder.append("Feruchemically storing and tapping %s, %s, %s and %s, the feruchemical attributes keyed to each of these are:  $(p)"
						.formatted(
								PatchouliTextFormat.LinkEntry(a, feruchemicalPageLink + a),
								PatchouliTextFormat.LinkEntry(b, feruchemicalPageLink + b),
								PatchouliTextFormat.LinkEntry(c, feruchemicalPageLink + c),
								PatchouliTextFormat.LinkEntry(d, feruchemicalPageLink + d)
						));
				builder.append("Healing(lose max health/regeneration), Determination(extra hearts), Breath(lose O2 outside water/reduce rate of O2 consumption), and Nutrition(Hunger/Saturation)");
			}
			break;
			case BENDALLOY://Chromium//Nicrosil//Aluminum//Duralumin
			{
				builder.append("Bendalloy spikes steal the spiritual feruchemical powers: $(p)");
				String a = "Aluminum";
				String b = "Duralumin";
				String c = "Chromium";
				String d = "Nicrosil";
				builder.append("Feruchemically storing and tapping %s, %s, %s and %s, the feruchemical attributes keyed to each of these are:  $(p)"
						.formatted(
								PatchouliTextFormat.LinkEntry(a, feruchemicalPageLink + a),
								PatchouliTextFormat.LinkEntry(b, feruchemicalPageLink + b),
								PatchouliTextFormat.LinkEntry(c, feruchemicalPageLink + c),
								PatchouliTextFormat.LinkEntry(d, feruchemicalPageLink + d)
						));
				builder.append("Identity(making identityless metalminds), Connection(Bad omen/Hero of the village), Fortune(Bad luck/luck), and Investiture(Charge can be used for tapping other metals)");
			}
			break;
			case ELECTRUM://Chromium//Nicrosil//Aluminum//Duralumin
			{
				builder.append("Electrum spikes steal the enhancement allomantic powers: $(p)");
				String a = "Aluminum";
				String b = "Duralumin";
				String c = "Chromium";
				String d = "Nicrosil";

				builder.append("Allomantically burning %s, %s, %s and %s."
						.formatted(
								PatchouliTextFormat.LinkEntry(a, allomanticPageLink + a),
								PatchouliTextFormat.LinkEntry(b, allomanticPageLink + b),
								PatchouliTextFormat.LinkEntry(c, allomanticPageLink + c),
								PatchouliTextFormat.LinkEntry(d, allomanticPageLink + d)
						));
			}
			break;
			case ATIUM:
				builder.append("Atium Spikes can steal any one Allomantic or Feruchemical power. For example, if a twinborn were spiked with an atium spike, that spike would only grant one of their powers.");
				break;
			case LERASIUM:
				builder.append("Lerasium Spikes steals all Attributes from a donor.");
			default:
				builder.append("Unknown...");
				break;
		}

		return builder.toString();
	}
}
