/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.sandmastery.api.trial.SandmasteryTrialAPI;
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
		add("entity.sandmastery.diem_target", "Diem Target");
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

		// the Mastrell who tests newcomers. Speaks in chat until the NPC exists.
		add("diem.sandmastery.mastrell.name", "Mastrell");
		add("diem.sandmastery.mastrell.says", "<%s> %s");
		add("diem.sandmastery.intro.test", "None may enter the Diem untested. Whether the sand will answer you is something only a sand master can find out. I have poured charged sand into the stone.");
		add("diem.sandmastery.intro.reach", "Take the sand in your hands. Feel the grains, and reach out to them.");
		add("diem.sandmastery.intro.welcome", "The sand answered you, and spent its charge doing it. Welcome to the Diem, %s. You command %s ribbon(s).");
		add("diem.sandmastery.intro.trials", "You rise through the Diem by passing trials at the Diem stone. When charged sand waits in the stone, a trial is ready for you.");
		add("diem.sandmastery.promoted", "You have been raised to %s.");
		add("diem.sandmastery.status", "You are %s, controlling %s ribbon(s).");
		add("diem.sandmastery.trial_ready", "You may attempt the trial for %s.");
		add("diem.sandmastery.trial_locked", "Reach %s ribbons to attempt the trial for %s.");
		add("diem.sandmastery.highest_rank", "You hold the highest rank of the Diem.");
		add("diem.sandmastery.ribbon_gained", "Your practice pays off: you can now control %s ribbons.");
		add("diem.sandmastery.practice_capped", "Practice alone will take you no further. Seek the trial for %s at the Diem.");

		addDiemTrials();
	}

	private void addDiemTrials()
	{
		add("gui.sandmastery.diem_trials.title", "Diem Trials");
		add("gui.sandmastery.diem_trials.heading", "Trials for %s");
		add("gui.sandmastery.diem_trials.start", "Start");
		add("gui.sandmastery.diem_trials.scout", "Scout");
		add("gui.sandmastery.diem_trials.scout.tooltip", "Show where this trial's checkpoints are for a minute, without starting it");
		add("gui.sandmastery.diem_trials.need_ribbons", "You control %s ribbon(s). These trials need %s.");
		add("gui.sandmastery.diem_trials.cooldown", "You failed a trial recently. Try again in about %s minute(s).");
		add("gui.sandmastery.diem_trials.ready", "You control %s ribbon(s). Choose your trial.");
		add("gui.sandmastery.diem_trials.none", "The Diem has no trials for this rank yet.");

		add("diem.sandmastery.trial.started", "%s begins. You have %s seconds.");
		add("diem.sandmastery.trial.passed", "You passed %s in %s seconds.");
		add("diem.sandmastery.trial.failed", "You failed %s: %s");
		add("diem.sandmastery.trial.cooldown", "You must wait before attempting another trial.");
		add("diem.sandmastery.trial.stopped", "Your trial was stopped.");
		add("diem.sandmastery.trial.scouting", "The course is shown for %s seconds.");
		add("diem.sandmastery.trial.refused.ribbons", "You need to control %s ribbons to attempt this trial.");
		add("diem.sandmastery.trial.refused.cooldown", "You failed a trial recently and must wait before trying again.");
		add("diem.sandmastery.trial.refused.already_in_trial", "You are already in a trial.");
		add("diem.sandmastery.trial.refused.overmastered", "You cannot attempt a trial while overmastered.");
		add("diem.sandmastery.trial.unavailable.no_course", "No course has been built near this stone.");

		add("diem.sandmastery.trial.fail.time", "time ran out");
		add("diem.sandmastery.trial.fail.decoy", "you hit a decoy");
		add("diem.sandmastery.trial.fail.out_of_shots", "you ran out of shots");
		add("diem.sandmastery.trial.fail.touched_ground", "you touched the ground");
		add("diem.sandmastery.trial.fail.off_tile", "your sand fell outside the tiles");
		add("diem.sandmastery.trial.fail.sand_budget", "you used too much sand");
		add("diem.sandmastery.trial.fail.hydration_budget", "you used too much water");
		add("diem.sandmastery.trial.fail.left_area", "you left the trial");
		add("diem.sandmastery.trial.fail.overmastered", "you overmastered");
		add("diem.sandmastery.trial.fail.died", "you died");

		add("diem.sandmastery.trial.outside_help.game_mode", "trials must be taken in survival");
		add("diem.sandmastery.trial.outside_help.flight", "flight that isn't Sandmastery");
		add("diem.sandmastery.trial.outside_help.gliding", "gliding");
		add("diem.sandmastery.trial.outside_help.riding", "riding");
		add("diem.sandmastery.trial.outside_help.other_powers", "using powers other than Sandmastery");
		add("diem.sandmastery.trial.outside_help.effects", "help from potion effects");
		add("diem.sandmastery.trial.outside_help.equipment", "wearing equipment other than sand pouches, jars and a qido");
		add("diem.sandmastery.trial.outside_help.teleport", "teleporting");
		add("diem.sandmastery.trial.outside_help.placed_block", "placing blocks");

		add("diem.sandmastery.trial.progress.targets", "%s/%s targets, %s shots left");
		add("diem.sandmastery.trial.progress.checkpoints", "%s/%s checkpoints");
		add("diem.sandmastery.trial.progress.sand", "%s sand left");
		add("diem.sandmastery.trial.progress.hydration", "%s water left");

		add("diem.sandmastery.trial.commendation.few_ribbons", "\"You needed no more ribbons than the rank asks. That is control.\"");
		add("diem.sandmastery.trial.commendation.clean", "\"Not a grain wasted. Every shot found its mark.\"");
		add("diem.sandmastery.trial.commendation.swift", "\"Swift. You finished with half your time to spare.\"");

		trial("accuracy_underfen", "Accuracy", "Strike the targets around the stone with your sand.");
		trial("crossing_underfen", "Crossing", "Reach the beacon without touching the ground. Place your sand only on the marked tiles.");
		trial("accuracy_fen", "Accuracy", "Strike the targets around the stone with your sand.");
		trial("crossing_tiles_fen", "Crossing on Tiles", "Reach the beacon without touching the ground. Place your sand only on the marked tiles.");
		trial("crossing_fen", "Crossing", "Reach the beacon without touching the ground.");
		trial("course_fen", "Course", "Reach every checkpoint of the Diem's course, in any order.");
		trial("accuracy_diemfen", "Accuracy", "Strike the smaller targets around the stone.");
		trial("crossing_diemfen", "Crossing", "Reach the high beacon without touching the ground.");
		trial("rings_diemfen", "Rings", "Pass through each ring in order.");
		trial("course_diemfen", "Course", "Reach every checkpoint of the Diem's course, in any order.");
		trial("accuracy_underlestrell", "Moving Targets", "Strike the targets as they circle the stone.");
		trial("crossing_tiles_underlestrell", "Crossing on Small Tiles", "Reach the beacon without touching the ground. The tiles are a single block wide.");
		trial("rings_underlestrell", "Rings", "Pass through each ring in order, climbing as you go.");
		trial("course_underlestrell", "Course", "Reach every checkpoint of the Diem's course, in any order.");
		trial("accuracy_lestrell", "Decoys", "Strike the targets. Hitting a decoy fails the trial.");
		trial("rings_lestrell", "Wide Rings", "Pass through each small ring in order. They are spread far apart.");
		trial("rings_targets_lestrell", "Rings and Targets", "Pass through the rings in order and strike the targets.");
		trial("course_lestrell", "Course", "Reach every checkpoint of the Diem's course, in any order.");
		trial("crossing_decoys_undermastrell", "Crossing under Fire", "Reach the beacon on the tiles while striking targets. Avoid the decoys.");
		trial("rings_undermastrell", "High Rings", "Pass through each small ring in order, high above the Diem.");
		trial("crossing_undermastrell", "Long Crossing", "Reach the distant, high beacon without touching the ground.");
		trial("course_undermastrell", "Course", "Reach every checkpoint of the Diem's course, in any order.");
		trial("gauntlet_mastrell", "The Gauntlet", "Pass through the rings in order while striking moving targets. Avoid the decoys.");
		trial("course_mastrell", "Course", "Reach every checkpoint of the Diem's course, in any order.");
	}

	private void trial(String path, String name, String description)
	{
		ResourceLocation id = Sandmastery.rl(path);
		add(SandmasteryTrialAPI.nameKey(id), name);
		add(SandmasteryTrialAPI.descriptionKey(id), description);
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
