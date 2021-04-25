/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.config;

import leaf.cosmere.Cosmere;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{

    public static final ForgeConfigSpec COMMON_SPEC;
    private static final CommonConfig COMMON;

    public static final ForgeConfigSpec CLIENT_SPEC;
    private static final ClientConfig CLIENT;
    public static boolean clientConfigBool;

    static
    {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON = specPair.getLeft();
        COMMON_SPEC = specPair.getRight();
    }

    private static Set<String> stringConfigList;
    public static int intConfigValue;
    public static boolean boolConfigValue;

    public static int intConfigValue2;

    static
    {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static boolean isBlacklisted(@Nullable ResourceLocation biome)
    {
        return biome != null && (stringConfigList.contains(biome));
    }

    public static void bakeCommon()
    {
        Config.stringConfigList = COMMON.stringConfigList.get()
                .stream()
                .filter(string -> string.endsWith(":*"))
                .map(string -> string.substring(0, string.length() - 2))
                .collect(Collectors.toSet());

        Config.intConfigValue = COMMON.intConfigValue.get();
        Config.boolConfigValue = COMMON.boolConfigValue.get();

        Config.intConfigValue2 = COMMON.intConfigValue2.get();
    }

    public static void bakeClient()
    {
        Config.clientConfigBool = CLIENT.clientBool.get();
    }

    public static void register()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void onModConfigEvent(ModConfig.ModConfigEvent configEvent)
    {
        if (configEvent.getConfig().getSpec() == Config.COMMON_SPEC)
        {
            bakeCommon();
        }
        else if (configEvent.getConfig().getSpec() == Config.CLIENT_SPEC)
        {
            bakeClient();
        }
    }
}
