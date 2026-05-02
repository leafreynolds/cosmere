/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 *
 * 1.21.1 changes:
 *  - `BootstapContext` (1.20.1 typo) → `BootstrapContext`
 *  - `net.minecraftforge.common.world.ForgeBiomeModifiers` → `net.neoforged.neoforge.common.world.BiomeModifiers`
 *  - `ForgeRegistries.Keys.BIOME_MODIFIERS` → `NeoForgeRegistries.Keys.BIOME_MODIFIERS`
 *  - `new ResourceLocation(ns, path)` → `ResourceLocation.fromNamespaceAndPath(ns, path)`
 *  - The previous `Function.identity()` collector erased the type for the lambda's `type`
 *    parameter, leading to "Object has no method getName()" — typed it explicitly.
 */

package leaf.cosmere.surgebinding.common.worldgen;

import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SurgebindingBiomeModifiers
{
	public static final Map<Roshar.Gemstone, ResourceKey<BiomeModifier>> ADD_GEMSTONE_ORE =
			Arrays.stream(EnumUtils.GEMSTONE_TYPES_ORE)
					.collect(Collectors.toMap(
							Function.identity(),
							(Roshar.Gemstone type) -> registerKey(type == Roshar.Gemstone.DIAMOND ? "add_rosharan_diamond_ore"
							                                                                      : "add_" + type.getName() + "_ore")
					));
	public static final ResourceKey<BiomeModifier> ADD_SAPPHIRE_GEODE = registerKey("add_sapphire_geode");

	public static void bootstrap(BootstrapContext<BiomeModifier> context)
	{
		var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
		var biomes = context.lookup(Registries.BIOME);

		for (Roshar.Gemstone gemstone : EnumUtils.GEMSTONE_TYPES_ORE)
		{
			context.register(ADD_GEMSTONE_ORE.get(gemstone), new BiomeModifiers.AddFeaturesBiomeModifier(
					biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
					HolderSet.direct(placedFeatures.getOrThrow(SurgebindingPlacedFeatures.GEMSTONE_ORE_PLACED_KEY.get(gemstone))),
					GenerationStep.Decoration.UNDERGROUND_ORES));
		}
		context.register(ADD_SAPPHIRE_GEODE, new BiomeModifiers.AddFeaturesBiomeModifier(
				biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
				HolderSet.direct(placedFeatures.getOrThrow(SurgebindingPlacedFeatures.SAPPHIRE_GEODE_PLACED_KEY)),
				GenerationStep.Decoration.UNDERGROUND_ORES));
	}


	private static ResourceKey<BiomeModifier> registerKey(String name)
	{
		return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(Surgebinding.MODID, name));
	}
}
