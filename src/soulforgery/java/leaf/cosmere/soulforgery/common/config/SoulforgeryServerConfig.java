/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.soulforgery.common.config;

import leaf.cosmere.common.config.ICosmereConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ModConfig.Type;

public class SoulforgeryServerConfig implements ICosmereConfig
{

	private final ModConfigSpec configSpec;

	public final ModConfigSpec.IntValue SOULFORGERY;


	SoulforgeryServerConfig()
	{
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
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
		SOULFORGERY.clearCache();
	}
}
