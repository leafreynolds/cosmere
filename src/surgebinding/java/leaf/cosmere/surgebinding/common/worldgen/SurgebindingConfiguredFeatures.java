package leaf.cosmere.surgebinding.common.worldgen;

import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.registries.SurgebindingBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class SurgebindingConfiguredFeatures
{
	public static final ResourceKey<ConfiguredFeature<?,?>> SMOKESTONE_ORE_KEY = registerKey("smokestone_ore");

	public static void boostrap(BootstapContext<ConfiguredFeature<?,?>> context){
		RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
		RuleTest deepslateReplaceable = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

		List<OreConfiguration.TargetBlockState> overworldSmokestoneOres = List.of(
				OreConfiguration.target(stoneReplaceable,
						Blocks.ACACIA_LEAVES.defaultBlockState()),
				OreConfiguration.target(deepslateReplaceable,
								SurgebindingBlocks.GEM_ORE_DEEPSLATE.get(Roshar.Gemstone.SMOKESTONE).getBlock().defaultBlockState())
		);

		register(context, SMOKESTONE_ORE_KEY, Feature.ORE, new OreConfiguration(overworldSmokestoneOres, 3));


	}

	public static ResourceKey<ConfiguredFeature<?,?>> registerKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Surgebinding.MODID, name));
	}

	private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
	                                                                                      ResourceKey<ConfiguredFeature<?,?>> key, F feature, FC configuration) {
		context.register(key, new ConfiguredFeature<>(feature,configuration));
	}
}