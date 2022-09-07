/*
 * File created ~ 7 - 9 - 2022 ~ Leaf
 */

package leaf.cosmere.datagen.biome;

import leaf.cosmere.registry.BiomeRegistry;
import leaf.cosmere.registry.TagsRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BiomeTagsProvider extends net.minecraft.data.tags.BiomeTagsProvider
{
	public BiomeTagsProvider(DataGenerator arg, ExistingFileHelper existingFileHelper)
	{
		super(arg, "cosmere", existingFileHelper);
	}

	@Override
	protected void addTags()
	{
		tag(BiomeRegistry.ROSHAR_BIOME_KEY, TagsRegistry.Biomes.IS_ROSHAR);
		tag(BiomeRegistry.SHADESMAR_BIOME_KEY, TagsRegistry.Biomes.IS_SHADESMAR);
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