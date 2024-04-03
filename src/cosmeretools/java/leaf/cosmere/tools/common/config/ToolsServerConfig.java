/*
 * File updated ~ 22 - 3 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.common.config;

import leaf.cosmere.common.config.ICosmereConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class ToolsServerConfig implements ICosmereConfig
{

	private final ForgeConfigSpec configSpec;

	public final ForgeConfigSpec.IntValue TOOLS;


	ToolsServerConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Tools Config. This config is synced between server and client.").push("Tools");

		TOOLS = builder.comment("Tools.").defineInRange("tools", 0, 0, 100);


		builder.pop();
		configSpec = builder.build();
	}

	@Override
	public String getFileName()
	{
		return "ToolsServer";
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
		TOOLS.clearCache();
	}
}