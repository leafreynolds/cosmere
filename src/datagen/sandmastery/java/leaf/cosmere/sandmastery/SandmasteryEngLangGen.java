/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.diem.DiemRank;
import leaf.cosmere.sandmastery.common.manifestation.SandmasteryManifestation;
import leaf.cosmere.sandmastery.common.registries.SandmasteryManifestations;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Locale;

import static leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes.RIBBONS;
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
		addGuiText();
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
		for (Item item : BuiltInRegistries.ITEM)
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
	}

	private void addAttributes()
	{
		//Attribute
		final String descriptionId = RIBBONS.getAttribute().getDescriptionId();
		final String name = "Sand Mastery " + RIBBONS.getName();
		// Attributes are the powers themselves, they need their own names.
		add(descriptionId, StringHelper.fixCapitalisation(name));
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

	private void addGuiText()
	{
		add("gui.sandmastery.sand_pouch.taking_black", "Black");
		add("gui.sandmastery.sand_pouch.taking_white", "White");
		add("gui.sandmastery.ribbons_free", "Ribbons free: %s");

		add(DiemRank.ACOLENT.getTranslationKey(), "Acolent");
		add(DiemRank.UNDERFEN.getTranslationKey(), "Underfen");
		add(DiemRank.FEN.getTranslationKey(), "Fen");
		add(DiemRank.DIEMFEN.getTranslationKey(), "Diemfen");
		add(DiemRank.UNDERLESTRELL.getTranslationKey(), "Underlestrell");
		add(DiemRank.LESTRELL.getTranslationKey(), "Lestrell");
		add(DiemRank.UNDERMASTRELL.getTranslationKey(), "Undermastrell");
		add(DiemRank.MASTRELL.getTranslationKey(), "Mastrell");
		add(DiemRank.LORD_MASTRELL.getTranslationKey(), "Lord Mastrell");

		add("diem.sandmastery.detection", "You reach toward the sand on the stone, and it stirs. The Diem has found talent in you.");
		add("diem.sandmastery.detection_result", "You are now an %s, able to control %s ribbon(s).");
		add("diem.sandmastery.promoted", "You have been raised to %s.");
		add("diem.sandmastery.status", "You are %s, controlling %s ribbon(s).");
		add("diem.sandmastery.trial_ready", "You may attempt the trial for %s.");
		add("diem.sandmastery.trial_locked", "Reach %s ribbons to attempt the trial for %s.");
		add("diem.sandmastery.highest_rank", "You hold the highest rank of the Diem.");
		add("diem.sandmastery.ribbon_gained", "Your practice pays off: you can now control %s ribbons.");
		add("diem.sandmastery.practice_capped", "Practice alone will take you no further. Seek the trial for %s at the Diem.");
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
