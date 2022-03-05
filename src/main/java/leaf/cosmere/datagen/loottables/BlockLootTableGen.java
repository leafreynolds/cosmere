/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.loottables;

import leaf.cosmere.blocks.MetalOreBlock;
import leaf.cosmere.registry.BlocksRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraftforge.fml.RegistryObject;

public class BlockLootTableGen extends BlockLootTables
{
    @Override
    protected void addTables()
    {
		/*this.registerLootTable(BlocksRegistry.atium_geode.get(), (block) -> {
			return droppingItemWithFortune(block, ItemsRegistry.ATIUM.get());
		});*/

        for (RegistryObject<Block> itemRegistryObject : BlocksRegistry.BLOCKS.getEntries())
        {
            if ((itemRegistryObject.get() instanceof MetalOreBlock))
            {
                MetalOreBlock oreBlock = (MetalOreBlock) itemRegistryObject.get();

                this.add(oreBlock, (ore) -> {
                    return createOreDrop(ore, oreBlock.getMetalType().getRawMetalItem());
                });
            }
            else
            {
                this.dropSelf(itemRegistryObject.get());
            }
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks()
    {
        return BlocksRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
