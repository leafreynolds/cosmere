/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere;

import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.api.providers.IAttributeProvider;
import leaf.cosmere.api.providers.IEntityTypeProvider;
import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registry.*;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

import static leaf.cosmere.api.Constants.Strings.*;

public class EngLangGen extends LanguageProvider
{
	final String advancementTitleFormat = "advancements.cosmere.%s.title";
	final String advancementDescriptionFormat = "advancements.cosmere.%s.description";

	public EngLangGen(PackOutput output)
	{
		super(output, Cosmere.MODID, "en_us");
	}

	@Override
	protected void addTranslations()
	{
		addItemsAndBlocks();
		addEntities();
		addAdvancements();
		addManifestations();
		addAttributes();
		addPatchouli();
		addTooltips();
		addDamageSources();
		addMobEffects();
		addCurioIdentifiers();
		addConfigs();
		addCommands();
		addKeybindings();
		addStats();
		addCreativeTabs();
	}


	private void addItemsAndBlocks()
	{
		//Items and Blocks
		for (IItemProvider item : ItemsRegistry.ITEMS.getAllItems())
		{
			final Item currentItem = item.asItem();
			final ResourceLocation registryName = RegistryHelper.get(currentItem);
			if (registryName.getNamespace().contentEquals(Cosmere.MODID))
			{
				String localisedString = StringHelper.fixCapitalisation(registryName.getPath());

				//string overrides
				//todo will we be overriding more of these?
				if (localisedString.equals("Guide"))
				{
					localisedString = "Ars Arcanum";//after the book that is written by Khriss
				}

				add(currentItem.getDescriptionId(), localisedString);
			}
		}

		for (IItemProvider item : BlocksRegistry.BLOCKS.getAllBlocks())
		{
			final Item currentItem = item.asItem();
			final ResourceLocation registryName = RegistryHelper.get(currentItem);
			String localisedString = StringHelper.fixCapitalisation(registryName.getPath());
			add(currentItem.getDescriptionId(), localisedString);
		}


		//work through each metal and generate localisation for related things.
		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			//if a vanilla metal, like iron/gold/copper
			if (!metalType.hasMaterialItem() || metalType == Metals.MetalType.COPPER)
			{
				final String name = metalType.getName();
				//add(item.getDescriptionId(), localisedString);
				if (metalType != Metals.MetalType.COPPER)       // copper specifically has no vanilla nugget, and so we have already made this key
				{
					final String n = name + "_nugget";
					add("item.cosmere." + n, StringHelper.fixCapitalisation(n));
				}
				final String i = name + "_ingot";
				final String b = name + "_block";
				add("item.cosmere." + i, StringHelper.fixCapitalisation(i));
				add("item.cosmere." + b, StringHelper.fixCapitalisation(b));
			}
		}
	}

	private void addEntities()
	{
		//Entities
		add("entity.minecraft.villager.cosmere.metal_trader", "Metal Trader");
		for (IEntityTypeProvider type : EntityTypeRegistry.ENTITY_TYPES.getAllEntityTypes())
		{
			final ResourceLocation id = type.getRegistryName();
			add(type.getEntityType().getDescriptionId(), StringHelper.fixCapitalisation(id.getPath()));
		}
	}

	private void addAdvancements()
	{
	}

	private void addManifestations()
	{
		add(ManifestationRegistry.NONE.getTranslationKey(), "None");
	}

	private void addAttributes()
	{
		//Attributes
		for (IAttributeProvider registryObject : AttributesRegistry.ATTRIBUTES.getAllAttributes())
		{
			final String descriptionId = registryObject.getAttribute().getDescriptionId();
			//no duplicates pls
			//manifestation section handles adding attributes lang gen for themselves
			if (!descriptionId.startsWith("manifestation"))
			{
				String translation = descriptionId.split("\\.")[1];
				add(descriptionId, StringHelper.fixCapitalisation(translation));
			}
		}
	}

	private void addPatchouli()
	{
		//ARS Arcanum
		add("cosmere.landing", "The Cosmere is filled with many fantastical things. I have left my findings written within this book.");
	}

	private void addCreativeTabs()
	{
		//ItemGroups/Tabs
		//CreativeTabsRegistry.ITEMS.get().getDisplayName()
		add("tabs.cosmere.items", "Cosmere");
	}

	private void addTooltips()
	{
		//Tooltips
		add("tooltip.item.info.shift", "\u00A77Hold \u00A78[\u00A7eShift\u00A78]");
		add("tooltip.item.info.shift_control", "\u00A77Hold \u00A78[\u00A7eShift\u00A78] \u00A77and \u00A78[\u00A7eControl\u00A78]");
		add("tooltip.item.info.control", "\u00A77Hold \u00A78[\u00A7eControl\u00A78]");

		//patchouli
		add(PATCHOULI_NOT_INSTALLED, "Patchouli is not installed. Documentation is not available.");
	}

	private void addDamageSources()
	{
		//Damage Sources
		add("death.attack.eat_metal", "%1$s shredded their throat while eating metal");
		add("death.attack.eat_metal.player", "%1$s tried to eat metal directly while fighting %2$s");
	}

	private void addMobEffects()
	{
	}

	private void addCurioIdentifiers()
	{
	}

	private void addConfigs()
	{
	}

	private void addCommands()
	{
		//Commands
		add(POWER_INVALID, "Invalid power");
		add(POWER_CLEAR_SUCCESS, "Cleared powers on player: %s");
		add(POWER_SET_SUCCESS, "Successfully set power to: %s");
		add(POWER_SET_FAIL, "Failed to update power");
		add(POWER_MODE_SET, "Mode set to: %s");
		add(POWER_ACTIVE, "Power now active: %s");
		add(POWER_INACTIVE, "Power now inactive: %s");
		add(POWER_STRENGTH, "Base Strength: %s \nTotal Strength: %s");

		add(POWERS_FOUND, "Powers found for: %s \n");
		add(EFFECTS_FOUND, "Effects found for: %s \n");
		add(EFFECTS_CLEAR, "Effects cleared for: %s \n");
	}

	private void addKeybindings()
	{
		//KeyBindings
		add(KEYS_CATEGORY, "Cosmere");
		add(KEYS_ACTIVATE_CATEGORY, "Power Activators");
		add(KEY_MANIFESTATION_MENU, "Powers Menu");
		add(KEY_DEACTIVATE_ALL_POWERS, "Deactivate All Powers");
		add(KEY_MANIFESTATION_NEXT, "Next Power");
		add(KEY_MANIFESTATION_PREVIOUS, "Previous Power");
		add(KEY_MANIFESTATION_USE_ACTIVE, "Use Active Ability");
		add(KEY_MANIFESTATION_MODE_INCREASE, "Mode Increase");
		add(KEY_MANIFESTATION_MODE_DECREASE, "Mode Decrease");
		add(KEY_ACTIVATE, "Activate Power Save State");
		add(KEY_SAVE_ACTIVATOR, "Save New Power State");
		String allo = "Activate Allomantic ";
		String feru = "Activate Feruchemic ";
		String surge = "Activate Surgebinding ";
		for(Metals.MetalType metalType: EnumUtils.METAL_TYPES)
		{
			if(!metalType.hasFeruchemicalEffect())
			{
				continue;
			}
			add(KEY_ALLOMANCY + metalType.getName(), allo + StringHelper.fixCapitalisation(metalType.getName()));
			add(KEY_FERUCHEMY + metalType.getName(), feru + StringHelper.fixCapitalisation(metalType.getName()));
		}
		for(Roshar.Surges i : EnumUtils.SURGES)
		{
			add(KEY_STORMLIGHT + i.getName(), surge + StringHelper.fixCapitalisation(i.getName()));
		}
	}

	private void addStats()
	{
	}
}
