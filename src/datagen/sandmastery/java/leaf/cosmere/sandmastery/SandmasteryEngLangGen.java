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
//		add("tooltip.cosmere.metals.contained", "Contained Metals:");
	}

	private void addItemGroups()
	{
		//ItemGroups/Tabs
	}

	private void addDamageSources()
	{
		//Damage Sources
//		add("death.attack.eat_metal", "%1$s shredded their throat while eating metal");
//		add("death.attack.eat_metal.player", "%1$s tried to eat metal directly while fighting %2$s");
	}

	private void addMobEffects()
	{
//		for (IMobEffectProvider effect : AllomancyEffects.EFFECTS.getAllMobEffects())
//		{
//			add(effect.getMobEffect().getDescriptionId(), StringHelper.fixCapitalisation(effect.getRegistryName().getPath()));
//		}
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
//		add(KEY_ALLOMANCY_PUSH, "Push");
//		add(KEY_ALLOMANCY_PULL, "Pull");
	}

	private void addStats()
	{
		//stats
//		Arrays.stream(Metals.MetalType.values())
//				.filter(Metals.MetalType::hasAssociatedManifestation).forEach(metalType ->
//						add(
//								"stat.minecraft.time_since_started_burning_" + metalType.getName(),
//								"Time since started burning " + metalType.getName()
//						));
	}
}
