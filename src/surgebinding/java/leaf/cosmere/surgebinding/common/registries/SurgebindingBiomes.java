/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.common.registration.impl.BiomeDeferredRegister;
import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class SurgebindingBiomes
{
	public static final BiomeDeferredRegister BIOMES = new BiomeDeferredRegister(Surgebinding.MODID);

	//public static final BiomeRegistryObject<Biome> ROSHAR_BIOME = BIOMES.register("roshar_biome", OverworldBiomes::stonyPeaks);


	public static final ResourceKey<Biome> ROSHAR_BIOME_KEY = ResourceKey.create(Registries.BIOME, Surgebinding.rl("roshar_biome"));
	public static final ResourceKey<NoiseGeneratorSettings> ROSHAR_NOISE_SETTINGS = ResourceKey.create(Registries.NOISE_SETTINGS, Surgebinding.rl("roshar_biome"));


	public static void bootstrapBiomes(BootstapContext<Biome> context)
	{
		HolderGetter<ConfiguredWorldCarver<?>> carverGetter = context.lookup(Registries.CONFIGURED_CARVER);
		HolderGetter<PlacedFeature> placedFeatureGetter = context.lookup(Registries.PLACED_FEATURE);

		register(context, ROSHAR_BIOME_KEY, OverworldBiomes.stonyPeaks(placedFeatureGetter, carverGetter));
	}

	private static void register(BootstapContext<Biome> context, ResourceKey<Biome> key, Biome biome)
	{
		context.register(key, biome);
	}
}
