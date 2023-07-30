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

    public final ForgeConfigSpec.IntValue PROJECTILE_COOLDOWN;
    public final ForgeConfigSpec.IntValue PROJECTILE_HYDRATION_COST;
    public final ForgeConfigSpec.IntValue PLATFORM_HYDRATION_COST;
    public final ForgeConfigSpec.IntValue LAUNCH_HYDRATION_COST;
    public final ForgeConfigSpec.IntValue ELEVATE_HYDRATION_COST;
    public final ForgeConfigSpec.IntValue CUSHION_HYDRATION_COST;
    public final ForgeConfigSpec.IntValue CHARGE_COST_MULTIPLIER;


    SandmasteryServerConfig()
    {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Sandmastery Config. This config is synced between server and client.").push("sandmastery");

        PROJECTILE_COOLDOWN = builder.comment("How many ticks between each projectile").defineInRange("projectileCooldown", 30, 1, 1000);

        PROJECTILE_HYDRATION_COST = builder.comment("How much hydration is used per projectile").defineInRange("projectileHydrationCost", 30, 1, 1000);
        PLATFORM_HYDRATION_COST = builder.comment("How much hydration is used per tick for platform").defineInRange("platformHydrationCost", 60, 1, 1000);
        LAUNCH_HYDRATION_COST = builder.comment("How much hydration is used per tick for launch").defineInRange("launchHydrationCost", 10, 1, 1000);
        ELEVATE_HYDRATION_COST = builder.comment("How much hydration is used per tick for elevate").defineInRange("elevateHydrationCost", 10, 1, 1000);
        CUSHION_HYDRATION_COST = builder.comment("How much hydration is used per tick for cushion").defineInRange("cushionHydrationCost", 10, 1, 1000);

        CHARGE_COST_MULTIPLIER = builder.comment("Charge cost multiplier is multiplied by the mode to determine charge cost per tick").defineInRange("chargeCostMultiplier", 2, 1, 1000);

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
        PROJECTILE_COOLDOWN.clearCache();
    }
}