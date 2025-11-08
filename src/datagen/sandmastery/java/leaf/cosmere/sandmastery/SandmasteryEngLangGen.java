/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.api.providers.IAttributeProvider;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.manifestation.SandmasteryManifestation;
import leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes;
import leaf.cosmere.sandmastery.common.registries.SandmasteryManifestations;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

import static leaf.cosmere.sandmastery.common.utils.SandmasteryConstants.*;


public class SandmasteryEngLangGen extends LanguageProvider
{
	final String advancementTitleFormat = "advancements.sandmastery.%s.title";
	final String advancementDescriptionFormat = "advancements.sandmastery.%s.description";

	public SandmasteryEngLangGen(PackOutput output)
	{
		super(output, Sandmastery.MODID, "en_us");
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
			final ResourceLocation registryName = RegistryHelper.get(item);
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
		Manifestations.ManifestationTypes value = Manifestations.ManifestationTypes.SANDMASTERY;
		{
			add(String.format(advancementTitleFormat, value.getName()), StringHelper.fixCapitalisation(value.getName()));
			add(String.format(advancementDescriptionFormat, value.getName()), "Test description: " + StringHelper.fixCapitalisation(value.getName()));
		}
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

		// Temp translation fix - Remove when ribbons are added
		add("manifestation.sandmastery.ribbons", StringHelper.fixCapitalisation("Sand Mastery Ribbons"));
		add("manifestation.sandmastery.ribbons.description", "Masters can use ribbons");
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
		add("tabs.sandmastery.items", "Sandmastery");
	}

	private void addDamageSources()
	{
		//Damage Sources
		add("death.attack.dehydrated", "%1$s died of dehydration");
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
		add(KEY_SANDMASTERY_LAUNCH, "Use Launch Mastery");
		add(KEY_SANDMASTERY_ELEVATE, "Use Elevate Mastery");
		add(KEY_SANDMASTERY_PROJECTILE, "Use Projectile Mastery");
		add(KEY_SANDMASTERY_PLATFORM, "Use Platform Mastery");
	}

	private void addStats()
	{
		//stats
	}
}
