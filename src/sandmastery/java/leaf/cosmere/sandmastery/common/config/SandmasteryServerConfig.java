/*
 * File updated ~ 18 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.config;

import leaf.cosmere.common.config.ICosmereConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ModConfig.Type;

public class SandmasteryServerConfig implements ICosmereConfig
{

	private final ModConfigSpec configSpec;

	public final ModConfigSpec.IntValue PROJECTILE_COOLDOWN;
	public final ModConfigSpec.DoubleValue HYDRATION_COST_MULTIPLIER;
	public final ModConfigSpec.IntValue CHARGE_COST_MULTIPLIER;
	public final ModConfigSpec.IntValue STARTING_HYDRATION;
	public final ModConfigSpec.IntValue MAX_HYDRATION;
	public final ModConfigSpec.IntValue OVERMASTERY_DURATION;
	public final ModConfigSpec.DoubleValue DEHYDRATION_THRESHOLD;
	public final ModConfigSpec.BooleanValue GIVE_QIDO_ON_FIRST_LOGIN;
	public final ModConfigSpec.IntValue QIDO_MAX_FILL;

	SandmasteryServerConfig()
	{
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
		builder.comment("Sandmastery Config. This config is synced between server and client.").push("sandmastery");

		PROJECTILE_COOLDOWN = builder.comment("How many ticks between projectiles at 1 ribbon allocated in ticks. (1 second = 20 ticks)").defineInRange("projectileCooldown", 100, 24, 1000);

		HYDRATION_COST_MULTIPLIER = builder.comment("Hydration cost is tied to sand cost, how many units of hydration are used per unit of sand charge").defineInRange("hydrationCostMultiploer", 1.5, 0.5, 100);

		CHARGE_COST_MULTIPLIER = builder.comment("Charge cost multiplier is multiplied by the base cost to determine charge cost per tick").defineInRange("chargeCostMultiplier", 2, 1, 1000);

		STARTING_HYDRATION = builder.comment("Starting hydration a player has when they spawn into the world for the first time").defineInRange("startingHydration", 10000, 1, 100000);
		MAX_HYDRATION = builder.comment("Max hydration a player can have").defineInRange("maxHydration", 10000, 1, 100000);

		OVERMASTERY_DURATION = builder.comment("How long overmastery lasts in minutes").defineInRange("overmasteryDuration", 120, 1, 720);

		DEHYDRATION_THRESHOLD = builder.comment("The threshold at which a player will start to become dehydrated (percentage of max)").defineInRange("dehydrationThreshold", 0.01, 0.0, 100.0);

		GIVE_QIDO_ON_FIRST_LOGIN = builder.comment("Should the player be given a qido on first login?").define("qidoOnFirstLogin", false);

		QIDO_MAX_FILL = builder.comment("How much hydration should a qido restore in a single fill?").defineInRange("qidoMaxFill", 10000, 1, 1000000);

		builder.pop();
		configSpec = builder.build();
	}

	@Override
	public String getFileName()
	{
		return "SandmasteryServer";
	}

	@Override
	public ModConfigSpec getConfigSpec()
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
		HYDRATION_COST_MULTIPLIER.clearCache();
		CHARGE_COST_MULTIPLIER.clearCache();
		STARTING_HYDRATION.clearCache();
		MAX_HYDRATION.clearCache();
		OVERMASTERY_DURATION.clearCache();
		DEHYDRATION_THRESHOLD.clearCache();
	}
}