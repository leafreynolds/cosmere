/*
 * File updated ~ 4 - 11 - 2023 ~ Leaf
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
	public final ForgeConfigSpec.BooleanValue canHearRain;
	public final ForgeConfigSpec.IntValue pixelationAmount;

	AllomancyClientConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Client Config. This config only exists on the client").push("Allomancy");

		drawMetalLines = builder.comment("Iron/Steel vision draws metal lines between the user and the source of metal").define("drawMetalLines", true);
		drawMetalBoxes = builder.comment("Iron/Steel vision draws a blue box overlay over blocks that contain metal").define("drawMetalBoxes", true);
		canHearRain = builder.comment("Allomantic Tin picks up rain sounds, generating a sculk particle to player and showing an image at position of sound").define("canHearRain", true);
		pixelationAmount = builder.comment("Amount of pixelation on the Allomancy power wheel").defineInRange("pixelationAmount", 1, 1, 10);

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

	@Override
	public void clearCache()
	{
		drawMetalBoxes.clearCache();
		drawMetalLines.clearCache();
		canHearRain.clearCache();
	}
}