/*
 * File updated ~ 28 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class CosmereClientConfig implements ICosmereConfig
{
	private final ForgeConfigSpec configSpec;
	public final ForgeConfigSpec.BooleanValue clientConfigTest;


	CosmereClientConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Client Config. This config only exists on the client").push("cosmere");

		clientConfigTest = builder.comment("clientConfigTest").translation("config.cosmere.clientconfigtest").define("clientconfigtest", true);

		builder.pop();
		configSpec = builder.build();
	}

	@Override
	public String getFileName()
	{
		return "CosmereClient";
	}

	@Override
	public ForgeConfigSpec getConfigSpec()
	{
		return configSpec;
	}

	@Override
	public Type getConfigType()
	{
		return Type.CLIENT;
	}
}