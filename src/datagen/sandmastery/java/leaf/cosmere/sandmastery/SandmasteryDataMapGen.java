package leaf.cosmere.sandmastery;

import leaf.cosmere.sandmastery.common.datamaps.HydrationProperties;
import leaf.cosmere.sandmastery.common.datamaps.SandmasteryDataMaps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

//sandmastery:hydration entries. datapack shape:
//{ "values": { "minecraft:potion": { "amount": 1000 } } }
public class SandmasteryDataMapGen extends DataMapProvider
{
	public SandmasteryDataMapGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider)
	{
		super(packOutput, lookupProvider);
	}

	@Override
	protected void gather(HolderLookup.Provider provider)
	{
		builder(SandmasteryDataMaps.HYDRATION)
				.add(BuiltInRegistries.ITEM.wrapAsHolder(Items.POTION), new HydrationProperties(1000), false);
		// allomancy:metal_vial to be added (by ResourceLocation, so allomancy stays optional) once its amount is decided
	}
}
