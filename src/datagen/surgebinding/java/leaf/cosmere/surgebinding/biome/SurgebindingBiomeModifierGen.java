/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.biome;


import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import leaf.cosmere.api.Constants;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.registries.SurgebindingFeatures;
import leaf.cosmere.surgebinding.common.world.GemOreBiomeFeatureModifier;
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
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.holdersets.OrHolderSet;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

public record SurgebindingBiomeModifierGen(DataGenerator dataGenerator) implements DataProvider
{
	@Override
	public @NotNull String getName()
	{
		return Surgebinding.MODID + " Biome Modifiers";
	}

	private static HolderSet<Biome> or(HolderSet<Biome>... holders)
	{
		return new OrHolderSet<>(Arrays.asList(holders));
	}

	@Override
	public void run(@NotNull CachedOutput cachedOutput)
	{
		RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.BUILTIN.get());
		final Path outputFolder = this.dataGenerator.getOutputFolder();

		// Biome Modifiers
		final HolderSet.Named<Biome> roshar = new HolderSet.Named<>(
				ops.registry(Registry.BIOME_REGISTRY).get(),
				CosmereTags.Biomes.IS_ROSHAR);

		for (Roshar.Gemstone type : Roshar.Gemstone.values())
		{
			// Biome Modifiers
			GemOreBiomeFeatureModifier oreModifier = new GemOreBiomeFeatureModifier(
					roshar,
					GenerationStep.Decoration.UNDERGROUND_ORES,
					HolderSet.direct(Holder.direct(SurgebindingFeatures.PLACED_GEM_ORE_FEATURES.get(type).get()))
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

		final String biomeModifierPathString = String.join("/", directory, Surgebinding.MODID, biomeModifiersRegistryID.getNamespace(), biomeModifiersRegistryID.getPath(), saveName + ".json");

		BiomeModifier.DIRECT_CODEC.encodeStart(ops, modifier)
				.resultOrPartial(msg -> CosmereAPI.logger.error("Failed to encode {}: {}", biomeModifierPathString, msg))
				.ifPresent(json ->
				{
					try
					{
						final Path biomeModifierPath = outputFolder.resolve(biomeModifierPathString);
						DataProvider.saveStable(cache, json, biomeModifierPath);
					}
					catch (IOException e)
					{
						CosmereAPI.logger.error("Failed to save " + biomeModifierPathString, e);
					}
				});
	}

}