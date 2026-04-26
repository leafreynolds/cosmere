/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.aondor.common.config;

import leaf.cosmere.common.config.ICosmereConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ModConfig.Type;

public class AonDorServerConfig implements ICosmereConfig
{

	private final ModConfigSpec configSpec;

	public final ModConfigSpec.IntValue AONDOR;


	AonDorServerConfig()
	{
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
		builder.comment("AonDor Config. This config is synced between server and client.").push("AonDor");

		AONDOR = builder.comment("AonDor.").defineInRange("aondor", 0, 0, 100);


		builder.pop();
		configSpec = builder.build();
	}

	@Override
	public String getFileName()
	{
		return "AonDorServer";
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
		AONDOR.clearCache();
	}
}
