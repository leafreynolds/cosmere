/*
 * File updated ~ 29 - 2 - 2024 ~ Leaf
 */

package leaf.cosmere.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class CosmereServerConfig implements ICosmereConfig
{

	private final ForgeConfigSpec configSpec;

	public final ForgeConfigSpec.IntValue CHARGEABLE_MAX_VALUE;
	public final ForgeConfigSpec.BooleanValue SCULK_CAN_HEAR_KINETIC_INVESTITURE;
	public final ForgeConfigSpec.IntValue PLAYER_METALBORN_CHANCE;
	public final ForgeConfigSpec.IntValue FULLBORN_POWERS_CHANCE;
	public final ForgeConfigSpec.IntValue TWINBORN_POWERS_CHANCE_PLAYER;
	public final ForgeConfigSpec.IntValue TWINBORN_POWERS_CHANCE_MOB;
	public final ForgeConfigSpec.IntValue PLAYER_MISTING_TO_FERRING_DISTRIBUTION;
	public final ForgeConfigSpec.IntValue RAIDER_POWERS_CHANCE;
	public final ForgeConfigSpec.IntValue MOB_POWERS_CHANCE;
	public final ForgeConfigSpec.DoubleValue EMOTIONAL_POWERS_SINGLE_TARGET_RANGE_MULTIPLIER;
	public final ForgeConfigSpec.EnumValue<PowerGeneration> POWER_GENERATION;


	CosmereServerConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Cosmere Server Config. This config is synced between server and client.").push("cosmere");

		CHARGEABLE_MAX_VALUE = builder.comment("What is the max value for chargeables? This value is modified based on the object").defineInRange("chargeableMaxValue", 18000, 1000, 1000000);
		SCULK_CAN_HEAR_KINETIC_INVESTITURE = builder.comment("Can sculk and Warden hear people using powers if the user isn't copper clouded").define("sculkHearsKineticInvestiture", true);

		MOB_POWERS_CHANCE = builder.comment("1 in how many mobs should have powers?").defineInRange("mobPowerChance", 16, 1, 123456);
		RAIDER_POWERS_CHANCE = builder.comment("1 in how many Raiders should have powers?").defineInRange("raiderPowerChance", 64, 1, 123456);

		PLAYER_METALBORN_CHANCE = builder.comment("1 in how many players should be metalborn?").defineInRange("playerMetalbornChance", 1, 1, 123456);
		FULLBORN_POWERS_CHANCE = builder.comment("1 in how many should powered individuals should have full powers of one type").defineInRange("fullPowersChance", 16, 1, 123456);
		TWINBORN_POWERS_CHANCE_PLAYER = builder.comment("If not full born, 1 in how many powered players should be twinborn? If players are not twinborn, they will be either a misting or ferring.").defineInRange("twinbornPowersChancePlayer", 1, 1, 123456);
		TWINBORN_POWERS_CHANCE_MOB = builder.comment("If not full born, 1 in how many powered mobs should be twinborn?").defineInRange("twinbornPowersChanceMob", 16, 1, 123456);
		PLAYER_MISTING_TO_FERRING_DISTRIBUTION = builder.comment("Ratio defined as a percentage of how many metalborn players are mistings vs ferrings. E.g. 25 means 25%, or 1 in 4 players will be mistings.").defineInRange("playerMistingToFerringDistribution", 50, 0, 100);

		EMOTIONAL_POWERS_SINGLE_TARGET_RANGE_MULTIPLIER = builder.comment("Multiplier for emotional allomancy range when singe-targeting").defineInRange("emotionalAllomancySingleTargetRange", 1.5D, 1D, 123456D);

		POWER_GENERATION = builder.comment("Power generation method. RANDOM assigns random powers on spawn, PICK allows players to choose allomantic and feruchemical abilities, and NONE assigns nothing").defineEnum("powerGeneration", PowerGeneration.RANDOM);

		builder.pop();
		configSpec = builder.build();
	}

	@Override
	public String getFileName()
	{
		return "CosmereServer";
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
		CHARGEABLE_MAX_VALUE.clearCache();
		SCULK_CAN_HEAR_KINETIC_INVESTITURE.clearCache();
		MOB_POWERS_CHANCE.clearCache();
		RAIDER_POWERS_CHANCE.clearCache();
		FULLBORN_POWERS_CHANCE.clearCache();
		TWINBORN_POWERS_CHANCE_MOB.clearCache();
		TWINBORN_POWERS_CHANCE_PLAYER.clearCache();
		PLAYER_MISTING_TO_FERRING_DISTRIBUTION.clearCache();
		EMOTIONAL_POWERS_SINGLE_TARGET_RANGE_MULTIPLIER.clearCache();
	}

	public enum PowerGeneration
	{
		NONE,
		RANDOM,
		PICK
		// todo: ORIGINS later
	}
}