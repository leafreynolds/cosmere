/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.patchouli;

import leaf.cosmere.api.Constants;
import leaf.cosmere.patchouli.data.BookStuff;
import leaf.cosmere.patchouli.data.PatchouliTextFormat;

import java.util.ArrayList;
import java.util.List;

public class PatchouliBasics
{
	public static void collect(List<BookStuff.Category> categories, List<BookStuff.Entry> entries)
	{
		//todo basic general section

		BookStuff.Category basicsCategory = new BookStuff.Category("basics",
				"In this section, I will teach you how to access and use your powers most effectively",
				"cosmere:tin_ingot");
		categories.add(basicsCategory);
		basicsCategory.sortnum = 10;

		// Start a page list
		List<BookStuff.Page> pages = new ArrayList<>();

		{        // Investiture Basics Entry
			BookStuff.Entry investitureBasics = new BookStuff.Entry("investiture_basics", basicsCategory, "cosmere:lerasatium_ingot");
			investitureBasics.priority = true;

			BookStuff.Page firstPage = new BookStuff.TextPage();
			firstPage.setTitle("Investiture (For Dummies)");
			firstPage.setText("%s is a concept functioning like matter or energy. It makes up 16 godlike beings called %s, as well as many other mystical or magical things. ".formatted(PatchouliTextFormat.Thing("Investiture"),PatchouliTextFormat.Thing("Shards")) +
					"Investiture permeates all matter and can function as a power source for several types of magics we call \"%s.\" You may already have access to one or more of these arts.".formatted(PatchouliTextFormat.Thing("Invested Arts")));
			pages.add(firstPage);
			BookStuff.Page secondPage = new BookStuff.TextPage();
			secondPage.setTitle("Origins of Investiture");
			secondPage.setText("Once, all investiture belonged to one powerful being called \"%s,\" but eventually 17 people decided to work together to kill him. ".formatted(PatchouliTextFormat.Thing("Adonalsium")) +
					"They gathered four powerful artifacts made from investiture known as the \"%s\" and killed him with them through means still unknown to us. ".formatted(PatchouliTextFormat.Thing("Dawnshards")) +
					"After Adonalsium was killed, his investiture was broken into 16 Shards and each of the 17 people picked a shard and took it up, except for one, a man named \"%s.\" ".formatted(PatchouliTextFormat.Thing("Hoid")) +
					"You may even see Hoid on your travels.");
			pages.add(secondPage);

			investitureBasics.pages = pages.toArray(BookStuff.Page[]::new);
			pages.clear();
			entries.add(investitureBasics);
		}
		{       //Single activators entry
			BookStuff.Entry activatingPowersEntry = new BookStuff.Entry("activating_powers", basicsCategory, "cosmere:steel_ingot");
			activatingPowersEntry.priority = true;

			BookStuff.Page firstPage = new BookStuff.TextPage();
			firstPage.setTitle("Activating Powers");
			firstPage.setText("The first way I will teach you to activate powers is with the basic keybinds. This is not the most effective way of doing it, but it is straightforward.\n" +
					"First, locate the icon for your current main power in the upper left corner. By pressing (" + PatchouliTextFormat.Keybind(Constants.Strings.KEY_MANIFESTATION_NEXT) + ") and (" + PatchouliTextFormat.Keybind(Constants.Strings.KEY_MANIFESTATION_PREVIOUS) +
					") you can change which power is your current main. From there, press (" + PatchouliTextFormat.Keybind(Constants.Strings.KEY_MANIFESTATION_MODE_INCREASE) + ") and (" + PatchouliTextFormat.Keybind(Constants.Strings.KEY_MANIFESTATION_MODE_DECREASE) +
					") to change the mode for that power by plus or minus one respectively. If you are holding (" + PatchouliTextFormat.Keybind("Shift") + ") while you are changing the mode this way, it will change by plus or minus five levels, " +
					"If instead you are holding (" + PatchouliTextFormat.Keybind("control") + "()) while you are changing the mode this way, it will change by plus or minus ten levels or to the maximum, whichever is lower. " +
					"Changing your current main power will not stop your other powers from running in the background.");
			pages.add(firstPage);

			BookStuff.Page firstAndHalfPage = new BookStuff.TextPage();
			firstAndHalfPage.setText("If you are holding (" + PatchouliTextFormat.Keybind("Shift") + ") while you are changing the mode this way, it will change by plus or minus five levels, " +
					"If instead you are holding (" + PatchouliTextFormat.Keybind("Control") + "("+ PatchouliTextFormat.Keybind("Command") + " on Apple)) while you are changing the mode this way, it will change by plus or minus ten levels or " +
					"to the maximum, whichever is lower. In addition if you crouch with an empty hand, you can change your power mode by scrolling." +
					"Changing your current main power will not stop your other powers from running in the background.");

			BookStuff.Page secondPage = new BookStuff.TextPage();
			secondPage.setTitle("The Menu");
			secondPage.setText("The most accessible way to activate powers is from the menu. To access the menu, press and hold (" + PatchouliTextFormat.Keybind(Constants.Strings.KEY_MANIFESTATION_MENU) +
					"). From here, left clicking on a power will increase its mode by one, and right clicking will decrease it. Releasing (" + PatchouliTextFormat.Keybind(Constants.Strings.KEY_MANIFESTATION_MENU) + ") will close the menu.");
			pages.add(secondPage);

			BookStuff.Page thirdPage = new BookStuff.TextPage();
			thirdPage.setTitle("Dedicated Keybinds");
			thirdPage.setText("The fastest way to activate one power quickly is with its dedicated keybind. These are not set by default, but you can change your favorites to activate this way. " +
					"When this keybind is used, it will set the mode of your power to +1, unless the power is feruchemical in nature, in which case it will tap at level 5 instead. " +
					"However, in all cases, if the power is already active, no matter the mode, pressing the keybind will turn the power off instead. Using a dedicated keybind will also set your active main power to the associated power as well");
			pages.add(thirdPage);


			BookStuff.Page fourthPage = new BookStuff.TextPage();
			fourthPage.setTitle("Power Mode Save Keybind");
			fourthPage.setText("(Note: this is a recent and complicated feature. It may have bugs. Please report all bugs to the bug tracker.)$(br)By far the most complicated way to activate your powers is with the Power Mode Save States. You have access to nine different ones. " +
					"To save a new save state, activate all desired powers at the desired levels through any of the means previously described, and ensure all others are turned off. Then hold (" + PatchouliTextFormat.Keybind(Constants.Strings.KEY_SAVE_ACTIVATOR) + ") and press one of your hotbar slots.$(br2)" +
					"All active powers are now saved to that key.");
			pages.add(fourthPage);
			BookStuff.Page fifthPage = new BookStuff.TextPage();
			fifthPage.setText("To load a preexisting save state, hold (" + PatchouliTextFormat.Keybind(Constants.Strings.KEY_ACTIVATE) + ") and press the hotbar key for the desired save slot. Your powers should now be activated. If they are not, press it again. " +
					"(This is a known bug.) You can also toggle a save state off by pressing the keybind again. Powers that were not on when the save state was saved will not be affected by loading the save state.");
			pages.add(fifthPage);

			BookStuff.Page lastPage = new BookStuff.TextPage();
			lastPage.setTitle("Deactivating Powers");
			lastPage.setText("To quickly deactivate all powers you have active, press (" + PatchouliTextFormat.Keybind(Constants.Strings.KEY_DEACTIVATE_ALL_POWERS) + ").");
			pages.add(lastPage);

			activatingPowersEntry.pages = pages.toArray(BookStuff.Page[]::new);
			pages.clear();
			entries.add(activatingPowersEntry);
		}

	}
}
