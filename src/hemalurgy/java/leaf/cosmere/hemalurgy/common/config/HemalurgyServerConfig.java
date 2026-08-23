/*
 * File updated ~ 7 - 6 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.config;

import leaf.cosmere.common.config.ICosmereConfig;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.neoforge.common.ModConfigSpec;

public class HemalurgyServerConfig implements ICosmereConfig
{

	private final ModConfigSpec configSpec;

	// Boost amount for Duralumin and Nicrosil
	public final ModConfigSpec.IntValue SPIRITWEB_INTEGRITY_TICK_CHECK;
	public final ModConfigSpec.IntValue LERASATIUM_MAX_SPIKE_STRENGTH;
	public final ModConfigSpec.IntValue CHROMIUM_MAX_SPIKE_STRENGTH;
	public final ModConfigSpec.IntValue DEFAULT_POWER_MAX_SPIKE_STRENGTH;
	public final ModConfigSpec.IntValue SPIKE_TOTAL_STRENGTH_CAPACITY;
    public final ModConfigSpec.IntValue LINCHPIN_SPIKE_SPIRITWEB_BONUS;
    public final ModConfigSpec.IntValue ALLOMANTIC_PEWTER_SPIRITWEB_BONUS;
    public final ModConfigSpec.IntValue FERUCHEMICAL_GOLD_SPIRITWEB_BONUS;


	HemalurgyServerConfig()
	{
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
		builder.comment("Hemalurgy Config. This config is synced between server and client.").push("hemalurgy");

		SPIRITWEB_INTEGRITY_TICK_CHECK = builder.comment("What tick count should be used to check spiritweb integrity? There are 20 ticks in a second.").defineInRange("integrityCheckTick", 20, 1, 1234567890);

		LERASATIUM_MAX_SPIKE_STRENGTH = builder.comment("What is the maximum strength a lerasatium spike can hold.").defineInRange("lerasatiumMaxSpikeStrength", 5, 1, 20);
		CHROMIUM_MAX_SPIKE_STRENGTH = builder.comment("What is the maximum strength a chromium spike can hold.").defineInRange("chromiumMaxSpikeStrength", 3, 1, 5);
		DEFAULT_POWER_MAX_SPIKE_STRENGTH = builder.comment("What is the maximum strength all other power spikes can hold.").defineInRange("powerMaxSpikeStrength", 7, 1, 20);
		SPIKE_TOTAL_STRENGTH_CAPACITY = builder.comment("Total Investiture a single spike can hold, in strength units, shared between hemalurgic charge and feruchemical storage. A feruchemically full spike has no room left to steal into, and a fully invested spike cannot be charged.").defineInRange("spikeMaxTotalStrength", 9, 1, 100);
        LINCHPIN_SPIKE_SPIRITWEB_BONUS = builder.comment("What bonus does a linchpin spike grant spiritweb integrity.").defineInRange("linchpinSpikeSpiritwebBonus", 3, 0, 100);
        ALLOMANTIC_PEWTER_SPIRITWEB_BONUS = builder.comment("What bonus does Allomantic pewter grant spiritweb integrity.").defineInRange("allomanticPewterSpiritwebBonus", 3, 0, 100);
        FERUCHEMICAL_GOLD_SPIRITWEB_BONUS = builder.comment("What bonus does Feruchemical gold grant spiritweb integrity.").defineInRange("feruchemicalGoldSpiritwebBonus", 6, 0, 100);

		builder.pop();
		configSpec = builder.build();
	}

	@Override
	public String getFileName()
	{
		return "HemalurgyServer";
	}

	@Override
	public ModConfigSpec getConfigSpec()
	{
		return configSpec;
	}

	@Override
	public Type getConfigType()
	{
		return Type.SERVER;
	}

	@Override
	public void clearCache() {  }
}
