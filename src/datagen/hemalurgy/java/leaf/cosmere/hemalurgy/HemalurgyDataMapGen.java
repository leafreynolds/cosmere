/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

/*
 * File created ~ 22 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.hemalurgy;

import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.datamaps.CosmereDataMaps;
import leaf.cosmere.common.datamaps.SpikeProperties;
import leaf.cosmere.hemalurgy.common.items.HemalurgicSpikeItem;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

//hemalurgy:spike entries. datapack shape:
//{ "values": { "minecraft:iron_sword": { "metal": "iron" } } }
//piercing metal
public class HemalurgyDataMapGen extends DataMapProvider
{
	public static final Map<Item, Metals.MetalType> VANILLA_SPIKES = vanillaSpikes();

	private static Map<Item, Metals.MetalType> vanillaSpikes()
	{
		Map<Item, Metals.MetalType> map = new LinkedHashMap<>();
		map.put(Items.IRON_SWORD, Metals.MetalType.IRON);
		map.put(Items.IRON_AXE, Metals.MetalType.IRON);
		map.put(Items.IRON_PICKAXE, Metals.MetalType.IRON);
		map.put(Items.IRON_SHOVEL, Metals.MetalType.IRON);
		map.put(Items.IRON_HOE, Metals.MetalType.IRON);
		map.put(Items.GOLDEN_SWORD, Metals.MetalType.GOLD);
		map.put(Items.GOLDEN_AXE, Metals.MetalType.GOLD);
		map.put(Items.GOLDEN_PICKAXE, Metals.MetalType.GOLD);
		map.put(Items.GOLDEN_SHOVEL, Metals.MetalType.GOLD);
		map.put(Items.GOLDEN_HOE, Metals.MetalType.GOLD);
		return map;
	}

	public HemalurgyDataMapGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider)
	{
		super(packOutput, lookupProvider);
	}

	@Override
	protected void gather(HolderLookup.Provider provider)
	{
		var spikes = builder(CosmereDataMaps.SPIKE);

		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			if (!metalType.hasHemalurgicEffect())
			{
				continue;
			}

			final HemalurgicSpikeItem item = HemalurgyItems.METAL_SPIKE.get(metalType).get();
			spikes.add(item.builtInRegistryHolder(), new SpikeProperties(metalType, SpikeProperties.DEFAULT_CHARGE_MODIFIER), false);
		}

		//vanilla metal tools/weapons as spikes
		for (Map.Entry<Item, Metals.MetalType> entry : VANILLA_SPIKES.entrySet())
		{
			spikes.add(entry.getKey().builtInRegistryHolder(), new SpikeProperties(entry.getValue(), SpikeProperties.DEFAULT_CHARGE_MODIFIER), false);
		}
	}
}
