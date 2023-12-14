/*
 * File updated ~ 20 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.common.config;

import leaf.cosmere.common.config.ICosmereConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class AviarServerConfig implements ICosmereConfig
{

	private final ForgeConfigSpec configSpec;

	public final ForgeConfigSpec.DoubleValue AVIAR_BONUS_RANGE;


	AviarServerConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Aviar Config. This config is synced between server and client.").push("aviar");

		AVIAR_BONUS_RANGE = builder.comment("How far away the player the tamed aviar can be and still get a bonus").defineInRange("aviar_bonus_range", 15d, 1d, 20d);


		builder.pop();
		configSpec = builder.build();
	}

	@Override
	public String getFileName()
	{
		return "AviarServer";
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
		AVIAR_BONUS_RANGE.clearCache();
	}
}