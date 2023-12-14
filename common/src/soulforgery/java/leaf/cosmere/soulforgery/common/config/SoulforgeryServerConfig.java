/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.soulforgery.common.config;

import leaf.cosmere.common.config.ICosmereConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class SoulforgeryServerConfig implements ICosmereConfig
{

	private final ForgeConfigSpec configSpec;

	public final ForgeConfigSpec.IntValue SOULFORGERY;


	SoulforgeryServerConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Soulforgery Config. This config is synced between server and client.").push("Soulforgery");

		SOULFORGERY = builder.comment("Soulforgery.").defineInRange("soulforgery", 0, 0, 100);


		builder.pop();
		configSpec = builder.build();
	}

	@Override
	public String getFileName()
	{
		return "SoulforgeryServer";
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
		SOULFORGERY.clearCache();
	}
}