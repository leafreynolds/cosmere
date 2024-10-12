/*
 * File updated ~ 3 - 7 - 2022 ~ Leaf
 */

package leaf.cosmere.common.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import leaf.cosmere.common.registry.BiomeModifierRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public record MetalOreBiomeFeatureModifier(HolderSet<Biome> biomes, GenerationStep.Decoration generationStep,
                                           HolderSet<PlacedFeature> features) implements BiomeModifier
{

	@Override
	public Codec<? extends BiomeModifier> codec()
	{
		return BiomeModifierRegistry.METAL_ORE_MODIFIER.get();
	}

	public static Codec<MetalOreBiomeFeatureModifier> makeCodec()
	{
		return RecordCodecBuilder.create(builder -> builder.group(
				Biome.LIST_CODEC.fieldOf("biomes").forGetter(MetalOreBiomeFeatureModifier::biomes),
				Codec.STRING.comapFlatMap(MetalOreBiomeFeatureModifier::generationStageFromString, GenerationStep.Decoration::toString).fieldOf("generation_stage").forGetter(MetalOreBiomeFeatureModifier::generationStep),
				PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(MetalOreBiomeFeatureModifier::features)
		).apply(builder, MetalOreBiomeFeatureModifier::new));
	}

	@Override
	public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder)
	{
		if (phase == Phase.ADD && this.biomes.contains(biome))
		{
			BiomeGenerationSettingsBuilder generation = builder.getGenerationSettings();
			this.features.forEach(holder -> generation.addFeature(this.generationStep, holder));
		}
	}

	private static DataResult<GenerationStep.Decoration> generationStageFromString(String name)
	{
		try
		{
			return DataResult.success(GenerationStep.Decoration.valueOf(name));
		}
		catch (Exception e)
		{
			return DataResult.error(() -> "Not a decoration stage: " + name);
		}
	}
}