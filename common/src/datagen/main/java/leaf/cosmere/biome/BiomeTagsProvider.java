/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.biome;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.common.registry.BiomeRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BiomeTagsProvider extends net.minecraft.data.tags.BiomeTagsProvider
{
	public BiomeTagsProvider(DataGenerator arg, ExistingFileHelper existingFileHelper)
	{
		super(arg, CosmereAPI.COSMERE_MODID, existingFileHelper);
	}

	@Override
	protected void addTags()
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