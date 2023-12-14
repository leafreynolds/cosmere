/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.example.common.registries;

import leaf.cosmere.common.registration.impl.BiomeDeferredRegister;
import leaf.cosmere.example.common.Example;

public class ExampleBiomes
{
	public static final BiomeDeferredRegister BIOMES = new BiomeDeferredRegister(Example.MODID);

	//public static final BiomeRegistryObject<Biome> EXAMPLE_BIOME = BIOMES.register("example_biome", OverworldBiomes::stonyPeaks);
}
