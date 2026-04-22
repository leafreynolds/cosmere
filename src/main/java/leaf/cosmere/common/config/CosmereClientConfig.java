/*
 * File updated ~ 10 - 6 - 2025 ~ Soar
 */

package leaf.cosmere.common.config;

import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CosmereClientConfig implements ICosmereConfig
{
	private final ModConfigSpec configSpec;
	public final ModConfigSpec.BooleanValue disableItemTinting;
	public final ModConfigSpec.BooleanValue disableSelectedManifestationHud;
	public final ModConfigSpec.IntValue hudXCoordinate;
	public final ModConfigSpec.IntValue hudYCoordinate;
	public final ModConfigSpec.IntValue hudSize;
	public final ModConfigSpec.BooleanValue disableActivatorChatMessage;


	CosmereClientConfig()
	{
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
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
	public ModConfigSpec getConfigSpec()
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
