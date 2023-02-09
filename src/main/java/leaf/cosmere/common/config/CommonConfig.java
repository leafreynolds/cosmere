/*
 * File updated ~ 7 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class CommonConfig implements ICosmereConfig
{

	private final ForgeConfigSpec configSpec;
	public final ForgeConfigSpec.IntValue commonConfigTest;


	CommonConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Cosmere Common Config. This config is not synced between server and client.").push("common");

		commonConfigTest = builder.comment("commonConfigTest.").translation("config.cosmere.commonconfigtest").defineInRange("commonconfigtest", 5, 0, Integer.MAX_VALUE);

		builder.pop();
		configSpec = builder.build();
	}

	@Override
	public String getFileName()
	{
		return "common";
	}

	@Override
	public ForgeConfigSpec getConfigSpec()
	{
		return configSpec;
	}

	@Override
	public Type getConfigType()
	{
		return Type.COMMON;
	}
}