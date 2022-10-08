/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import com.google.common.collect.ImmutableList;
import leaf.cosmere.api.Constants;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.common.registration.impl.ConfiguredFeatureDeferredRegister;
import leaf.cosmere.common.registration.impl.ConfiguredFeatureRegistryObject;
import leaf.cosmere.common.registration.impl.PlacedFeatureDeferredRegister;
import leaf.cosmere.common.registration.impl.PlacedFeatureRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;

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
													commonOrePlacement(
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
						OreFeatures.STONE_ORE_REPLACEABLES,
						SurgebindingBlocks.GEM_ORE.get(type).getBlock().defaultBlockState()),
				OreConfiguration.target(
						OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
						SurgebindingBlocks.GEM_ORE_DEEPSLATE.get(type).getBlock().defaultBlockState())
		);
	}

	// The "New Tardis Mod" code says we should register configured versions of the features in FMLCommonSetup
	// since it helps prevent mod incompatibility issues. No need to delete other mod's world gen. Thank you 50!


	//copied from OrePlacements.java, since they're private methods that should really be public
	private static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange)
	{
		return orePlacement(CountPlacement.of(pCount), pHeightRange);
	}

	private static List<PlacementModifier> rareOrePlacement(int pCount, PlacementModifier pHeightRange)
	{
		return orePlacement(RarityFilter.onAverageOnceEvery(pCount), pHeightRange);
	}

	private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_)
	{
		return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
	}

}
