/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.registry;

import com.google.common.collect.ImmutableList;
import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.constants.Roshar;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FeatureRegistry
{
	public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Cosmere.MODID);
	public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Cosmere.MODID);

	public static final Map<Metals.MetalType, RegistryObject<ConfiguredFeature<?, ?>>> CONFIGURED_METAL_ORE_FEATURES =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasOre)
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

	public static final Map<Metals.MetalType, RegistryObject<PlacedFeature>> PLACED_METAL_ORE_FEATURES =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasOre)
					.collect(Collectors.toMap(
							Function.identity(),
							type ->
									PLACED_FEATURES.register(
											type.getName() + Constants.RegNameStubs.ORE,
											() -> new PlacedFeature(Holder.direct(CONFIGURED_METAL_ORE_FEATURES.get(type).get()),
													commonOrePlacement(
															10,//width?
															HeightRangePlacement.triangle(
																	VerticalAnchor.absolute(-32),
																	VerticalAnchor.absolute(150)
															)
													)
											)
									)
					));
	public static final Map<Roshar.Polestone, RegistryObject<ConfiguredFeature<?, ?>>> CONFIGURED_GEM_ORE_FEATURES =
			Arrays.stream(Roshar.Polestone.values())
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

	public static final Map<Roshar.Polestone, RegistryObject<PlacedFeature>> PLACED_GEM_ORE_FEATURES =
			Arrays.stream(Roshar.Polestone.values())
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

	private static List<OreConfiguration.TargetBlockState> makeTarget(Metals.MetalType metalType)
	{
		return ImmutableList.of(
				OreConfiguration.target(
						OreFeatures.STONE_ORE_REPLACEABLES,
						BlocksRegistry.METAL_ORE.get(metalType).get().defaultBlockState()),
				OreConfiguration.target(
						OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
						BlocksRegistry.METAL_ORE_DEEPSLATE.get(metalType).get().defaultBlockState())
		);
	}
	private static List<OreConfiguration.TargetBlockState> makeTarget(Roshar.Polestone type)
	{
		return ImmutableList.of(
				OreConfiguration.target(
						OreFeatures.STONE_ORE_REPLACEABLES,
						BlocksRegistry.GEM_ORE.get(type).get().defaultBlockState()),
				OreConfiguration.target(
						OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
						BlocksRegistry.GEM_ORE_DEEPSLATE.get(type).get().defaultBlockState())
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
