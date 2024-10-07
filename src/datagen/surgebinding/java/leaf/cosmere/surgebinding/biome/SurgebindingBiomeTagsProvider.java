/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.biome;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.registries.SurgebindingBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class SurgebindingBiomeTagsProvider extends net.minecraft.data.tags.BiomeTagsProvider
{
	public SurgebindingBiomeTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper)
	{
		super(packOutput, lookupProvider, Surgebinding.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider pProvider)
	{
		tag(SurgebindingBiomes.ROSHAR_BIOME_KEY, CosmereTags.Biomes.IS_ROSHAR);
	}

	@SafeVarargs
	private void tag(ResourceKey<Biome> biome, TagKey<Biome>... tags)
	{
		for (TagKey<Biome> key : tags)
		{
			tag(key).add(biome);
		}
	}

	@Override
	public String getName()
	{
		return "Surgebinding Biome Tags";
	}
}