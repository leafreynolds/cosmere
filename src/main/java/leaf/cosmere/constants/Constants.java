/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.constants;

import leaf.cosmere.Cosmere;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

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
        public static final IFormattableTextComponent INVESTITURE_UNITS = new StringTextComponent(" IU").withStyle(TextFormatting.RESET);
        public static final IFormattableTextComponent STORMLIGHT_UNITS = new StringTextComponent(" SU").withStyle(TextFormatting.RESET);
    }

    public static class NBT
    {

        public static final String CHARGE_LEVEL = "charge_level";

        public static final String ATTUNED_PLAYER = "attuned_player";
        public static final String ATTUNED_PLAYER_NAME = "attuned_player_name";


        public static final UUID UNSEALED_UUID = UUID.fromString("422fc419-1e39-4eac-ac01-6fc98512c122");
        public static final UUID ALUMINUM_UUID = UUID.fromString("8da98b83-be5e-4b34-b51e-5fdd79700893");
    }

    //todo rename this when I remember the word
    public static class RegNameStubs
    {
        public static final String BLOCK = "_block";
        public static final String RAW = "raw_";
        public static final String ORE = "_ore";
        public static final String BLEND = "_blend";

        public static final String METALMIND = "_metalmind";

        public static final String BRACELET = "_bracelet";
        public static final String RING = "_ring";
        public static final String NECKLACE = "_necklace";
        public static final String INGOT = "_ingot";
        public static final String NUGGET = "_nugget";
        public static final String SHAVINGS = "_shavings";
        public static final String SPIKE = "_spike";
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

        public static final String KEY_MANIFESTATION_TOGGLE = "key.cosmere.powers.toggle";
        public static final String KEY_MANIFESTATION_MENU = "key.cosmere.powers.menu";

        public static final String KEY_ALLOMANCY_PUSH = "key.cosmere.allomancy.push";
        public static final String KEY_ALLOMANCY_PULL = "key.cosmere.allomancy.pull";

        public static final String GUI = "gui.cosmere.";
        public static final String TOOLTIP_ITEM_INFO = "tooltip.item.info.";
        public static final String CONTAINED_POWERS_FOUND = "tooltip.cosmere.power.found";

        public static final String POWER_INVALID = "argument.cosmere.power.invalid";
        public static final String POWER_SET_SUCCESS = "command.cosmere.power.set.success";
        public static final String POWER_SET_FAIL = "command.cosmere.power.set.fail";
        public static final String POWER_MODE_SET = "power.cosmere.mode.set";
        public static final String POWER_ACTIVE = "power.cosmere.toggle.active";
        public static final String POWER_INACTIVE = "power.cosmere.toggle.inactive";


        public static final String POWERS_FOUND = "command.cosmere.power.found";


        public static final String CONTAINED_METALS = "tooltip.cosmere.metals.contained";


        public static final String PATCHOULI_NOT_INSTALLED = "tooltip.cosmere.patchouli.not_installed";
    }

    public static class Translations
    {
        public static final TranslationTextComponent TOOLTIP_HOLD_SHIFT = new TranslationTextComponent(Strings.TOOLTIP_ITEM_INFO + "shift");
        public static final TranslationTextComponent TOOLTIP_CONTROL = new TranslationTextComponent(Strings.TOOLTIP_ITEM_INFO + "control");
        public static final TranslationTextComponent TOOLTIP_SHIFT_AND_CONTROL = new TranslationTextComponent(Strings.TOOLTIP_ITEM_INFO + "shift_control");

        public static final TranslationTextComponent GUI_NEXT = new TranslationTextComponent(Strings.GUI + "next");
        public static final TranslationTextComponent GUI_PREV = new TranslationTextComponent(Strings.GUI + "previous");
        public static final TranslationTextComponent GUI_SELECT = new TranslationTextComponent(Strings.GUI + "select");
        public static final TranslationTextComponent GUI_CONFIRM = new TranslationTextComponent(Strings.GUI + "confirm");
        public static final TranslationTextComponent GUI_SAVE = new TranslationTextComponent(Strings.GUI + "save");
        public static final TranslationTextComponent GUI_CANCEL = new TranslationTextComponent(Strings.GUI + "cancel");

        public static final TranslationTextComponent GUI_BACK = new TranslationTextComponent(Strings.GUI + "button.back");


        public static final TranslationTextComponent POWER_ACTIVE = new TranslationTextComponent(Strings.POWER_ACTIVE);
        public static final TranslationTextComponent POWER_INACTIVE = new TranslationTextComponent(Strings.POWER_INACTIVE);
    }
}
