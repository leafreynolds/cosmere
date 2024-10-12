/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.providers.IAttributeProvider;
import leaf.cosmere.api.providers.IEntityTypeProvider;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.registries.SurgebindingAttributes;
import leaf.cosmere.surgebinding.common.registries.SurgebindingEntityTypes;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

import static leaf.cosmere.api.Constants.Strings.KEY_SHARDBLADE;

public class SurgebindingEngLangGen extends LanguageProvider
{
	final String advancementTitleFormat = "advancements.surgebinding.%s.title";
	final String advancementDescriptionFormat = "advancements.surgebinding.%s.description";

	public SurgebindingEngLangGen(PackOutput output)
	{
		super(output, Surgebinding.MODID, "en_us");
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
		addCreativeTabs();
		addTooltips();
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
			final ResourceLocation registryName = RegistryHelper.get(item);
			if (registryName.getNamespace().contentEquals(Surgebinding.MODID))
			{
				String localisedString = StringHelper.fixCapitalisation(registryName.getPath());
				add(item.getDescriptionId(), localisedString);
			}
		}

	}

	private void addEntities()
	{
		for (IEntityTypeProvider type : SurgebindingEntityTypes.ENTITY_TYPES.getAllEntityTypes())
		{
			final ResourceLocation id = type.getRegistryName();
			add(type.getEntityType().getDescriptionId(), StringHelper.fixCapitalisation(id.getPath()));
		}
	}

	private void addAdvancements()
	{
		Manifestations.ManifestationTypes value = Manifestations.ManifestationTypes.SURGEBINDING;
		{
			add(String.format(advancementTitleFormat, value.getName()), StringHelper.fixCapitalisation(value.getName()));
			add(String.format(advancementDescriptionFormat, value.getName()), "Test description: " + StringHelper.fixCapitalisation(value.getName()));
		}
	}

	private void addManifestations()
	{
		for (ManifestationRegistryObject<Manifestation> manifestationRegistryObject : SurgebindingManifestations.SURGEBINDING_POWERS.values())
		{
			Manifestation manifestation = manifestationRegistryObject.getManifestation();

			//power type
			String key = manifestation.getTranslationKey();
			String name = manifestation.getName();

			//description
			String description = "Needs description";

			String tabName = manifestation.getManifestationType().getName();

			add(String.format(advancementTitleFormat, tabName + "." + name), StringHelper.fixCapitalisation(name));
			add(String.format(advancementDescriptionFormat, tabName + "." + name), "Test description: " + StringHelper.fixCapitalisation(name));

			//Name
			add(key, StringHelper.fixCapitalisation(name));

			//todo decide about manifestation descriptions?
			final ResourceLocation regName = manifestation.getRegistryName();
			add("manifestation." + regName.getNamespace() + "." + regName.getPath() + ".description", description);
		}

	}

	private void addAttributes()
	{
		//Attributes
		for (IAttributeProvider registryObject : SurgebindingAttributes.ATTRIBUTES.getAllAttributes())
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
		//todo surgebinding patchouli localisation
	}


	private void addCreativeTabs()
	{
		//ItemGroups/Tabs
		add("tabs.surgebinding.items", "Surgebinding");

	}

	private void addTooltips()
	{

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

	}

	private void addKeybindings()
	{
		add(KEY_SHARDBLADE, "Summon/Dismiss Shardblade");
	}

	private void addStats()
	{

	}
}
