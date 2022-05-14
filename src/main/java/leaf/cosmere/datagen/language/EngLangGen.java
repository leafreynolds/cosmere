/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.language;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.itemgroups.CosmereItemGroups;
import leaf.cosmere.items.MetalmindItem;
import leaf.cosmere.items.curio.HemalurgicSpikeItem;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.AttributesRegistry;
import leaf.cosmere.registry.EffectsRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import leaf.cosmere.utils.helpers.StringHelper;
import net.minecraft.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.nio.file.Path;
import java.util.Locale;

import static leaf.cosmere.constants.Constants.Strings.*;

public class EngLangGen extends LanguageProvider
{
	private final DataGenerator generator;

	public EngLangGen(DataGenerator gen)
	{
		super(gen, Cosmere.MODID, "en_us");
		this.generator = gen;
	}

	@Override
	protected void addTranslations()
	{

		//Items and Blocks
		for (Item item : ForgeRegistries.ITEMS.getValues())
		{
			if (item.getRegistryName().getNamespace().contentEquals(Cosmere.MODID))
			{
				String localisedString = StringHelper.fixCapitalisation(item.getRegistryName().getPath());

				//string overrides
				switch (localisedString)
				{
					case "Guide":
						localisedString = "Ars Arcanum";//after the book that is written by Khriss
						break;
				}

				if (item instanceof HemalurgicSpikeItem)
				{
					String use = ((HemalurgicSpikeItem) item).getMetalType().getShortHemalurgicUseString();
					add("item.cosmere." + item.getRegistryName().getPath() + ".tooltip", use);
				}
				else if (item instanceof MetalmindItem)
				{
					String use = ((MetalmindItem) item).getMetalType().getFeruchemyMetalmindUse();
					add("item.cosmere." + item.getRegistryName().getPath() + ".tooltip", use);
				}


				add(item.getDescriptionId(), localisedString);
			}
		}

		//Entities
		add("entity.minecraft.villager.cosmere.metal_trader", "Metal Trader");
		for (EntityType<?> type : ForgeRegistries.ENTITIES)
		{
			if (type.getRegistryName().getNamespace().equals(Cosmere.MODID))
			{
				add(type.getDescriptionId(), StringHelper.fixCapitalisation(type.getRegistryName().getPath()));
			}
		}


		//innate

		for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
		{
			//power type
			String key = manifestation.translation().getKey();
			String path = manifestation.getName();


			//description
			//can't auto generate the descriptions ya dingleberry
			String name = path;
			String description;

			String metalName = Metals.MetalType.valueOf(manifestation.getPowerID()).get().toString().toLowerCase(Locale.ROOT);
			switch (manifestation.getManifestationType())
			{
				case ALLOMANCY:
					name = "Allomantic " + metalName;
					description = "Users can burn " + metalName;
					break;
				case FERUCHEMY:
					name = "Feruchemical " + metalName;
					description = "Users can tap " + metalName;
					break;
				case RADIANT:
				case ELANTRIAN:
				case AWAKENER:
				default:
				case NONE:
					description = "No Special Powers";
					break;
			}

			//Name
			add(key, StringHelper.fixCapitalisation(name));
			add(manifestation.description().getKey(), description);
		}

		//Attributes
		for (RegistryObject<Attribute> registryObject : AttributesRegistry.ATTRIBUTES.getEntries())
		{
			//no duplicates pls
			final String descriptionId = registryObject.get().getDescriptionId();
			if (!descriptionId.startsWith("manifestation"))
			{
				String translation = descriptionId.split("\\.")[1];
				add(descriptionId, StringHelper.fixCapitalisation(translation));
			}
		}

		//work through each metal and generate localisation for related things.
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			final String name = metalType.getName();

			final String mistingName = metalType.getMistingName();
			final String ferringName = metalType.getFerringName();

			String a = name + " - " + mistingName;
			String f = name + " - " + ferringName;

			String aKey = metalType.getAllomancyRegistryName();
			String fKey = metalType.getFeruchemyRegistryName();
			String hKey = "hemalurgic_" + name;

			String allomancyGuide = "entry." + aKey;
			String feruchemyGuide = "entry." + fKey;
			String hemalurgyGuide = "entry." + hKey;

			add(allomancyGuide, StringHelper.fixCapitalisation(a));
			add(feruchemyGuide, StringHelper.fixCapitalisation(f));
			add(hemalurgyGuide, StringHelper.fixCapitalisation(name));

			if (!metalType.hasMaterialItem())
			{
				//if a vanilla metal, like iron/gold
				//add(item.getDescriptionId(), localisedString);
				final String n = name + "_nugget";
				final String i = name + "_ingot";
				final String b = name + "_block";
				add("item.cosmere." + n, StringHelper.fixCapitalisation(n));
				add("item.cosmere." + i, StringHelper.fixCapitalisation(i));
				add("item.cosmere." + b, StringHelper.fixCapitalisation(b));
			}


			if (metalType.hasAssociatedManifestation())
			{
				add("cosmere." + mistingName, StringHelper.fixCapitalisation(mistingName));
				add("cosmere." + ferringName, StringHelper.fixCapitalisation(ferringName));
			}

		}


