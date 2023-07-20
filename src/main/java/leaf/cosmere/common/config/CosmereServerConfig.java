/*
 * File updated ~ 7 - 6 - 2023 ~ Leaf
 */

package leaf.cosmere.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class CosmereServerConfig implements ICosmereConfig
{

	private final ForgeConfigSpec configSpec;

	public final ForgeConfigSpec.IntValue CHARGEABLE_MAX_VALUE;
	public final ForgeConfigSpec.BooleanValue SCULK_CAN_HEAR_KINETIC_INVESTITURE;


	CosmereServerConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Cosmere Server Config. This config is synced between server and client.").push("cosmere");

		CHARGEABLE_MAX_VALUE = builder.comment("What is the max value for chargeables? This value is modified based on the object").defineInRange("chargeableMaxValue", 18000, 1000, 1000000);
		SCULK_CAN_HEAR_KINETIC_INVESTITURE = builder.comment("Can sculk and Warden hear people using powers if the user isn't copper clouded").define("sculkHearsKineticInvestiture", true);

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