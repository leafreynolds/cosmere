/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import com.google.common.collect.ImmutableList;
import leaf.cosmere.api.Constants;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.common.registration.impl.ConfiguredFeatureDeferredRegister;
import leaf.cosmere.common.registration.impl.ConfiguredFeatureRegistryObject;
import leaf.cosmere.common.registration.impl.PlacedFeatureDeferredRegister;
import leaf.cosmere.common.registration.impl.PlacedFeatureRegistryObject;
import leaf.cosmere.common.registry.FeatureRegistry;
import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SurgebindingFeatures
{
	public static final ConfiguredFeatureDeferredRegister CONFIGURED_FEATURES = new ConfiguredFeatureDeferredRegister(Surgebinding.MODID);
	public static final PlacedFeatureDeferredRegister PLACED_FEATURES = new PlacedFeatureDeferredRegister(Surgebinding.MODID);

	public static final Map<Roshar.Gemstone, ConfiguredFeatureRegistryObject<ConfiguredFeature<?, ?>>> CONFIGURED_GEM_ORE_FEATURES =
			Arrays.stream(Roshar.Gemstone.values())
					.collect(Collectors.toMap(
							Function.identity(),
							type -> CONFIGURED_FEATURES.register(
									type.getName() + Constants.RegNameStubs.ORE,
									() -> new ConfiguredFeature<>(
											Feature.ORE,
											new OreConfiguration(
													makeTarget(type),
													9)))
					));

	public static final Map<Roshar.Gemstone, PlacedFeatureRegistryObject<PlacedFeature>> PLACED_GEM_ORE_FEATURES =
			Arrays.stream(Roshar.Gemstone.values())
					.collect(Collectors.toMap(
							Function.identity(),
							type ->
									PLACED_FEATURES.register(
											type.getName() + Constants.RegNameStubs.ORE,
											() -> new PlacedFeature(Holder.direct(CONFIGURED_GEM_ORE_FEATURES.get(type).get()),
													FeatureRegistry.commonOrePlacement(
															8,//width?
															HeightRangePlacement.triangle(
																	VerticalAnchor.aboveBottom(-80),
																	VerticalAnchor.aboveBottom(80)
															)
													)
											)
									)
					));


	private static List<OreConfiguration.TargetBlockState> makeTarget(Roshar.Gemstone type)
	{
		return ImmutableList.of(
				OreConfiguration.target(
						FeatureRegistry.STONE_ORE_REPLACEABLES,
						SurgebindingBlocks.GEM_ORE.get(type).getBlock().defaultBlockState()),
				OreConfiguration.target(
						FeatureRegistry.DEEPSLATE_ORE_REPLACEABLES,
						SurgebindingBlocks.GEM_ORE_DEEPSLATE.get(type).getBlock().defaultBlockState())
		);
	}

}
