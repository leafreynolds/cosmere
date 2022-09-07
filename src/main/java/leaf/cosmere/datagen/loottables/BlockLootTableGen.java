/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.loottables;

import leaf.cosmere.blocks.GemOreBlock;
import leaf.cosmere.blocks.MetalOreBlock;
import leaf.cosmere.registry.BlocksRegistry;
import leaf.cosmere.registry.ItemsRegistry;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class BlockLootTableGen extends BlockLoot
{
	@Override
	protected void addTables()
	{
		/*this.registerLootTable(BlocksRegistry.atium_geode.get(), (block) -> {
			return droppingItemWithFortune(block, ItemsRegistry.ATIUM.get());
		});*/

		for (RegistryObject<Block> itemRegistryObject : BlocksRegistry.BLOCKS.getEntries())
		{
			final Block block = itemRegistryObject.get();
			if (block instanceof MetalOreBlock oreBlock)
			{

				this.add(oreBlock, (ore) ->
				{
					return createOreDrop(ore, oreBlock.getMetalType().getRawMetalItem());
				});
			}
			else if (block instanceof GemOreBlock oreBlock)
			{

				this.add(oreBlock, (ore) ->
				{
					return createOreDrop(ore, ItemsRegistry.POLESTONE_CHIPS.get(oreBlock.getGemType()).get());
				});
			}
			else
			{
				this.dropSelf(block);
			}
		}
	}

	@Override
	protected Iterable<Block> getKnownBlocks()
	{
		return BlocksRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
	}
}
