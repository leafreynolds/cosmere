/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

/*
 * File created ~ 22 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.common.datamaps;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import javax.annotation.Nullable;

//hemalurgy registers SPIKE, feruchemy METALMIND. a missing module's files are ignored
public final class CosmereDataMaps
{
	public static final DataMapType<Item, SpikeProperties> SPIKE =
			DataMapType.builder(ResourceLocation.fromNamespaceAndPath("hemalurgy", "spike"), Registries.ITEM, SpikeProperties.CODEC)
					.synced(SpikeProperties.CODEC, false)
					.build();

	public static final DataMapType<Item, MetalmindProperties> METALMIND =
			DataMapType.builder(ResourceLocation.fromNamespaceAndPath("feruchemy", "metalmind"), Registries.ITEM, MetalmindProperties.CODEC)
					.synced(MetalmindProperties.CODEC, false)
					.build();

	private CosmereDataMaps()
	{
	}

	@Nullable
	public static SpikeProperties getSpikeProperties(Item item)
	{
		return item.builtInRegistryHolder().getData(SPIKE);
	}

	@Nullable
	public static MetalmindProperties getMetalmindProperties(Item item)
	{
		return item.builtInRegistryHolder().getData(METALMIND);
	}
}
