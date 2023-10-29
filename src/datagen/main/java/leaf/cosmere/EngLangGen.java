/*
 * File updated ~ 27 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.ResourceLocationHelper;
import leaf.cosmere.api.providers.IAttributeProvider;
import leaf.cosmere.api.providers.IEntityTypeProvider;
import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.itemgroups.CosmereItemGroups;
import leaf.cosmere.common.registry.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

import static leaf.cosmere.api.Constants.Strings.*;

public class EngLangGen extends LanguageProvider
{
	final String advancementTitleFormat = "advancements.cosmere.%s.title";
	final String advancementDescriptionFormat = "advancements.cosmere.%s.description";

	public EngLangGen(DataGenerator gen)
	{
		super(gen, Cosmere.MODID, "en_us");
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
		addItemGroups();
		addDamageSources();
		addMobEffects();
		addCurioIdentifiers();
		addConfigs();
		addCommands();
		addKeybindings();
		addStats();

	}


	private void addItemsAndBlocks()
	{
		//Items and Blocks
		for (IItemProvider item : ItemsRegistry.ITEMS.getAllItems())
		{
			final Item currentItem = item.asItem();
			final ResourceLocation registryName = ResourceLocationHelper.get(currentItem);
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
			final ResourceLocation registryName = ResourceLocationHelper.get(currentItem);
			String localisedString = StringHelper.fixCapitalisation(registryName.getPath());
			add(currentItem.getDescriptionId(), localisedString);
		}


		//work through each metal and generate localisation for related things.
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			//if a vanilla metal, like iron/gold
			if (!metalType.hasMaterialItem())
			{
				final String name = metalType.getName();
				//add(item.getDescriptionId(), localisedString);
				final String n = name + "_nugget";
				final String i = name + "_ingot";
				final String b = name + "_block";
				add("item.cosmere." + n, StringHelper.fixCapitalisation(n));
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

	private void addTooltips()
	{
		//Tooltips
		add("tooltip.item.info.shift", "\u00A77Hold \u00A78[\u00A7eShift\u00A78]");
		add("tooltip.item.info.shift_control", "\u00A77Hold \u00A78[\u00A7eShift\u00A78] \u00A77and \u00A78[\u00A7eControl\u00A78]");
		add("tooltip.item.info.control", "\u00A77Hold \u00A78[\u00A7eControl\u00A78]");

		//patchouli
		add(PATCHOULI_NOT_INSTALLED, "Patchouli is not installed. Documentation is not available.");
	}

	private void addItemGroups()
	{
		//ItemGroups/Tabs
		add("itemGroup." + CosmereItemGroups.ITEMS.getRecipeFolderName(), "Cosmere Items");
		add("itemGroup." + CosmereItemGroups.BLOCKS.getRecipeFolderName(), "Cosmere Blocks");
	}

	private void addDamageSources()
	{
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
	}

	private void addKeybindings()
	{
		//KeyBindings
		add(KEYS_CATEGORY, "Cosmere");
		add(KEY_MANIFESTATION_MENU, "Powers Menu");
		add(KEY_DEACTIVATE_ALL_POWERS, "Deactivate All Powers");
		add(KEY_MANIFESTATION_NEXT, "Next Power");
		add(KEY_MANIFESTATION_PREVIOUS, "Previous Power");
		add(KEY_MANIFESTATION_USE_ACTIVE, "Use Active Ability");
		add(KEY_MANIFESTATION_MODE_INCREASE, "Mode Increase");
		add(KEY_MANIFESTATION_MODE_DECREASE, "Mode Decrease");
	}

	private void addStats()
	{
	}
}
