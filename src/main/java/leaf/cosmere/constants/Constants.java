/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.constants;

import leaf.cosmere.Cosmere;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class Constants
{
	public static class Resources
	{
		/**
		 * Generic ResourceLocation to allow for all entries in a command suggestion to be used for the command
		 */
		public static final ResourceLocation ALL = new ResourceLocation(Cosmere.MODID, "all");

		public static final ResourceLocation JEI = new ResourceLocation(Cosmere.MODID, "jei");
		public static final ResourceLocation SPIRITWEB_CAP = new ResourceLocation(Cosmere.MODID, "innate_investiture");
	}

	public static class Suffix
	{
		public static final MutableComponent INVESTITURE_UNITS = new TextComponent(" IU").withStyle(ChatFormatting.RESET);
		public static final MutableComponent STORMLIGHT_UNITS = new TextComponent(" SU").withStyle(ChatFormatting.RESET);
	}

	public static class NBT
	{

		public static final String CHARGE_LEVEL = "charge_level";

		public static final String ATTUNED_PLAYER = "attuned_player";
		public static final String ATTUNED_PLAYER_NAME = "attuned_player_name";


		public static final UUID UNKEYED_UUID = UUID.fromString("422fc419-1e39-4eac-ac01-6fc98512c122");
		public static final UUID ALUMINUM_UUID = UUID.fromString("8da98b83-be5e-4b34-b51e-5fdd79700893");
	}

	//todo rename this when I remember the word
	public static class RegNameStubs
	{
		public static final String BLOCK = "_block";
		public static final String RAW = "raw_";
		public static final String ORE = "_ore";
		public static final String DEEPSLATE_ORE = "_deepslate";
		public static final String BLEND = "_blend";

		public static final String METALMIND = "_metalmind";

		public static final String BRACELET = "_bracelet";
		public static final String RING = "_ring";
		public static final String NECKLACE = "_necklace";
		public static final String INGOT = "_ingot";
		public static final String NUGGET = "_nugget";
		public static final String SPIKE = "_spike";


		public static final String CHIP = "_chip";
		public static final String MARK = "_mark";
		public static final String BROAM = "_broam";
	}


	public static class Gui
	{
		public static final int NONE = 0;
		public static final int GUIDE = 1;
	}

	public static class Strings
	{
		public static final String KEYS_CATEGORY = "keys.cosmere.main";

		public static final String KEY_MANIFESTATION_MODE_INCREASE = "key.cosmere.powers.mode.increase";
		public static final String KEY_MANIFESTATION_MODE_DECREASE = "key.cosmere.powers.mode.decrease";

		public static final String KEY_MANIFESTATION_NEXT = "key.cosmere.powers.next";
		public static final String KEY_MANIFESTATION_PREVIOUS = "key.cosmere.powers.previous";

		public static final String KEY_DEACTIVATE_ALL_POWERS = "key.cosmere.powers.toggle";
		public static final String KEY_MANIFESTATION_MENU = "key.cosmere.powers.menu";

		public static final String KEY_ALLOMANCY_PUSH = "key.cosmere.allomancy.push";
		public static final String KEY_ALLOMANCY_PULL = "key.cosmere.allomancy.pull";

		public static final String GUI = "gui.cosmere.";
		public static final String TOOLTIP_ITEM_INFO = "tooltip.item.info.";
		public static final String CONTAINED_POWERS_FOUND = "tooltip.cosmere.power.found";

		public static final String POWER_INVALID = "argument.cosmere.power.invalid";
		public static final String POWER_SET_SUCCESS = "command.cosmere.power.set.success";
		public static final String POWER_CLEAR_SUCCESS = "command.cosmere.power.clear.success";
		public static final String POWER_SET_FAIL = "command.cosmere.power.set.fail";
		public static final String POWER_MODE_SET = "power.cosmere.mode.set";
		public static final String POWER_ACTIVE = "power.cosmere.toggle.active";
		public static final String POWER_INACTIVE = "power.cosmere.toggle.inactive";


		public static final String POWERS_FOUND = "command.cosmere.power.found";
		public static final String POWER_STRENGTH = "command.cosmere.power.strength";
		public static final String SET_EYE_HEIGHT_SUCCESS = "command.cosmere.eyeheight.set.success";


		public static final String CONTAINED_METALS = "tooltip.cosmere.metals.contained";


		public static final String PATCHOULI_NOT_INSTALLED = "tooltip.cosmere.patchouli.not_installed";
	}

	public static class Translations
	{
		public static final TranslatableComponent TOOLTIP_HOLD_SHIFT = new TranslatableComponent(Strings.TOOLTIP_ITEM_INFO + "shift");
		public static final TranslatableComponent TOOLTIP_CONTROL = new TranslatableComponent(Strings.TOOLTIP_ITEM_INFO + "control");
		public static final TranslatableComponent TOOLTIP_SHIFT_AND_CONTROL = new TranslatableComponent(Strings.TOOLTIP_ITEM_INFO + "shift_control");

		public static final TranslatableComponent GUI_NEXT = new TranslatableComponent(Strings.GUI + "next");
		public static final TranslatableComponent GUI_PREV = new TranslatableComponent(Strings.GUI + "previous");
		public static final TranslatableComponent GUI_SELECT = new TranslatableComponent(Strings.GUI + "select");
		public static final TranslatableComponent GUI_CONFIRM = new TranslatableComponent(Strings.GUI + "confirm");
		public static final TranslatableComponent GUI_SAVE = new TranslatableComponent(Strings.GUI + "save");
		public static final TranslatableComponent GUI_CANCEL = new TranslatableComponent(Strings.GUI + "cancel");

		public static final TranslatableComponent GUI_BACK = new TranslatableComponent(Strings.GUI + "button.back");


		public static final TranslatableComponent POWER_ACTIVE = new TranslatableComponent(Strings.POWER_ACTIVE);
		public static final TranslatableComponent POWER_INACTIVE = new TranslatableComponent(Strings.POWER_INACTIVE);
	}
}
