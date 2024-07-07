/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.common.registry;

import com.google.common.collect.ImmutableList;
import leaf.cosmere.api.Constants;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registration.impl.ConfiguredFeatureDeferredRegister;
import leaf.cosmere.common.registration.impl.ConfiguredFeatureRegistryObject;
import leaf.cosmere.common.registration.impl.PlacedFeatureDeferredRegister;
import leaf.cosmere.common.registration.impl.PlacedFeatureRegistryObject;
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

public class FeatureRegistry
{
	public static final ConfiguredFeatureDeferredRegister CONFIGURED_FEATURES = new ConfiguredFeatureDeferredRegister(Cosmere.MODID);
	public static final PlacedFeatureDeferredRegister PLACED_FEATURES = new PlacedFeatureDeferredRegister(Cosmere.MODID);

	// todo: might need to be redone? ore and placed features are already in the generated jsons, so might be fine
	// hard to tell while it doesn't build
//	public static final Map<Metals.MetalType, ConfiguredFeatureRegistryObject<ConfiguredFeature<?, ?>>> CONFIGURED_METAL_ORE_FEATURES =
//			Arrays.stream(Metals.MetalType.values())
//					.filter(Metals.MetalType::hasOre)
//					.collect(Collectors.toMap(
//							Function.identity(),
//							type -> CONFIGURED_FEATURES.register(
//									type.getName() + Constants.RegNameStubs.ORE,
//									() -> new ConfiguredFeature<>(
//											Feature.ORE,
//											new OreConfiguration(
//													makeTarget(type),
//													9)))
//					));

//	public static final Map<Metals.MetalType, PlacedFeatureRegistryObject<PlacedFeature>> PLACED_METAL_ORE_FEATURES =
//			Arrays.stream(Metals.MetalType.values())
//					.filter(Metals.MetalType::hasOre)
//					.collect(Collectors.toMap(
//							Function.identity(),
//							type ->
//									PLACED_FEATURES.register(
//											type.getName() + Constants.RegNameStubs.ORE,
//											() -> new PlacedFeature(Holder.direct(CONFIGURED_METAL_ORE_FEATURES.get(type).get()),
//													commonOrePlacement(
//															10,//width?
//															HeightRangePlacement.triangle(
//																	VerticalAnchor.absolute(-32),
//																	VerticalAnchor.absolute(150)
//															)
//													)
//											)
//									)
//					));


//	private static List<OreConfiguration.TargetBlockState> makeTarget(Metals.MetalType metalType)
//	{
//		return ImmutableList.of(
//				OreConfiguration.target(
//						OreFeatures.STONE_ORE_REPLACEABLES,
//						BlocksRegistry.METAL_ORE.get(metalType).getBlock().defaultBlockState()),
//				OreConfiguration.target(
//						OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
//						BlocksRegistry.METAL_ORE_DEEPSLATE.get(metalType).getBlock().defaultBlockState())
//		);
//	}


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

	private static List<PlacementModifier> orePlacement(PlacementModifier countPlacement, PlacementModifier heightRange)
	{
		return List.of(countPlacement, InSquarePlacement.spread(), heightRange, BiomeFilter.biome());
	}

}
