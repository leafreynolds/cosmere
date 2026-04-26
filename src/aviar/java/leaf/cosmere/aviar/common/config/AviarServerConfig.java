/*
 * File updated ~ 20 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.common.config;

import leaf.cosmere.common.config.ICosmereConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ModConfig.Type;

public class AviarServerConfig implements ICosmereConfig
{

	private final ModConfigSpec configSpec;

	public final ModConfigSpec.DoubleValue AVIAR_BONUS_RANGE;


	AviarServerConfig()
	{
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
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
		AVIAR_BONUS_RANGE.clearCache();
	}
}