		add("tooltip.cosmere.metals.contained", "Contained Metals:");

		//ItemGroups/Tabs
		add("itemGroup." + CosmereItemGroups.ITEMS.getRecipeFolderName(), "Cosmere Items");
		add("itemGroup." + CosmereItemGroups.METALMINDS.getRecipeFolderName(), "Cosmere Metalminds");
		add("itemGroup." + CosmereItemGroups.HEMALURGIC_SPIKES.getRecipeFolderName(), "Cosmere Spikes");
		add("itemGroup." + CosmereItemGroups.BLOCKS.getRecipeFolderName(), "Cosmere Blocks");

		//Damage Sources

		//Containers

		//effects
		for (RegistryObject<MobEffect> effect : EffectsRegistry.EFFECTS.getEntries())
		{
			add(effect.get().getDescriptionId(), StringHelper.fixCapitalisation(effect.get().getRegistryName().getPath()));
		}

		//Sound Schemes

		//Configs

		//Commands


		//Tooltips
		add("tooltip.item.info.shift", "\u00A77Hold \u00A78[\u00A7eShift\u00A78]");
		add("tooltip.item.info.shift_control", "\u00A77Hold \u00A78[\u00A7eShift\u00A78] \u00A77and \u00A78[\u00A7eControl\u00A78]");
		add("tooltip.item.info.control", "\u00A77Hold \u00A78[\u00A7eControl\u00A78]");

		//GUI elements
		add("gui.cosmere.previous", "> Previous");
		add("gui.cosmere.next", "> Next");
		add("gui.cosmere.select", "> Select");
		add("gui.cosmere.confirm", "> Confirm");
		add("gui.cosmere.save", "> Save");
		add("gui.cosmere.cancel", "> Cancel");
		add("gui.cosmere.button.back", "Back");


		add("gui.cosmere.other.inactive", "Inactive");
		add("gui.cosmere.other.active", "Active");
		add("gui.cosmere.mode.increase", "Increase");
		add("gui.cosmere.mode.decrease", "Decrease");


		//ARS Arcanum
		add("cosmere.landing", "The Cosmere is filled with many fantastical things. I have left my findings written within this book.");

		//KeyBindings
		add(KEYS_CATEGORY, "Cosmere");
		add(KEY_MANIFESTATION_MENU, "Powers Menu");
		add(KEY_MANIFESTATION_TOGGLE, "Toggle Current Powers");
		add(KEY_MANIFESTATION_NEXT, "Next Power");
		add(KEY_MANIFESTATION_PREVIOUS, "Previous Power");
		add(KEY_MANIFESTATION_MODE_INCREASE, "Mode Increase");
		add(KEY_MANIFESTATION_MODE_DECREASE, "Mode Decrease");

		add(KEY_ALLOMANCY_PUSH, "Push");
		add(KEY_ALLOMANCY_PULL, "Pull");


		//powers
		add(POWER_INVALID, "Invalid power");
		add(POWER_SET_SUCCESS, "Successfully set power to: %s");
		add(POWER_SET_FAIL, "Failed to update power");
		add(POWER_MODE_SET, "Mode set to: %s");
		add(POWER_ACTIVE, "Power now active: %s");
		add(POWER_INACTIVE, "Power now inactive: %s");

		add(POWERS_FOUND, "Powers found for: %s \n");
		add(CONTAINED_POWERS_FOUND, "Hemalurgic Charge:");


	}

	public Path getSoundPath(Path path, String modid)
	{
		return path.resolve("data/" + modid + "/sounds/" + "sounds.json");
	}

	public String getTranslationKey(SoundEvent sound)
	{
		String subtitleTranslationKey = "";
		if (subtitleTranslationKey.isEmpty() || subtitleTranslationKey == null)
		{
			subtitleTranslationKey = Util.makeDescriptionId("subtitle", sound.getRegistryName());
		}
		return subtitleTranslationKey;
	}


}
