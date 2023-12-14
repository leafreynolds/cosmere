/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.example.common.config;

import leaf.cosmere.common.config.ICosmereConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class ExampleServerConfig implements ICosmereConfig
{

	private final ForgeConfigSpec configSpec;

	public final ForgeConfigSpec.IntValue EXAMPLE;


	ExampleServerConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Example Config. This config is synced between server and client.").push("Example");

		EXAMPLE = builder.comment("Example.").defineInRange("example", 0, 0, 100);


		builder.pop();
		configSpec = builder.build();
	}

	@Override
	public String getFileName()
	{
		return "ExampleServer";
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
		EXAMPLE.clearCache();
	}
}