package leaf.cosmere.surgebinding.common.worldgen;

import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SurgebindingPlacedFeatures
{
	public static final Map<Roshar.Gemstone, ResourceKey<PlacedFeature>> GEMSTONE_ORE_PLACED_KEY =
			Arrays.stream(EnumUtils.GEMSTONE_TYPES_ORE)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> registerKey(type == Roshar.Gemstone.DIAMOND ? "rosharan_diamond_ore_placed"
							                                                    : type.getName() + "_ore_placed")
					));
	public static final ResourceKey<PlacedFeature> SAPPHIRE_GEODE_PLACED_KEY = registerKey("sapphire_geode_placed_key");

	public static void bootstrap(BootstapContext<PlacedFeature> context)
	{
		HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

		for (Roshar.Gemstone gemstone : EnumUtils.GEMSTONE_TYPES_ORE)
		{
			register(context, GEMSTONE_ORE_PLACED_KEY.get(gemstone), configuredFeatures.getOrThrow(SurgebindingConfiguredFeatures.GEMSTONE_ORE_KEY.get(gemstone)),
					SurgebindingOrePlacement.commonOrePlacement(18,
							HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(24))));
		}
		register(context, SAPPHIRE_GEODE_PLACED_KEY, configuredFeatures.getOrThrow(SurgebindingConfiguredFeatures.SAPPHIRE_GEODE_KEY),
				SurgebindingOrePlacement.geodePlacement(24,
						HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(30))));
	}

	private static ResourceKey<PlacedFeature> registerKey(String name)
	{
		return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Surgebinding.MODID, name));
	}

	private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
	                             List<PlacementModifier> modifiers)
	{
		context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
	}
}
