/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.registry;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.ResourceLocationHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FeatureRegistry
{
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Cosmere.MODID);

	public static final Map<Metals.MetalType, RegistryObject<Feature<OreConfiguration>>> ORE_FEATURES =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasOre)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> FEATURES.register(
									type.getName() + Constants.RegNameStubs.ORE,
									() -> new OreFeature(OreConfiguration.CODEC))));


	// The "New Tardis Mod" code says we should register configured versions of the features in FMLCommonSetup
	// since it helps prevent mod incompatibility issues. No need to delete other mod's world gen. Thank you 50!

	public static class ConfiguredFeatures
	{
		//todo have ore specific changes, rather than all using the same settings
		//reference Features.java
		public static final Map<Metals.MetalType, Holder<ConfiguredFeature<?, ?>>> ORE_FEATURES =
				Arrays.stream(Metals.MetalType.values())
						.filter(Metals.MetalType::hasOre)
						.collect(Collectors.toMap(
								Function.identity(),
								metalType -> registerConfiguredFeature(
										metalType.getName() + Constants.RegNameStubs.ORE,
										new ConfiguredFeature<>(
												FeatureRegistry.ORE_FEATURES.get(metalType).get(),
												new OreConfiguration(makeTarget(metalType), 7)
										)
								)));

		private static List<OreConfiguration.TargetBlockState> makeTarget(Metals.MetalType metalType)
		{
			return
					List.of(
							OreConfiguration.target(
									OreFeatures.STONE_ORE_REPLACEABLES,
									BlocksRegistry.METAL_ORE.get(metalType).get().defaultBlockState()),
							OreConfiguration.target(
									OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
									BlocksRegistry.METAL_ORE_DEEPSLATE.get(metalType).get().defaultBlockState())
					);
		}


		public static void registerConfiguredFeatures()
		{
			//call to static will trigger static finals to be created
		}
	}

	public static class PlacedFeatures
	{
		public static final Map<Metals.MetalType, Holder<PlacedFeature>> PLACED_ORE_FEATURES =
				Arrays.stream(Metals.MetalType.values())
						.filter(Metals.MetalType::hasOre)
						.collect(Collectors.toMap(
								Function.identity(),
								metalType ->
										registerPlacedFeature(
												metalType.getName() + Constants.RegNameStubs.ORE,
												ConfiguredFeatures.ORE_FEATURES.get(metalType),
												commonOrePlacement(
														10,//width?
														HeightRangePlacement.triangle(
																VerticalAnchor.absolute(-32),
																VerticalAnchor.absolute(150))
												)
										)
								)
						);


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

		public static void registerPlacedFeatures()
		{
			//call to static will trigger static finals to be created
		}
	}


	private static Holder<PlacedFeature> registerPlacedFeature(String registryName, Holder<ConfiguredFeature<?, ?>> configuredFeature, List<PlacementModifier> placementModifiers)
	{
		return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, ResourceLocationHelper.prefix(registryName), new PlacedFeature(Holder.hackyErase(configuredFeature), List.copyOf(placementModifiers)));
	}

	private static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<ConfiguredFeature<?, ?>> registerConfiguredFeature(String registryName, ConfiguredFeature<?, ?> configuredFeature)
	{
		Registry<ConfiguredFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_FEATURE;
		return BuiltinRegistries.register(registry, ResourceLocationHelper.prefix(registryName), configuredFeature);
	}
}
