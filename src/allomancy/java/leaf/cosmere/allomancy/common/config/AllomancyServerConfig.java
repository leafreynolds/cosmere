/*
 * File updated ~ 2026-04-25 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.allomancy.common.config;

import leaf.cosmere.common.config.ICosmereConfig;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.neoforge.common.ModConfigSpec;

public class AllomancyServerConfig implements ICosmereConfig
{

	private final ModConfigSpec configSpec;

	public final ModConfigSpec.DoubleValue IRON_STEEL_RANGE;
	public final ModConfigSpec.DoubleValue PUSH_PULL_WEIGHT;
	public final ModConfigSpec.DoubleValue MAX_PUSH_PULL_WEIGHT;
	public final ModConfigSpec.IntValue MAX_INGESTIBLE_METAL;

	// Boost amount for Duralumin and Nicrosil
	//public final ModConfigSpec.DoubleValue boostAmount;


	AllomancyServerConfig()
	{
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
		builder.comment("Allomancy Config. This config is synced between server and client.").push("allomancy");

		//can't modify registry objects like that
		//boostAmount = builder.comment("Boost amount for Duralumin and Nicrosil").defineInRange("boostAmount", 0.334D, 0D, 1D);

		// max 10 might seem low, but the scanning thread chugs so hard at 10 that it's unusable and I doubt anyone will try it except for fun >> Gerbagel
		IRON_STEEL_RANGE = builder.comment("Multiplier for iron and steel range. At base strength (9) the range is 9 while burning and 18 while flaring").defineInRange("ironSteelRange", 1.0D, 0D, 10.0D);

		PUSH_PULL_WEIGHT = builder.comment("Push/pull weight adjustment per block.").defineInRange("pushPullWeight", 0.05D, 0.01D, 100.0D);
		MAX_PUSH_PULL_WEIGHT = builder.comment("Maximum pull/push weight adjustment. At 1.0 (default), push/pull power maxes out at 2 times (20 blocks)").defineInRange("maxPushPullWeight", 1.0D, 0D, 999999.0D);

		MAX_INGESTIBLE_METAL = builder.comment("Maximum amount of metal possible to ingest in seconds").defineInRange("maxIngestibleMetal", 7200, 1, 999999);

		builder.pop();
		configSpec = builder.build();
	}

	@Override
	public String getFileName()
	{
		return "AllomancyServer";
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
	public void clearCache() {  }
}
