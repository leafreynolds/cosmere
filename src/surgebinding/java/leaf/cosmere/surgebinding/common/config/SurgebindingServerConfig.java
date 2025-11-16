/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.config;

import leaf.cosmere.common.config.ICosmereConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public class SurgebindingServerConfig implements ICosmereConfig
{
	private final ForgeConfigSpec configSpec;

	public final ForgeConfigSpec.IntValue MAX_SHARDBLADES;
	public final ForgeConfigSpec.IntValue PLAYER_MAX_STORMLIGHT;
	public final ForgeConfigSpec.IntValue PLAYER_DRAW_SPEED;
	public final ForgeConfigSpec.IntValue STORMLIGHT_DRAIN_RATE;
	public final ForgeConfigSpec.IntValue PROGRESSION_BONEMEAL_COST;
	public final ForgeConfigSpec.IntValue PROGRESSION_HEAL_COST;
	public final ForgeConfigSpec.IntValue PROGRESSION_AGE_UP_COST;
	public final ForgeConfigSpec.BooleanValue NIGHTBLOOD_SPOILERS;


	//public final ForgeConfigSpec.ConfigValue<List<? extends String>> FIRST_IDEALS;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> SECOND_IDEALS;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> THIRD_IDEALS;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> FOURTH_IDEALS;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> FIFTH_IDEALS;

	public static final String IDEAL_NOT_IMPLEMENTED = "notyetimplemented";

	SurgebindingServerConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Surgebinding Config. This config is synced between server and client.").push("surgebinding");

		MAX_SHARDBLADES = builder.comment("How many shardblades total that the user can bond").defineInRange("shardbladeBondAmount", 10, 0, 20);
		PLAYER_MAX_STORMLIGHT = builder.comment("How much stormlight can a player hold at once").defineInRange("playerMaxStormlight", 5000, 100, 20000);
		PLAYER_DRAW_SPEED = builder.comment("How fast a player draws in or breathes out stormlight using keybinds.").defineInRange("playerDrawSpeed", 150, 5, 20000);
		STORMLIGHT_DRAIN_RATE = builder.comment("How many points of stormlight drain per second").defineInRange("stormlightDrainRate", 5, 1, 100);
		PROGRESSION_BONEMEAL_COST = builder.comment("How many points of stormlight to trigger the bonemeal effect").defineInRange("progressionBonemealStormlightCost", 20, 1, 1000);
		PROGRESSION_HEAL_COST = builder.comment("How many points of stormlight per half a heart healed").defineInRange("progressionHealStormlightCost", 20, 1, 1000);
		PROGRESSION_AGE_UP_COST = builder.comment("How many points of stormlight per age up on a baby mob").defineInRange("progressionAgeUpCost", 50, 1, 1000);

		NIGHTBLOOD_SPOILERS = builder.comment("Enable this to allow Nightblood to have certain feature, which is spoilers for WindAndTruth").define("nightbloodSpoilers", true);

		final Predicate<Object> elementValidator = o -> o instanceof String;

		SECOND_IDEALS = builder.defineList(
				"second_ideals",
				List.of(
						"I will protect those who cannot protect themselves.", //windrunner
						"I will put the law before all else.", //skybreaker
						"I will seek self-mastery", //dustbringer
						"I will remember those who have been forgotten", //edgedancer
						"I will seek the truth", //truthwatcher //todo better?
						IDEAL_NOT_IMPLEMENTED, //lightweaver
						"I will reach my potential", //elsecaller
						"I will seek freedom for those in bondage.", //willshaper
						"I will stand when others fall.", //stoneward
						"I will unite instead of divide." //bondsmith
				),
				elementValidator);

		THIRD_IDEALS = builder.defineList(
				"third_ideals",
				List.of(
						"I will protect even those I hate, so long as it is right.", //windrunner
						IDEAL_NOT_IMPLEMENTED, //skybreaker
						"I will strive to create rather than destroy", //dustbringer
						"I will listen to those who have been ignored.", //edgedancer
						IDEAL_NOT_IMPLEMENTED, //truthwatcher
						IDEAL_NOT_IMPLEMENTED, //lightweaver
						"I will achieve my goals, no matter the cost", //elsecaller
						IDEAL_NOT_IMPLEMENTED, //willshaper
						"I will be the foundation on which others can build.", //stoneward
						"I will take responsibility for what I have done. If I must fall, I will rise each time a better" //bondsmith
				),
				elementValidator);

		FOURTH_IDEALS = builder.defineList(
				"fourth_ideals",
				List.of(
						"I accept that there will be those I cannot protect.", //windrunner
						IDEAL_NOT_IMPLEMENTED, //skybreaker
						"I will accept that destruction is sometimes necessary", //dustbringer
						IDEAL_NOT_IMPLEMENTED, //edgedancer
						IDEAL_NOT_IMPLEMENTED, //truthwatcher
						IDEAL_NOT_IMPLEMENTED, //lightweaver
						"I will accept that some goals are unobtainable", //elsecaller
						IDEAL_NOT_IMPLEMENTED, //willshaper
						IDEAL_NOT_IMPLEMENTED, //stoneward
						IDEAL_NOT_IMPLEMENTED //bondsmith
				),
				elementValidator);

		FIFTH_IDEALS = builder.defineList(
				"fifth_ideals",
				List.of(
						"I will protect myself, so that I may continue to protect others. ", //windrunner
						"I am the law", //skybreaker
						IDEAL_NOT_IMPLEMENTED, //dustbringer
						IDEAL_NOT_IMPLEMENTED, //edgedancer
						IDEAL_NOT_IMPLEMENTED, //truthwatcher
						IDEAL_NOT_IMPLEMENTED, //lightweaver
						IDEAL_NOT_IMPLEMENTED, //elsecaller
						IDEAL_NOT_IMPLEMENTED, //willshaper
						IDEAL_NOT_IMPLEMENTED, //stoneward
						IDEAL_NOT_IMPLEMENTED //bondsmith
				),
				elementValidator);


		builder.pop();
		configSpec = builder.build();
	}

	@Override
	public String getFileName()
	{
		return "SurgebindingServer";
	}

	@Override
	public ForgeConfigSpec getConfigSpec()
	{
		return configSpec;
	}

	@Override
	public Type getConfigType()
	{
		return Type.SERVER;
	}

	@Override
	public void clearCache()
	{
		MAX_SHARDBLADES.clearCache();
		PLAYER_MAX_STORMLIGHT.clearCache();
		STORMLIGHT_DRAIN_RATE.clearCache();
		PROGRESSION_BONEMEAL_COST.clearCache();
		PROGRESSION_HEAL_COST.clearCache();
		PROGRESSION_AGE_UP_COST.clearCache();
	}

	public String getIdeal(int ideal, int order)
	{
		String idealString = null;

		switch (ideal)
		{
			default:
			case 1:
				idealString = "life before death, strength before weakness, journey before destination";
				break;
			case 2:
				idealString = SECOND_IDEALS.get().get(order);
				break;
			case 3:
				idealString = THIRD_IDEALS.get().get(order);
				break;
			case 4:
				idealString = FOURTH_IDEALS.get().get(order);
				break;
			case 5:
				idealString = FIFTH_IDEALS.get().get(order);
				break;
		}

		return cleanIdeal(idealString);
	}

	//todo move this to a utility class
	public static String cleanIdeal(String idealString)
	{
		String cleanedString = idealString
				.replace(" ", "")
				.replace(",", "")
				.replace(".", "")
				.replace(";", "")
				.toLowerCase(Locale.ROOT);

		return cleanedString;
	}
}