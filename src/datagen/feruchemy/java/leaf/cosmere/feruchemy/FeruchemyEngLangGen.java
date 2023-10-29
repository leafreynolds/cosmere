/*
 * File updated ~ 27 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.ResourceLocationHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.providers.IAttributeProvider;
import leaf.cosmere.api.providers.ICosmereEffectProvider;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.common.items.ChargeableMetalCurioItem;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.itemgroups.FeruchemyItemGroups;
import leaf.cosmere.feruchemy.common.registries.FeruchemyAttributes;
import leaf.cosmere.feruchemy.common.registries.FeruchemyEffects;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;
import java.util.Map;

public class FeruchemyEngLangGen extends LanguageProvider
{
	final String advancementTitleFormat = "advancements.feruchemy.%s.title";
	final String advancementDescriptionFormat = "advancements.feruchemy.%s.description";

	public FeruchemyEngLangGen(DataGenerator gen)
	{
		super(gen, Feruchemy.MODID, "en_us");
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
			if (registryName.getNamespace().contentEquals(Feruchemy.MODID))
			{
				String localisedString = StringHelper.fixCapitalisation(registryName.getPath());

				if (item instanceof ChargeableMetalCurioItem)
				{
					String use = ((ChargeableMetalCurioItem) item).getMetalType().getFeruchemyMetalmindUse();
					add("item.feruchemy." + registryName.getPath() + ".tooltip", use);
				}


				add(item.getDescriptionId(), localisedString);
			}
		}
	}

	private void addEntities()
	{
		//Entities
		for (Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>> type : ForgeRegistries.ENTITY_TYPES.getEntries())
		{
			final ResourceLocation id = type.getKey().location();
			if (id.getNamespace().equals(Feruchemy.MODID))
			{
				add(type.getValue().getDescriptionId(), StringHelper.fixCapitalisation(id.getPath()));
			}
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
		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			if (manifestation.getManifestationType() != Manifestations.ManifestationTypes.FERUCHEMY)
			{
				continue;
			}

			//power type
			String key = manifestation.getTranslationKey();

			//description
			//can't auto generate the descriptions ya dingleberry
			String name;
			String description;


			String tabName = manifestation.getManifestationType().getName();
			String metalName = Metals.MetalType.valueOf(manifestation.getPowerID()).get().toString().toLowerCase(Locale.ROOT);

			name = "Feruchemical " + metalName;
			description = "Users can tap " + metalName;
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
				final String ferringName = metalType.getFerringName();
				add("feruchemy." + ferringName, StringHelper.fixCapitalisation(ferringName));
			}
		}
	}

	private void addAttributes()
	{
		//Attributes
		for (IAttributeProvider registryObject : FeruchemyAttributes.ATTRIBUTES.getAllAttributes())
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
			final String ferringName = metalType.getFerringName();

			String f = name + " - " + ferringName;
			String fKey = metalType.getName();
			String feruchemyGuide = "entry." + fKey;

			add(feruchemyGuide, StringHelper.fixCapitalisation(f));
		}
	}

	private void addTooltips()
	{
		//Tooltips
		add("tooltip.cosmere.attribute.iron", "%s%s Attack Damage");
		add("tooltip.cosmere.attribute.tin", "%s%s%% Better Night Vision");
		add("tooltip.cosmere.attribute.copper", "%s%s%% XP");
		add("tooltip.cosmere.attribute.zinc", "%s%s%% Emotional Fortitude");
		add("tooltip.cosmere.attribute.duralumin", "");//todo hemalurgic connection/identity
		add("tooltip.cosmere.attribute.chromium", "%s%s Luck");
		add("tooltip.cosmere.attribute.nicrosil", "");//todo hemalurgic investiture
	}

	private void addItemGroups()
	{
		//ItemGroups/Tabs
		add("itemGroup." + FeruchemyItemGroups.METALMINDS.getRecipeFolderName(), "Feruchemical Metalminds");
	}

	private void addDamageSources()
	{
	}

	private void addMobEffects()
	{
		for (ICosmereEffectProvider effect : FeruchemyEffects.EFFECTS.getEffectsInRegistry())
		{
			add(effect.getEffect().getTranslationKey(), StringHelper.fixCapitalisation(effect.getRegistryName().getPath()));
		}
	}

	private void addCurioIdentifiers()
	{
	}

	private void addConfigs()
	{
	}

	private void addCommands()
	{
	}

	private void addKeybindings()
	{
	}

	private void addStats()
	{
	}
}
