/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.registry;

import com.google.common.collect.ImmutableList;
import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Metals;
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

	public static final Map<Metals.MetalType, RegistryObject<ConfiguredFeature<?, ?>>> CONFIGURED_ORE_FEATURES =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasOre)
					.collect(Collectors.toMap(
							Function.identity(),
							metalType -> CONFIGURED_FEATURES.register(
									metalType.getName() + Constants.RegNameStubs.ORE,
									() -> new ConfiguredFeature<>(
											Feature.ORE,
											new OreConfiguration(
													makeTarget(metalType),
													9)))
					));

	public static final Map<Metals.MetalType, RegistryObject<PlacedFeature>> PLACED_ORE_FEATURES =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasOre)
					.collect(Collectors.toMap(
							Function.identity(),
							metalType ->
									PLACED_FEATURES.register(
											metalType.getName() + Constants.RegNameStubs.ORE,
											() -> new PlacedFeature(Holder.direct(CONFIGURED_ORE_FEATURES.get(metalType).get()),
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

	// The "New Tardis Mod" code says we should register configured versions of the features in FMLCommonSetup
	// since it helps prevent mod incompatibility issues. No need to delete other mod's world gen. Thank you 50!


	//copied from OrePlacements.java, since they're private methods that should really be public
	private static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_)
	{
		return orePlacement(CountPlacement.of(p_195344_), p_195345_);
	}

	private static List<PlacementModifier> rareOrePlacement(int p_195350_, PlacementModifier p_195351_)
	{
		return orePlacement(RarityFilter.onAverageOnceEvery(p_195350_), p_195351_);
	}

	private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_)
	{
		return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
	}

}
