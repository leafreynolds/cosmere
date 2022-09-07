/*
 * File created ~ 3 - 7 - 2022 ~ Leaf
 */

package leaf.cosmere.datagen.biome;


import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.constants.Roshar;
import leaf.cosmere.registry.FeatureRegistry;
import leaf.cosmere.registry.TagsRegistry;
import leaf.cosmere.utils.helpers.LogHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

public record BiomeModifierGen(DataGenerator dataGenerator) implements DataProvider
{
	@Override
	public @NotNull String getName()
	{
		return Cosmere.MODID + " Biome Modifiers";
	}

	@Override
	public void run(@NotNull CachedOutput cachedOutput)
	{
		RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.BUILTIN.get());
		final Path outputFolder = this.dataGenerator.getOutputFolder();

		for (Metals.MetalType type : Metals.MetalType.values())
		{
			if (!type.hasOre())
			{
				continue;
			}

			// Biome Modifiers
			BiomeFeatureModifier oreModifier = new BiomeFeatureModifier(
					new HolderSet.Named<>(
							ops.registry(Registry.BIOME_REGISTRY).get(),
							BiomeTags.IS_OVERWORLD),
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

		for (Roshar.Polestone type : Roshar.Polestone.values())
		{
			// Biome Modifiers
			BiomeFeatureModifier oreModifier = new BiomeFeatureModifier(
					new HolderSet.Named<>(
							ops.registry(Registry.BIOME_REGISTRY).get(),
							TagsRegistry.Biomes.IS_ROSHAR),
					GenerationStep.Decoration.UNDERGROUND_ORES,
					HolderSet.direct(Holder.direct(FeatureRegistry.PLACED_GEM_ORE_FEATURES.get(type).get()))
			);

			// Generate BiomeModiers
			generate(ops,
					oreModifier,
					outputFolder,
					type.getName() + Constants.RegNameStubs.ORE,
					cachedOutput);

		}
	}

	public static void generate(RegistryOps<JsonElement> ops, BiomeModifier modifier, Path outputFolder, String saveName, CachedOutput cache)
	{
		final String directory = PackType.SERVER_DATA.getDirectory();
		final ResourceLocation biomeModifiersRegistryID = ForgeRegistries.Keys.BIOME_MODIFIERS.location();

		final String biomeModifierPathString = String.join("/", directory, Cosmere.MODID, biomeModifiersRegistryID.getNamespace(), biomeModifiersRegistryID.getPath(), saveName + ".json");

		BiomeModifier.DIRECT_CODEC.encodeStart(ops, modifier)
				.resultOrPartial(msg -> LogHelper.LOGGER.error("Failed to encode {}: {}", biomeModifierPathString, msg))
				.ifPresent(json ->
				{
					try
					{
						final Path biomeModifierPath = outputFolder.resolve(biomeModifierPathString);
						DataProvider.saveStable(cache, json, biomeModifierPath);
					}
					catch (IOException e)
					{
						LogHelper.LOGGER.error("Failed to save " + biomeModifierPathString, e);
					}
				});
	}

}