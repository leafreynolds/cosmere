/*
 * File updated ~ 18 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.config;

import leaf.cosmere.common.config.ICosmereConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class SandmasteryServerConfig implements ICosmereConfig
{

	private final ForgeConfigSpec configSpec;

	public final ForgeConfigSpec.IntValue PROJECTILE_COOLDOWN;
	public final ForgeConfigSpec.IntValue PROJECTILE_HYDRATION_COST;
	public final ForgeConfigSpec.IntValue PLATFORM_HYDRATION_COST;
	public final ForgeConfigSpec.IntValue LAUNCH_HYDRATION_COST;
	public final ForgeConfigSpec.IntValue ELEVATE_HYDRATION_COST;
	public final ForgeConfigSpec.IntValue CUSHION_HYDRATION_COST;
	public final ForgeConfigSpec.IntValue CHARGE_COST_MULTIPLIER;
	public final ForgeConfigSpec.IntValue STARTING_HYDRATION;
	public final ForgeConfigSpec.IntValue MAX_HYDRATION;
	public final ForgeConfigSpec.IntValue OVERMASTERY_DURATION;
	public final ForgeConfigSpec.DoubleValue DEHYDRATION_THRESHOLD;


	SandmasteryServerConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Sandmastery Config. This config is synced between server and client.").push("sandmastery");

		PROJECTILE_COOLDOWN = builder.comment("How many ticks between each projectile").defineInRange("projectileCooldown", 30, 1, 1000);

		PROJECTILE_HYDRATION_COST = builder.comment("How much hydration is used per projectile").defineInRange("projectileHydrationCost", 30, 1, 1000);
		PLATFORM_HYDRATION_COST = builder.comment("How much hydration is used per tick for platform").defineInRange("platformHydrationCost", 60, 1, 1000);
		LAUNCH_HYDRATION_COST = builder.comment("How much hydration is used per tick for launch").defineInRange("launchHydrationCost", 10, 1, 1000);
		ELEVATE_HYDRATION_COST = builder.comment("How much hydration is used per tick for elevate").defineInRange("elevateHydrationCost", 10, 1, 1000);
		CUSHION_HYDRATION_COST = builder.comment("How much hydration is used per tick for cushion").defineInRange("cushionHydrationCost", 10, 1, 1000);

		CHARGE_COST_MULTIPLIER = builder.comment("Charge cost multiplier is multiplied by the base cost to determine charge cost per tick").defineInRange("chargeCostMultiplier", 2, 1, 1000);

		STARTING_HYDRATION = builder.comment("Starting hydration a player has when they spawn into the world for the first time").defineInRange("startingHydration", 10000, 1, 100000);
		MAX_HYDRATION = builder.comment("Max hydration a player can have").defineInRange("maxHydration", 10000, 1, 100000);

		OVERMASTERY_DURATION = builder.comment("How long overmastery lasts in minutes").defineInRange("overmasteryDuration", 120, 1, 720);

		DEHYDRATION_THRESHOLD = builder.comment("The threshold at which a player will start to become dehydrated (percentage of max)").defineInRange("dehydrationThreshold", 0.01, 0.1, 100.0);

		builder.pop();
		configSpec = builder.build();
	}

	@Override
	public String getFileName()
	{
		return "SandmasteryServer";
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
		PROJECTILE_COOLDOWN.clearCache();
		PROJECTILE_HYDRATION_COST.clearCache();
		PLATFORM_HYDRATION_COST.clearCache();
		LAUNCH_HYDRATION_COST.clearCache();
		ELEVATE_HYDRATION_COST.clearCache();
		CUSHION_HYDRATION_COST.clearCache();
		CHARGE_COST_MULTIPLIER.clearCache();
		STARTING_HYDRATION.clearCache();
		MAX_HYDRATION.clearCache();
		OVERMASTERY_DURATION.clearCache();
		DEHYDRATION_THRESHOLD.clearCache();
	}
}