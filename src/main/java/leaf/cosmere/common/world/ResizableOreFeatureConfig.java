/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.common.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import leaf.cosmere.common.config.CosmereConfigs;
import leaf.cosmere.common.config.CosmereWorldConfig;
import leaf.cosmere.common.resource.ore.OreType;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public record ResizableOreFeatureConfig(List<TargetBlockState> targetStates, OreType.OreVeinType oreVeinType,
                                        ModConfigSpec.ConfigValue<Integer> size,
                                        ModConfigSpec.ConfigValue<Double> discardChanceOnAirExposure) implements FeatureConfiguration
{

	public static final Codec<ResizableOreFeatureConfig> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Codec.list(TargetBlockState.CODEC).fieldOf("targets").forGetter(config -> config.targetStates),
			OreType.OreVeinType.CODEC.fieldOf("oreVeinType").forGetter(config -> config.oreVeinType)
	).apply(builder, (targetStates, oreVeinType) ->
	{
		CosmereWorldConfig.OreVeinConfig veinConfig = CosmereConfigs.WORLD_CONFIG.getVeinConfig(oreVeinType);
		return new ResizableOreFeatureConfig(targetStates, oreVeinType, veinConfig.maxVeinSize(), veinConfig.discardChanceOnAirExposure());
	}));
}