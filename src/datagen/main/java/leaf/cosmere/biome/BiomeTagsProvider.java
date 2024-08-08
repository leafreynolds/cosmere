/*
 * File updated ~ 7 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.biome;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.common.registry.BiomeRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class BiomeTagsProvider extends net.minecraft.data.tags.BiomeTagsProvider
{
	public BiomeTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper)
	{
		super(packOutput, lookupProvider, CosmereAPI.COSMERE_MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider pProvider)
	{
		tag(BiomeRegistry.SHADESMAR_BIOME_KEY, CosmereTags.Biomes.IS_SHADESMAR);
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
		return "Cosmere Biome Tags";
	}
}