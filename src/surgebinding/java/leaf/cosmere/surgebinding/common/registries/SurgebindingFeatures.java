/*
 * File updated ~ 9 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.common.registration.impl.ConfiguredFeatureDeferredRegister;
import leaf.cosmere.common.registration.impl.PlacedFeatureDeferredRegister;
import leaf.cosmere.surgebinding.common.Surgebinding;

public class SurgebindingFeatures
{
	public static final ConfiguredFeatureDeferredRegister CONFIGURED_FEATURES = new ConfiguredFeatureDeferredRegister(Surgebinding.MODID);
	public static final PlacedFeatureDeferredRegister PLACED_FEATURES = new PlacedFeatureDeferredRegister(Surgebinding.MODID);

	/*public static final Map<Roshar.Gemstone, ConfiguredFeatureRegistryObject<ConfiguredFeature<?, ?>>> CONFIGURED_GEM_ORE_FEATURES =
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
*/
}
