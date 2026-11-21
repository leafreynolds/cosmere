/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.common.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import leaf.cosmere.common.resource.ore.BaseOreConfig;
import leaf.cosmere.common.resource.ore.OreType;
import leaf.cosmere.common.util.CosmereEnumUtils;
import leaf.cosmere.common.world.height.ConfigurableHeightRange;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class CosmereWorldConfig implements ICosmereConfig
{

	private final ModConfigSpec configSpec;

	private final Map<OreType, OreConfig> ores = new EnumMap<>(OreType.class);


	CosmereWorldConfig()
	{
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
		builder.comment("World generation settings for Cosmere. This config is synced from server to client")
				.push("world_generation");

		for (OreType ore : CosmereEnumUtils.ORE_TYPES)
		{
			ores.put(ore, new OreConfig(builder, ore));
		}

		builder.pop();
		configSpec = builder.build();
	}

	@Override
	public String getFileName()
	{
		return "world";
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
	public boolean addToContainer()
	{
		return false;
	}

	public OreVeinConfig getVeinConfig(OreType.OreVeinType oreVeinType)
	{
		return ores.get(oreVeinType.type()).veinConfigs.get(oreVeinType.index());
	}

	public record OreVeinConfig(BooleanSupplier shouldGenerate,
	                            ModConfigSpec.ConfigValue<Integer> perChunk,
	                            ModConfigSpec.ConfigValue<Integer> maxVeinSize,
	                            ModConfigSpec.ConfigValue<Double> discardChanceOnAirExposure,
	                            ConfigurableHeightRange range)
	{
	}

	private static class OreConfig
	{

		private final ModConfigSpec.BooleanValue shouldGenerate;
		private final List<OreVeinConfig> veinConfigs;

		private OreConfig(ModConfigSpec.Builder builder, OreType oreType)
		{
			String ore = oreType.getMetalType().getName();

			builder.comment("Generation Settings for " + ore + " ore.")
					.push(ore);

			this.shouldGenerate = builder
					.comment("Determines if " + ore + " ore should be added to world generation.")
					.define("shouldGenerate", true);

			Builder<OreVeinConfig> veinBuilder = ImmutableList.builder();

			for (BaseOreConfig baseConfig : oreType.getBaseConfigs())
			{
				String veinType = baseConfig.name() + " " + ore + " vein";

				builder.comment(veinType + " Generation Settings.")
						.push(baseConfig.name());

				ModConfigSpec.BooleanValue shouldVeinTypeGenerate = builder
						.comment("Determines if " + veinType + "s should be added to world generation. Note: Requires generating " + ore + " ore to be enabled.")
						.define("shouldGenerate", true);

				veinBuilder.add(
						new OreVeinConfig(
								() -> this.shouldGenerate.get() && shouldVeinTypeGenerate.get(),
								builder.comment("Chance that " + veinType + "s generates in a chunk.")
										.defineInRange("perChunk", baseConfig.perChunk(), 1, 256),
								builder.comment("Maximum number of blocks in a " + veinType + ".")
										.defineInRange("maxVeinSize", baseConfig.maxVeinSize(), 1, 64),
								builder.comment("Chance that blocks that are directly exposed to air in a " + veinType + " are not placed.")
										.defineInRange("discardChanceOnAirExposure", baseConfig.discardChanceOnAirExposure(), 0, 1),
								ConfigurableHeightRange.create(builder, veinType, baseConfig)
						));
				builder.pop();
			}
			veinConfigs = veinBuilder.build();
			builder.pop();
		}
	}

	@Override
	public void clearCache()
	{
		for (OreType ore : CosmereEnumUtils.ORE_TYPES)
		{
			final OreConfig oreConfig = ores.get(ore);
			oreConfig.shouldGenerate.clearCache();
			for (var veinConfig : oreConfig.veinConfigs)
			{
				veinConfig.discardChanceOnAirExposure.clearCache();
				veinConfig.maxVeinSize.clearCache();
				veinConfig.perChunk.clearCache();
				veinConfig.range.plateau().clearCache();
			}
		}
	}
}
