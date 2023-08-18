/*
 * File updated ~ 18 - 8 - 2023 ~ Leaf
 */

package leaf.cosmere.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class CosmereServerConfig implements ICosmereConfig
{

	private final ForgeConfigSpec configSpec;

	public final ForgeConfigSpec.IntValue CHARGEABLE_MAX_VALUE;
	public final ForgeConfigSpec.BooleanValue SCULK_CAN_HEAR_KINETIC_INVESTITURE;
	public final ForgeConfigSpec.IntValue FULLBORN_POWERS_CHANCE;
	public final ForgeConfigSpec.IntValue TWINBORN_POWERS_CHANCE;
	public final ForgeConfigSpec.IntValue RAIDER_POWERS_CHANCE;
	public final ForgeConfigSpec.IntValue MOB_POWERS_CHANCE;


	CosmereServerConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Cosmere Server Config. This config is synced between server and client.").push("cosmere");

		CHARGEABLE_MAX_VALUE = builder.comment("What is the max value for chargeables? This value is modified based on the object").defineInRange("chargeableMaxValue", 18000, 1000, 1000000);
		SCULK_CAN_HEAR_KINETIC_INVESTITURE = builder.comment("Can sculk and Warden hear people using powers if the user isn't copper clouded").define("sculkHearsKineticInvestiture", true);

		MOB_POWERS_CHANCE = builder.comment("1 in how many mobs should have powers?").defineInRange("mobPowersChance", 16, 1, 123456);
		RAIDER_POWERS_CHANCE = builder.comment("1 in how many Raiders should have powers?").defineInRange("mobPowersChance", 64, 1, 123456);

		FULLBORN_POWERS_CHANCE = builder.comment("1 in how many should powered individuals should have full powers of one type").defineInRange("fullPowersChance", 16, 1, 123456);
		TWINBORN_POWERS_CHANCE = builder.comment("If not full born, 1 in how many powered mobs should be twinborn? Players are twinborn as a minimum.").defineInRange("twinbornPowersChance", 16, 1, 123456);

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
	}
}