/*
 * File created ~ 3 - 7 - 2022 ~ Leaf
 */

package leaf.cosmere.registry;

import com.mojang.serialization.Codec;
import leaf.cosmere.Cosmere;
import leaf.cosmere.utils.helpers.LogHelper;
import leaf.cosmere.utils.helpers.ResourceLocationHelper;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BiomeRegistry
{
	public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Cosmere.MODID);

	public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, Cosmere.MODID);

	public static final ResourceKey<Biome> ROSHAR_BIOME_KEY = ResourceKey.create(Registry.BIOME_REGISTRY, ResourceLocationHelper.prefix("roshar_biome"));
	public static final RegistryObject<Biome> ROSHAR_BIOME = BIOMES.register("roshar_biome", OverworldBiomes::stonyPeaks);

	public static final ResourceKey<Biome> SHADESMAR_BIOME_KEY = ResourceKey.create(Registry.BIOME_REGISTRY, ResourceLocationHelper.prefix("shadesmar_biome"));
	public static final RegistryObject<Biome> SHADESMAR_BIOME = BIOMES.register("shadesmar_biome", OverworldBiomes::stonyPeaks);

	public static final ResourceKey<NoiseGeneratorSettings> ROSHAR_NOISE_SETTINGS = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, ResourceLocationHelper.prefix("roshar_biome"));
	public static final ResourceKey<NoiseGeneratorSettings> SHADESMAR_NOISE_SETTINGS = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, ResourceLocationHelper.prefix("shadesmar_biome"));


	public static void register()
	{
		LogHelper.info("Registering cosmere biomes");
	}


}
