package leaf.cosmere.surgebinding.common.worldgen;

import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.registries.SurgebindingBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SurgebindingConfiguredFeatures
{
	public static Map<Roshar.Gemstone, ResourceKey<ConfiguredFeature<?, ?>>> GEMSTONE_ORE_KEY =
			Arrays.stream(EnumUtils.GEMSTONE_TYPES_ORE)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> registerKey(
									type == Roshar.Gemstone.DIAMOND ? "ore_roshar_diamond" : "ore_" + type.getName())
					));

	public static final ResourceKey<ConfiguredFeature<?, ?>> SAPPHIRE_GEODE_KEY = registerKey("sapphire_geode_key");

	public static void boostrap(BootstapContext<ConfiguredFeature<?, ?>> context)
	{
		RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
		RuleTest deepslateReplaceable = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
		Map<Roshar.Gemstone, List<OreConfiguration.TargetBlockState>> overworldGemstoneOres = new HashMap<>(Map.of());

		for (Roshar.Gemstone gemstone : EnumUtils.GEMSTONE_TYPES_ORE)
		{
			overworldGemstoneOres.put(gemstone, List.of(
					OreConfiguration.target(stoneReplaceable,
							SurgebindingBlocks.GEM_ORE.get(gemstone).getBlock().defaultBlockState()),
					OreConfiguration.target(deepslateReplaceable,
							SurgebindingBlocks.GEM_ORE_DEEPSLATE.get(gemstone).getBlock().defaultBlockState())
			));
			register(context, GEMSTONE_ORE_KEY.get(gemstone), Feature.ORE, new OreConfiguration(overworldGemstoneOres.get(gemstone), 5));
		}
		register(context, SAPPHIRE_GEODE_KEY, Feature.GEODE, new GeodeConfiguration(
				new GeodeBlockSettings(BlockStateProvider.simple(Blocks.AIR), BlockStateProvider.simple(SurgebindingBlocks.BLOCK_OF_SAPPHIRE.getBlock()), BlockStateProvider.simple(SurgebindingBlocks.BUDDING_SAPPHIRE.getBlock()),
						BlockStateProvider.simple(Blocks.CALCITE), BlockStateProvider.simple(Blocks.SMOOTH_BASALT),
						List.of(SurgebindingBlocks.LARGE_SAPPHIRE_BUD.getBlock().defaultBlockState(), SurgebindingBlocks.MEDIUM_SAPPHIRE_BUD.getBlock().defaultBlockState(), SurgebindingBlocks.SMALL_SAPPHIRE_BUD.getBlock().defaultBlockState(), SurgebindingBlocks.SAPPHIRE_CLUSTER.getBlock().defaultBlockState()),
						BlockTags.FEATURES_CANNOT_REPLACE, BlockTags.GEODE_INVALID_BLOCKS),
				new GeodeLayerSettings(1.7D, 2.2D, 3.2D, 4.2D),
				new GeodeCrackSettings(0.95D, 2.0D, 2),
				0.35D, 0.083D, true, UniformInt.of(4, 6), UniformInt.of(3, 4), UniformInt.of(1, 2), -16, 16, 0.05D, 1));
	}

	public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name)
	{
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Surgebinding.MODID, name));
	}

	private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
	                                                                                      ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration)
	{
		context.register(key, new ConfiguredFeature<>(feature, configuration));
	}
}
