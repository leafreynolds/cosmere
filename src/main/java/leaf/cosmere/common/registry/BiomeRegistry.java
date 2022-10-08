/*
 * File updated ~ 3 - 7 - 2022 ~ Leaf
 */

package leaf.cosmere.common.registry;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registration.impl.BiomeDeferredRegister;
import leaf.cosmere.common.registration.impl.BiomeRegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class BiomeRegistry
{
	public static final BiomeDeferredRegister BIOMES = new BiomeDeferredRegister(Cosmere.MODID);

	public static final BiomeRegistryObject<Biome> SHADESMAR_BIOME = BIOMES.register("shadesmar_biome", OverworldBiomes::stonyPeaks);

	//resource keys
	public static final ResourceKey<Biome> SHADESMAR_BIOME_KEY = ResourceKey.create(Registry.BIOME_REGISTRY, Cosmere.rl("shadesmar_biome"));
	public static final ResourceKey<NoiseGeneratorSettings> SHADESMAR_NOISE_SETTINGS = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, Cosmere.rl("shadesmar_biome"));


}
