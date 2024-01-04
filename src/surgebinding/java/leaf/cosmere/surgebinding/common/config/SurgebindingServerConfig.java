/*
 * File updated ~ 7 - 6 - 2023 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.config;

import leaf.cosmere.common.config.ICosmereConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class SurgebindingServerConfig implements ICosmereConfig
{

	private final ForgeConfigSpec configSpec;

	public final ForgeConfigSpec.IntValue MAX_SHARDBLADES;
	public final ForgeConfigSpec.IntValue PLAYER_MAX_STORMLIGHT;
	public final ForgeConfigSpec.IntValue STORMLIGHT_DRAIN_RATE;
	public final ForgeConfigSpec.IntValue PROGRESSION_BONEMEAL_COST;
	public final ForgeConfigSpec.IntValue PROGRESSION_HEAL_COST;
	public final ForgeConfigSpec.IntValue PROGRESSION_AGE_UP_COST;


	SurgebindingServerConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Surgebinding Config. This config is synced between server and client.").push("surgebinding");

		MAX_SHARDBLADES = builder.comment("How many shardblades total that the user can bond").defineInRange("shardbladeBondAmount", 10, 0, 20);
		PLAYER_MAX_STORMLIGHT = builder.comment("How much stormlight can a player hold at once").defineInRange("playerMaxStormlight", 5000, 100, 20000);
		STORMLIGHT_DRAIN_RATE = builder.comment("How many points of stormlight drain per second").defineInRange("stormlightDrainRate", 5, 1, 100);
		PROGRESSION_BONEMEAL_COST = builder.comment("How many points of stormlight to trigger the bonemeal effect").defineInRange("progressionBonemealStormlightCost", 20, 1, 1000);
		PROGRESSION_HEAL_COST = builder.comment("How many points of stormlight per half a heart healed").defineInRange("progressionHealStormlightCost", 20, 1, 1000);
		PROGRESSION_AGE_UP_COST = builder.comment("How many points of stormlight per age up on a baby mob").defineInRange("progressionAgeUpCost", 50, 1, 1000);

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
}