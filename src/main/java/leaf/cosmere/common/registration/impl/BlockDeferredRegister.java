/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.common.registration.impl;

import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.common.registration.DoubleDeferredRegister;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockDeferredRegister extends DoubleDeferredRegister<Block, Item>
{

	private final List<IBlockProvider> allBlocks = new ArrayList<>();

	public BlockDeferredRegister(String modid)
	{
		super(modid, ForgeRegistries.BLOCKS, ForgeRegistries.ITEMS);
	}

	//Normal block registration
	public <BLOCK extends Block> BlockRegistryObject<BLOCK, BlockItem> register(String name, Supplier<? extends BLOCK> blockSupplier)
	{
		return registerDefaultProperties(name, blockSupplier, BlockItem::new, Rarity.COMMON);
	}

	//register blocks with rarity
	public <BLOCK extends Block> BlockRegistryObject<BLOCK, BlockItem> registerWithRarity(String name, Supplier<? extends BLOCK> blockSupplier, Rarity rarity)
	{
		//don't confuse BlockItem with block,
		return registerDefaultProperties(name, blockSupplier, BlockItem::new, rarity);
	}

	public <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerDefaultProperties(String name, Supplier<? extends BLOCK> blockSupplier,
	                                                                                                                BiFunction<BLOCK, Item.Properties, ITEM> itemCreator, Rarity rarity)
	{
		return register(name, blockSupplier, block -> itemCreator.apply(block, new Item.Properties().rarity(rarity)));
	}

	public <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> register(String name, Supplier<? extends BLOCK> blockSupplier,
	                                                                                               Function<BLOCK, ITEM> itemCreator)
	{
		//registers a block item to go with the block
		BlockRegistryObject<BLOCK, ITEM> registeredBlock = register(name, blockSupplier, itemCreator, BlockRegistryObject::new);
		allBlocks.add(registeredBlock);
		return registeredBlock;
	}

	public List<IBlockProvider> getAllBlocks()
	{
		return Collections.unmodifiableList(allBlocks);
	}
}