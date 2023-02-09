/*
 * File updated ~ 7 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class ClientConfig implements ICosmereConfig
{
	private final ForgeConfigSpec configSpec;
	public final ForgeConfigSpec.BooleanValue clientConfigTest;


	ClientConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Client Config. This config only exists on the client").push("client");

		clientConfigTest = builder.comment("clientConfigTest").translation("config.cosmere.clientconfigtest").define("clientconfigtest", true);

		builder.pop();
		configSpec = builder.build();
	}

	@Override
	public String getFileName()
	{
		return "client";
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