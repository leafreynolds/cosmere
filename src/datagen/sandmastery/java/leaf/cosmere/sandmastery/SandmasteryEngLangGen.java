/*
 * File updated ~ 12 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery;

import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.manifestation.SandmasteryManifestation;
import leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes;
import leaf.cosmere.sandmastery.common.registries.SandmasteryManifestations;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.ResourceLocationHelper;
import leaf.cosmere.api.providers.IAttributeProvider;
import leaf.cosmere.api.providers.IEntityTypeProvider;
import leaf.cosmere.api.providers.IMobEffectProvider;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

import static leaf.cosmere.api.Constants.Strings.KEY_SANDMASTERY_USE;

public class SandmasteryEngLangGen extends LanguageProvider
{
	final String advancementTitleFormat = "advancements.sandmastery.%s.title";
	final String advancementDescriptionFormat = "advancements.sandmastery.%s.description";

	public SandmasteryEngLangGen(DataGenerator gen)
	{
		super(gen, Sandmastery.MODID, "en_us");
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
			if (registryName.getNamespace().contentEquals(Sandmastery.MODID))
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
		for (ManifestationRegistryObject<SandmasteryManifestation> manifestationRegistryObject : SandmasteryManifestations.SANDMASTERY_POWERS.values())
		{
			SandmasteryManifestation manifestation = manifestationRegistryObject.getManifestation();

			//power type
			String key = manifestation.getTranslationKey();

			//description
			String name;
			String description;

			String abilityName = Taldain.Mastery.valueOf(manifestation.getPowerID()).get().toString().toLowerCase(Locale.ROOT);

			name = "Sand Mastery " + abilityName;
			description = "Masters can use " + abilityName;

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
		for (IAttributeProvider registryObject : SandmasteryAttributes.ATTRIBUTES.getAllAttributes())
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
	}

	private void addTooltips()
	{
	}

	private void addItemGroups()
	{
		//ItemGroups/Tabs
	}

	private void addDamageSources()
	{
		//Damage Sources
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
		//KeyBindings
		add(KEY_SANDMASTERY_USE, "Use Mastery Ability");
	}

	private void addStats()
	{
		//stats
	}
}
