/*
 * File updated ~ 7 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.biome;


import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import leaf.cosmere.api.Constants;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registry.FeatureRegistry;
import leaf.cosmere.common.world.MetalOreBiomeFeatureModifier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.holdersets.OrHolderSet;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public record BiomeModifierGen(net.minecraft.data.PackOutput dataGenerator) implements DataProvider
{
	@Override
	public @NotNull String getName()
	{
		return Cosmere.MODID + " Biome Modifiers";
	}

	private static HolderSet<Biome> or(HolderSet<Biome>... holders)
	{
		return new OrHolderSet<>(Arrays.asList(holders));
	}

	@Override
	public CompletableFuture<?> run(@NotNull CachedOutput cachedOutput)
	{
		RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.BUILTIN.get());
		final Path outputFolder = this.dataGenerator.getOutputFolder();

		// Biome Modifiers
		final HolderSet.Named<Biome> overworld = new HolderSet.Named<>(
				ops.registry(Registries.BIOME).get(),
				BiomeTags.IS_OVERWORLD);
		final HolderSet.Named<Biome> roshar = new HolderSet.Named<>(
				ops.registry(Registries.BIOME).get(),
				CosmereTags.Biomes.IS_ROSHAR);

		for (Metals.MetalType type : Metals.MetalType.values())
		{
			if (!type.hasOre())
			{
				continue;
			}

			MetalOreBiomeFeatureModifier oreModifier = new MetalOreBiomeFeatureModifier(
					or(overworld, roshar),
					GenerationStep.Decoration.UNDERGROUND_ORES,
					HolderSet.direct(Holder.direct(FeatureRegistry.PLACED_METAL_ORE_FEATURES.get(type).get()))
			);

			// Generate BiomeModiers
			generate(ops,
					oreModifier,
					outputFolder,
					type.getName() + Constants.RegNameStubs.ORE,
					cachedOutput);
		}

		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
	}

	public static void generate(RegistryOps<JsonElement> ops, BiomeModifier modifier, Path outputFolder, String saveName, CachedOutput cache)
	{
		final String directory = PackType.SERVER_DATA.getDirectory();
		final ResourceLocation biomeModifiersRegistryID = ForgeRegistries.Keys.BIOME_MODIFIERS.location();

		final String biomeModifierPathString = String.join("/", directory, Cosmere.MODID, biomeModifiersRegistryID.getNamespace(), biomeModifiersRegistryID.getPath(), saveName + ".json");

		BiomeModifier.DIRECT_CODEC.encodeStart(ops, modifier)
				.resultOrPartial(msg -> CosmereAPI.logger.error("Failed to encode {}: {}", biomeModifierPathString, msg))
				.ifPresent(json ->
				{
					final Path biomeModifierPath = outputFolder.resolve(biomeModifierPathString);
					DataProvider.saveStable(cache, json, biomeModifierPath);
				});
	}

}