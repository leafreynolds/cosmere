/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding;

import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.providers.IAttributeProvider;
import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.registries.SurgebindingAttributes;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

import static leaf.cosmere.api.Constants.Strings.KEY_SHARDBLADE;

public class SurgebindingEngLangGen extends LanguageProvider
{
	final String advancementTitleFormat = "advancements.surgebinding.%s.title";
	final String advancementDescriptionFormat = "advancements.surgebinding.%s.description";

	public SurgebindingEngLangGen(DataGenerator gen)
	{
		super(gen, Surgebinding.MODID, "en_us");
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
		for (IItemProvider itemRegistryObject : SurgebindingItems.ITEMS.getAllItems())
		{
			final Item item = itemRegistryObject.asItem();
			final ResourceLocation registryName = itemRegistryObject.getRegistryName();
			if (registryName.getNamespace().contentEquals(Surgebinding.MODID))
			{
				String localisedString = StringHelper.fixCapitalisation(registryName.getPath());
				add(item.getDescriptionId(), localisedString);
			}
		}

	}

	private void addEntities()
	{
	}

	private void addAdvancements()
	{
	}

	private void addManifestations()
	{
		for (ManifestationRegistryObject<Manifestation> manifestationRegistryObject : SurgebindingManifestations.SURGEBINDING_POWERS.values())
		{
			Manifestation manifestation = manifestationRegistryObject.getManifestation();

			//power type
			String key = manifestation.getTranslationKey();
			String path = manifestation.getName();


			//description
			//can't auto generate the descriptions ya dingleberry
			String name = path;
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

	private void addTooltips()
	{

	}

	private void addItemGroups()
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
