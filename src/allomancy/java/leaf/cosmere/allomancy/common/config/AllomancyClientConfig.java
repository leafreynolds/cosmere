/*
 * File updated ~ 7 - 6 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.config;

import leaf.cosmere.common.config.ICosmereConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class AllomancyClientConfig implements ICosmereConfig
{
	private final ForgeConfigSpec configSpec;
	public final ForgeConfigSpec.BooleanValue drawMetalLines;
	public final ForgeConfigSpec.BooleanValue drawMetalBoxes;


	AllomancyClientConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Client Config. This config only exists on the client").push("Allomancy");

		drawMetalLines = builder.comment("Iron/Steel vision draws metal lines between the user and the source of metal").define("drawMetalLines", true);

		drawMetalBoxes = builder.comment("Iron/Steel vision draws a blue box overlay over blocks that contain metal").define("drawMetalBoxes", true);

		builder.pop();
		configSpec = builder.build();
	}

	@Override
	public String getFileName()
	{
		return "AllomancyClient";
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