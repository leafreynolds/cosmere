/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.common.registration.impl.BiomeDeferredRegister;
import leaf.cosmere.common.registration.impl.BiomeRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class SurgebindingBiomes
{
	public static final BiomeDeferredRegister BIOMES = new BiomeDeferredRegister(Surgebinding.MODID);

	public static final BiomeRegistryObject<Biome> ROSHAR_BIOME = BIOMES.register("roshar_biome", OverworldBiomes::stonyPeaks);


	public static final ResourceKey<Biome> ROSHAR_BIOME_KEY = ResourceKey.create(Registry.BIOME_REGISTRY, Surgebinding.rl("roshar_biome"));
	public static final ResourceKey<NoiseGeneratorSettings> ROSHAR_NOISE_SETTINGS = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, Surgebinding.rl("roshar_biome"));


}
