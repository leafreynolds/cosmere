/*
 * File updated ~ 7 - 6 - 2023 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.config;

import leaf.cosmere.common.config.ICosmereConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class SandmasteryServerConfig implements ICosmereConfig
{

    private final ForgeConfigSpec configSpec;

    public final ForgeConfigSpec.DoubleValue someValue;


    SandmasteryServerConfig()
    {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Sandmastery Config. This config is synced between server and client.").push("sandmastery");

        someValue = builder.comment("some value but with comment").defineInRange("someValue", 0.334D, 0D, 1D);

        builder.pop();
        configSpec = builder.build();
    }

    @Override
    public String getFileName()
    {
        return "SandmasteryServer";
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
        someValue.clearCache();
    }
}