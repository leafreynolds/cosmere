/*
 * File updated ~ 7 - 6 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.config;

import leaf.cosmere.common.config.ICosmereConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class FeruchemyServerConfig implements ICosmereConfig
{

	private final ForgeConfigSpec configSpec;

	public final ForgeConfigSpec.IntValue BENDALLOY_SATURATION_MULTIPLIER;
	public final ForgeConfigSpec.IntValue GOLD_TAP_COST_MULTIPLIER;

	public final ForgeConfigSpec.IntValue GOD_METAL_EAT_STRENGTH_MINIMUM;
	public final ForgeConfigSpec.IntValue FULL_FERUCHEMIST_STARTING_METALMIND_COUNT;
	public final ForgeConfigSpec.DoubleValue STARTING_METALMIND_RANDOMISED_MAX_FILL_AMOUNT;

	FeruchemyServerConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Feruchemy Config. This config is synced between server and client.").push("feruchemy");

		BENDALLOY_SATURATION_MULTIPLIER = builder.comment("The cost multiplier for when player is full and needs to start doing saturation").defineInRange("bendalloySaturationMultiplier", 2, 1, Integer.MAX_VALUE);
		GOLD_TAP_COST_MULTIPLIER = builder.comment("Healing cost multiplier for tapping").defineInRange("goldTapCostMultiplier", 2, 1, Integer.MAX_VALUE);
		GOD_METAL_EAT_STRENGTH_MINIMUM = builder.comment("When the user eats a lerasatium nugget, what should their strength in feruchemy be set to").defineInRange("godMetalMinimumStrength", 16, 1, 32);
		FULL_FERUCHEMIST_STARTING_METALMIND_COUNT = builder.comment("How many metalminds should a full feruchemist start with?").defineInRange("fullFeruchemistStartingMetalmindCount", 3, 0, 16);

		STARTING_METALMIND_RANDOMISED_MAX_FILL_AMOUNT = builder
				.comment("What is the max amount a starting metalmind can be filled with attribute? The value is randomised, with this set as the max.")
				.defineInRange("fullFeruchemistStartingMetalmindFillAmount", 0.6667, 0, 1);

		builder.pop();
		configSpec = builder.build();
	}

	@Override
	public String getFileName()
	{
		return "FeruchemyServer";
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
		BENDALLOY_SATURATION_MULTIPLIER.clearCache();
		GOLD_TAP_COST_MULTIPLIER.clearCache();
		GOD_METAL_EAT_STRENGTH_MINIMUM.clearCache();
		FULL_FERUCHEMIST_STARTING_METALMIND_COUNT.clearCache();
		STARTING_METALMIND_RANDOMISED_MAX_FILL_AMOUNT.clearCache();
	}
}