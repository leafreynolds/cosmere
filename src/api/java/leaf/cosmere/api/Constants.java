/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 * File updated ~ 2 - 5 - 2025 ~ SoaringEaqle
 */

package leaf.cosmere.api;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class Constants
{
	public static class Resources
	{

		public static final ResourceLocation JEI = new ResourceLocation(CosmereAPI.COSMERE_MODID, "jei");
		public static final ResourceLocation JEI_FERUCHEMY = new ResourceLocation("feruchemy", "jei");
		public static final ResourceLocation JEI_HEMALURGY = new ResourceLocation("hemalurgy", "jei");
		public static final ResourceLocation SPIRITWEB_CAP = new ResourceLocation(CosmereAPI.COSMERE_MODID, "spiritweb");
		public static final ResourceLocation ROSHAR_CAP = new ResourceLocation("surgebinding", "roshar");
		public static final ResourceLocation SCADRIAL_CAP = new ResourceLocation("allomancy", "scadrial");
		public static final ResourceLocation HEMALURGY_WORLD_CAP = new ResourceLocation("hemalurgy", "world_cap");
	}

	public static class Suffix
	{
		public static final MutableComponent INVESTITURE_UNITS = Component.literal(" IU").withStyle(ChatFormatting.RESET);
		public static final MutableComponent STORMLIGHT_UNITS = Component.literal(" SU").withStyle(ChatFormatting.RESET);
	}

	public static class NBT
	{

		public static final String CHARGE_LEVEL = "charge_level";

		public static final String ATTUNED_PLAYER = "attuned_player";
		public static final String ATTUNED_PLAYER_NAME = "attuned_player_name";

		public static final UUID UNKEYED_UUID = UUID.fromString("422fc419-1e39-4eac-ac01-6fc98512c122");
		public static final UUID FERU_NICROSIL_UUID = UUID.fromString("5199248a-4a6e-4891-bc2a-1f3c0fcb5191");
		public static final UUID ALUMINUM_UUID = UUID.fromString("8da98b83-be5e-4b34-b51e-5fdd79700893");
	}

	//todo rename this when I remember the word
	public static class RegNameStubs
	{
		public static final String BLOCK = "_block";
		public static final String RAW = "raw_";
		public static final String ORE = "_ore";
		public static final String DEEPSLATE = "deepslate_";
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
		public static final String KEYS_ACTIVATE_CATEGORY = "keys.cosmere.activators";

		//keybind builder
		public static final String KEY_ALLOMANCY = "key.cosmere.allomancy.";
		public static final String KEY_FERUCHEMY = "key.cosmere.feruchemy.";
		public static final String KEY_STORMLIGHT = "key.cosmere.stormlight.";

		public static final String KEY_ACTIVATE = "key.cosmere.powers.activate";
		public static final String KEY_SAVE_ACTIVATOR = "key.cosmere.powers.save";

		public static final String KEY_MANIFESTATION_MODE_INCREASE = "key.cosmere.powers.mode.increase";
		public static final String KEY_MANIFESTATION_MODE_DECREASE = "key.cosmere.powers.mode.decrease";

		public static final String KEY_MANIFESTATION_NEXT = "key.cosmere.powers.next";
		public static final String KEY_MANIFESTATION_PREVIOUS = "key.cosmere.powers.previous";

		public static final String KEY_MANIFESTATION_USE_ACTIVE = "key.cosmere.powers.use";

		public static final String KEY_DEACTIVATE_ALL_POWERS = "key.cosmere.powers.toggle";
		public static final String KEY_MANIFESTATION_MENU = "key.cosmere.powers.menu";

		public static final String KEY_ALLOMANCY_STEEL_PUSH = "key.cosmere.allomancy.push";
		public static final String KEY_ALLOMANCY_IRON_PULL = "key.cosmere.allomancy.pull";
		public static final String KEY_ALLOMANCY_SOOTHE = "key.cosmere.allomancy.soothe";
		public static final String KEY_ALLOMANCY_RIOT = "key.cosmere.allomancy.riot";

		public static final String KEY_SHARDBLADE = "key.cosmere.stormlight.shardblade";
		public static final String KEY_REQUEST_STORMLIGHT = "key.cosmere.stormlight.request_stormlight";
		public static final String KEY_DISPATCH_STORMLIGHT = "key.cosmere.stormlight.dispatch_stormlight";
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
		public static final String EFFECTS_FOUND = "command.cosmere.effects.found";
		public static final String EFFECTS_CLEAR = "command.cosmere.effects.clear";


		public static final String CONTAINED_METALS = "tooltip.cosmere.metals.contained";


		public static final String PATCHOULI_NOT_INSTALLED = "tooltip.cosmere.patchouli.not_installed";
	}

	public static class Translations
	{
		public static final MutableComponent TOOLTIP_HOLD_SHIFT = Component.translatable(Strings.TOOLTIP_ITEM_INFO + "shift");
		public static final MutableComponent TOOLTIP_CONTROL = Component.translatable(Strings.TOOLTIP_ITEM_INFO + "control");
		public static final MutableComponent TOOLTIP_SHIFT_AND_CONTROL = Component.translatable(Strings.TOOLTIP_ITEM_INFO + "shift_control");


		public static final MutableComponent POWER_ACTIVE = Component.translatable(Strings.POWER_ACTIVE);
		public static final MutableComponent POWER_INACTIVE = Component.translatable(Strings.POWER_INACTIVE);
	}
}
