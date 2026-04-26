/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.awakening.common.config;

import leaf.cosmere.common.config.ICosmereConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ModConfig.Type;

public class AwakeningServerConfig implements ICosmereConfig
{

	private final ModConfigSpec configSpec;

	public final ModConfigSpec.IntValue AWAKENING;


	AwakeningServerConfig()
	{
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
		builder.comment("Awakening Config. This config is synced between server and client.").push("Awakening");

		AWAKENING = builder.comment("Awakening.").defineInRange("awakening", 0, 0, 100);


		builder.pop();
		configSpec = builder.build();
	}

	@Override
	public String getFileName()
	{
		return "AwakeningServer";
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
		AWAKENING.clearCache();
	}
}
