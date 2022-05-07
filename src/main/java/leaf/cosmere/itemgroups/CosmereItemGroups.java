/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.itemgroups;

import leaf.cosmere.Cosmere;
import leaf.cosmere.registry.BlocksRegistry;
import leaf.cosmere.registry.ItemsRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CosmereItemGroups
{

	public static CreativeModeTab ITEMS = new CreativeModeTab(Cosmere.MODID + ".items")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(ItemsRegistry.GUIDE.get());
		}
	};

	public static CreativeModeTab METALMINDS = new CreativeModeTab(Cosmere.MODID + ".metalminds")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(ItemsRegistry.METAL_BRACELETS.entrySet().stream().findAny().get().getValue().get());
		}
	};

	public static CreativeModeTab HEMALURGIC_SPIKES = new CreativeModeTab(Cosmere.MODID + ".spikes")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(ItemsRegistry.METAL_SPIKE.entrySet().stream().findAny().get().getValue().get());
		}
	};

	public static CreativeModeTab BLOCKS = new CreativeModeTab(Cosmere.MODID + ".blocks")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(BlocksRegistry.GEM_BLOCK.get());
		}
	};

}
