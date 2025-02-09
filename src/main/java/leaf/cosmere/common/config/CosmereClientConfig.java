/*
 * File updated ~ 30 - 7 - 2023 ~ Leaf
 */

package leaf.cosmere.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class CosmereClientConfig implements ICosmereConfig
{
	private final ForgeConfigSpec configSpec;
	public final ForgeConfigSpec.BooleanValue disableItemTinting;
	public final ForgeConfigSpec.BooleanValue disableSelectedManifestationHud;


	CosmereClientConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Client Config. This config only exists on the client").push("cosmere");

		disableItemTinting = builder.comment("Lets you disable item and block tinting for metal items and blocks").define("disableItemTinting", false);

		disableSelectedManifestationHud = builder.comment("Disables the HUD for selected power").define("disableSelectedManifestationHud", false);

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