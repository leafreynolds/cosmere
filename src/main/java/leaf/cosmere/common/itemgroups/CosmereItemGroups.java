/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.common.itemgroups;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registry.BlocksRegistry;
import leaf.cosmere.common.registry.ItemsRegistry;
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


	public static CreativeModeTab BLOCKS = new CreativeModeTab(Cosmere.MODID + ".blocks")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(BlocksRegistry.METAL_ORE.entrySet().stream().findAny().get().getValue().getBlock());
		}
	};

}
