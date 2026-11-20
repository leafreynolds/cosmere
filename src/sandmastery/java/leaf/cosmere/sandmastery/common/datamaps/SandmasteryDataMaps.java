package leaf.cosmere.sandmastery.common.datamaps;

import leaf.cosmere.sandmastery.common.Sandmastery;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import javax.annotation.Nullable;

public final class SandmasteryDataMaps
{
	public static final DataMapType<Item, HydrationProperties> HYDRATION =
			DataMapType.builder(ResourceLocation.fromNamespaceAndPath(Sandmastery.MODID, "hydration"), Registries.ITEM, HydrationProperties.CODEC)
					.build();

	private SandmasteryDataMaps()
	{
	}

	@Nullable
	public static HydrationProperties getHydration(ItemStack stack)
	{
		return stack.getItemHolder().getData(HYDRATION);
	}
}
