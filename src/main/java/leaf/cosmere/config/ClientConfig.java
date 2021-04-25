/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.config;

import leaf.cosmere.Cosmere;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig
{

    final ForgeConfigSpec.BooleanValue clientBool;

    ClientConfig(ForgeConfigSpec.Builder builder)
    {
        builder.push("clientSection1");
        clientBool = builder
                .worldRestart()
                .comment("comment for 2nd config value")
                .translation(Cosmere.MODID + ".config.translation.key")
                .define("config_name_1", true);
    }
}
