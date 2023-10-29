/*
 * File updated ~ 27 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.manifestation.AllomancyManifestation;
import leaf.cosmere.allomancy.common.registries.AllomancyAttributes;
import leaf.cosmere.allomancy.common.registries.AllomancyEffects;
import leaf.cosmere.allomancy.common.registries.AllomancyEntityTypes;
import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.ResourceLocationHelper;
import leaf.cosmere.api.providers.IAttributeProvider;
import leaf.cosmere.api.providers.ICosmereEffectProvider;
import leaf.cosmere.api.providers.IEntityTypeProvider;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Locale;

import static leaf.cosmere.api.Constants.Strings.KEY_ALLOMANCY_PULL;
import static leaf.cosmere.api.Constants.Strings.KEY_ALLOMANCY_PUSH;

public class AllomancyEngLangGen extends LanguageProvider
{
	final String advancementTitleFormat = "advancements.allomancy.%s.title";
	final String advancementDescriptionFormat = "advancements.allomancy.%s.description";

	public AllomancyEngLangGen(DataGenerator gen)
	{
		super(gen, Allomancy.MODID, "en_us");
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
		for (Item item : ForgeRegistries.ITEMS.getValues())
		{
			final ResourceLocation registryName = ResourceLocationHelper.get(item);
			if (registryName.getNamespace().contentEquals(Allomancy.MODID))
			{
				String localisedString = StringHelper.fixCapitalisation(registryName.getPath());

				add(item.getDescriptionId(), localisedString);
			}
		}
	}

	private void addEntities()
	{
		//Entities
		for (IEntityTypeProvider type : AllomancyEntityTypes.ENTITY_TYPES.getAllEntityTypes())
		{
			final ResourceLocation id = type.getRegistryName();
			add(type.getEntityType().getDescriptionId(), StringHelper.fixCapitalisation(id.getPath()));
		}
	}

	private void addAdvancements()
	{
		//innate
		for (Manifestations.ManifestationTypes value : Manifestations.ManifestationTypes.values())
		{
			add(String.format(advancementTitleFormat, value.getName()), StringHelper.fixCapitalisation(value.getName()));
			add(String.format(advancementDescriptionFormat, value.getName()), "Test description: " + StringHelper.fixCapitalisation(value.getName()));
		}
	}

	private void addManifestations()
	{
		for (ManifestationRegistryObject<AllomancyManifestation> manifestationRegistryObject : AllomancyManifestations.ALLOMANCY_POWERS.values())
		{
			AllomancyManifestation manifestation = manifestationRegistryObject.getManifestation();

			//power type
			String key = manifestation.getTranslationKey();

			//description
			String name;
			String description;


			String tabName = manifestation.getManifestationType().getName();
			String metalName = Metals.MetalType.valueOf(manifestation.getPowerID()).get().toString().toLowerCase(Locale.ROOT);

			name = "Allomantic " + metalName;
			description = "Users can burn " + metalName;
			add(String.format(advancementTitleFormat, tabName + "." + metalName), StringHelper.fixCapitalisation(name));
			add(String.format(advancementDescriptionFormat, tabName + "." + metalName), "Test description: " + StringHelper.fixCapitalisation(name));

			//Name
			add(key, StringHelper.fixCapitalisation(name));

			//todo decide about manifestation descriptions?
			final ResourceLocation regName = manifestation.getRegistryName();
			add("manifestation." + regName.getNamespace() + "." + regName.getPath() + ".description", description);
		}


		//work through each metal and generate localisation for related things.
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (metalType.hasAssociatedManifestation())
			{
				final String mistingName = metalType.getMistingName();
				add("allomancy." + mistingName, StringHelper.fixCapitalisation(mistingName));
			}
		}
	}

	private void addAttributes()
	{
		//Attributes
		for (IAttributeProvider registryObject : AllomancyAttributes.ATTRIBUTES.getAllAttributes())
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
		//work through each metal and generate localisation for related things.
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			final String name = metalType.getName();
			final String mistingName = metalType.getMistingName();
			String a = name + " - " + mistingName;
			String aKey = metalType.getName();
			String allomancyGuide = "entry." + aKey;
			add(allomancyGuide, StringHelper.fixCapitalisation(a));
		}
	}

	private void addTooltips()
	{
		add("tooltip.cosmere.metals.contained", "Contained Metals:");
	}

	private void addItemGroups()
	{
		//ItemGroups/Tabs
	}

	private void addDamageSources()
	{
		//Damage Sources
		add("death.attack.eat_metal", "%1$s shredded their throat while eating metal");
		add("death.attack.eat_metal.player", "%1$s tried to eat metal directly while fighting %2$s");
	}

	private void addMobEffects()
	{
		//
		for (ICosmereEffectProvider effect : AllomancyEffects.EFFECTS.getEffectsInRegistry())
		{
			add(effect.getEffect().getTranslationKey(), StringHelper.fixCapitalisation(effect.getRegistryName().getPath()));
		}
	}

	private void addCurioIdentifiers()
	{

	}

	private void addConfigs()
	{
		add("config.jade.plugin_allomancy.bronze_seeker_tooltip", "Seeker Tooltip");
	}

	private void addCommands()
	{
	}

	private void addKeybindings()
	{
		//KeyBindings
		add(KEY_ALLOMANCY_PUSH, "Push");
		add(KEY_ALLOMANCY_PULL, "Pull");
	}

	private void addStats()
	{
		//stats
		Arrays.stream(Metals.MetalType.values())
				.filter(Metals.MetalType::hasAssociatedManifestation).forEach(metalType ->
						add(
								"stat.minecraft.time_since_started_burning_" + metalType.getName(),
								"Time since started burning " + metalType.getName()
						));
	}
}
