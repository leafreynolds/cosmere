/*
 * File updated ~ 3 - 7 - 2022 ~ Leaf
 */

package leaf.cosmere.common.registry;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registration.impl.BiomeDeferredRegister;
import leaf.cosmere.common.registration.impl.BiomeRegistryObject;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class BiomeRegistry
{
	public static final BiomeDeferredRegister BIOMES = new BiomeDeferredRegister(Cosmere.MODID);

	//public static final BiomeRegistryObject<Biome> SHADESMAR_BIOME = BIOMES.register("shadesmar_biome", OverworldBiomes::stonyPeaks);

	//resource keys
	public static final ResourceKey<Biome> SHADESMAR_BIOME_KEY = ResourceKey.create(Registries.BIOME, Cosmere.rl("shadesmar_biome"));
	public static final ResourceKey<NoiseGeneratorSettings> SHADESMAR_NOISE_SETTINGS = ResourceKey.create(Registries.NOISE_SETTINGS, Cosmere.rl("shadesmar_biome"));

	public static void bootstrapBiomes(BootstapContext<Biome> context)
	{
		HolderGetter<ConfiguredWorldCarver<?>> carverGetter = context.lookup(Registries.CONFIGURED_CARVER);
		HolderGetter<PlacedFeature> placedFeatureGetter = context.lookup(Registries.PLACED_FEATURE);

		register(context, SHADESMAR_BIOME_KEY, OverworldBiomes.stonyPeaks(placedFeatureGetter, carverGetter));
	}

	private static void register(BootstapContext<Biome> context, ResourceKey<Biome> key, Biome biome)
	{
		context.register(key, biome);
	}
}
