/*
 * File updated ~ 10 - 6 - 2025 ~ Soar
 */

package leaf.cosmere.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class CosmereClientConfig implements ICosmereConfig
{
	private final ForgeConfigSpec configSpec;
	public final ForgeConfigSpec.BooleanValue disableItemTinting;
	public final ForgeConfigSpec.BooleanValue disableSelectedManifestationHud;
	public final ForgeConfigSpec.IntValue hudXCoordinate;
	public final ForgeConfigSpec.IntValue hudYCoordinate;
	public final ForgeConfigSpec.IntValue hudSize;
	public final ForgeConfigSpec.BooleanValue disableActivatorChatMessage;


	CosmereClientConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Client Config. This config only exists on the client").push("cosmere");

		disableItemTinting = builder.comment("Lets you disable cosmere item and block tinting for metal items and blocks").define("disableItemTinting", false);

		disableSelectedManifestationHud = builder.comment("Disables the HUD for selected power").define("disableSelectedManifestationHud", false);

		hudXCoordinate = builder.comment("X coordinate for the HUD").defineInRange("hudXCoordinate", 10, 0, Integer.MAX_VALUE);
		hudYCoordinate = builder.comment("Y coordinate for the HUD").defineInRange("hudYCoordinate", 20, 0, Integer.MAX_VALUE);
		hudSize = builder.comment("Size of the icon in the HUD; both width and height").defineInRange("hudSize", 40, 0, Integer.MAX_VALUE);

		disableActivatorChatMessage = builder.comment("Disables the chat message alerting you when you active or save a power state").define("disableActivatorChatMessage", false);
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

	@Override
	public void clearCache()
	{
		disableItemTinting.clearCache();
	}
}