/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.itemgroups;

import leaf.cosmere.Cosmere;
import leaf.cosmere.registry.BlocksRegistry;
import leaf.cosmere.registry.ItemsRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CosmereItemGroups
{

    public static ItemGroup ITEMS = new ItemGroup(Cosmere.MODID + ".items")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(ItemsRegistry.GUIDE.get());
        }
    };

    public static ItemGroup METALMINDS = new ItemGroup(Cosmere.MODID + ".metalminds")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(ItemsRegistry.METAL_BRACELETS.entrySet().stream().findAny().get().getValue().get());
        }
    };

    public static ItemGroup BLOCKS = new ItemGroup(Cosmere.MODID + ".blocks")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(BlocksRegistry.GEM_BLOCK.get());
        }
    };

}
