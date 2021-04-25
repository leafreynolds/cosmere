/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.config;

import com.google.common.collect.Lists;
import leaf.cosmere.Cosmere;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class CommonConfig
{

    final ForgeConfigSpec.ConfigValue<List<String>> stringConfigList;
    final ForgeConfigSpec.IntValue intConfigValue;
    final ForgeConfigSpec.IntValue intConfigValue2;
    final ForgeConfigSpec.BooleanValue boolConfigValue;

    CommonConfig(ForgeConfigSpec.Builder builder)
    {
        builder.comment("Comment for the config");

        builder.push("section one");

        stringConfigList = builder
                .worldRestart()
                .comment("comment for first config value")
                .translation(Cosmere.MODID + ".config.translation.key")
                .define("config_name_1", Lists.newArrayList("string 1", "string 2", "string 3"));
        intConfigValue = builder
                .comment("comment for 2nd config value")
                .translation(Cosmere.MODID + ".config.translation.key")
                .defineInRange("config_name_2", 123, 0, 1234);
        boolConfigValue = builder
                .comment("comment for 3rd config value")
                .translation(Cosmere.MODID + ".config.translation.key")
                .define("config_name_3", true);


        builder.pop();
        builder.push("section two");
        intConfigValue2 = builder
                .comment("comment for 4th config value")
                .translation(Cosmere.MODID + ".config.translation.key")
                .defineInRange("config_name_4", 1234, 0, Integer.MAX_VALUE);
        builder.pop();
    }
}
