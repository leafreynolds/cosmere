/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

/*
 * File created ~ 22 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.tools;

import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.datamaps.CosmereDataMaps;
import leaf.cosmere.common.datamaps.MetalmindProperties;
import leaf.cosmere.common.datamaps.SpikeProperties;
import leaf.cosmere.tools.common.registries.ToolsItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

//cosmere metals as spikes/metalminds
public class ToolsDataMapGen extends DataMapProvider
{
	public ToolsDataMapGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider)
	{
		super(packOutput, lookupProvider);
	}

	@Override
	protected void gather(HolderLookup.Provider provider)
	{
		var spikes = builder(CosmereDataMaps.SPIKE);
		var metalminds = builder(CosmereDataMaps.METALMIND);

		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			if (!metalType.hasMaterialItem())
			{
				continue;
			}

			if (metalType.hasHemalurgicEffect())
			{
				final SpikeProperties spike = new SpikeProperties(metalType, SpikeProperties.DEFAULT_CHARGE_MODIFIER);
				spikes.add(ToolsItems.METAL_SWORDS.get(metalType).get().builtInRegistryHolder(), spike, false);
				spikes.add(ToolsItems.METAL_AXES.get(metalType).get().builtInRegistryHolder(), spike, false);
				spikes.add(ToolsItems.METAL_PICKAXES.get(metalType).get().builtInRegistryHolder(), spike, false);
				spikes.add(ToolsItems.METAL_SHOVEL.get(metalType).get().builtInRegistryHolder(), spike, false);
				spikes.add(ToolsItems.METAL_HOE.get(metalType).get().builtInRegistryHolder(), spike, false);
			}

			if (metalType.hasFeruchemicalEffect())
			{
				metalminds.add(ToolsItems.METAL_HELMETS.get(metalType).get().builtInRegistryHolder(), new MetalmindProperties(metalType, 5f / 9f), false);
				metalminds.add(ToolsItems.METAL_CHESTPLATES.get(metalType).get().builtInRegistryHolder(), new MetalmindProperties(metalType, 8f / 9f), false);
				metalminds.add(ToolsItems.METAL_LEGGINGS.get(metalType).get().builtInRegistryHolder(), new MetalmindProperties(metalType, 7f / 9f), false);
				metalminds.add(ToolsItems.METAL_BOOTS.get(metalType).get().builtInRegistryHolder(), new MetalmindProperties(metalType, 4f / 9f), false);
			}
		}
	}
}
