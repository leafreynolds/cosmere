/*
 * File updated ~ 26 - 11 - 2026 ~ Leaf
 */

package leaf.cosmere;

import leaf.cosmere.common.registry.GameEventRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.VibrationFrequency;

import java.util.concurrent.CompletableFuture;

public class CosmereDataMapGen extends DataMapProvider
{
	public CosmereDataMapGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider)
	{
		super(packOutput, lookupProvider);
	}

	@Override
	protected void gather(HolderLookup.Provider provider)
	{
		//sculk on amethyst crashes on any vibration without a frequency
		//so we give ours the 'magic' frequency, like teleport... or lightning.
		builder(NeoForgeDataMaps.VIBRATION_FREQUENCIES)
				.add(GameEventRegistry.KINETIC_INVESTITURE.getRegistryObject().getId(), new VibrationFrequency(14), false);
	}
}
