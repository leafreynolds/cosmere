/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.common.properties;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class PropTypes
{
	public static class Blocks
	{
		public static final Supplier<Block.Properties> EXAMPLE = () -> Block.Properties.copy(net.minecraft.world.level.block.Blocks.GLASS).strength(2.0F, 6.0F);
		public static final Supplier<Block.Properties> ORE = () ->
				Block.Properties
						.copy(net.minecraft.world.level.block.Blocks.STONE)
						.strength(2.0F, 6.0F)
						.requiresCorrectToolForDrops();

		public static final Supplier<Block.Properties> METAL = () ->
				Block.Properties
						.copy(net.minecraft.world.level.block.Blocks.IRON_BLOCK)
						.strength(2.0F, 6.0F)
						.requiresCorrectToolForDrops();
		public static final Supplier<BlockBehaviour.Properties> SAND = () ->
				BlockBehaviour.Properties
						.copy(net.minecraft.world.level.block.Blocks.SAND)
						.strength(0.5f);
	}

	public static class Items
	{
		// todo: remove commented out tabs
		public static final Supplier<Item.Properties> SHARDBLADE = () -> new Item.Properties()
				//.tab(CosmereItemGroups.ITEMS)
				.stacksTo(1)
				.fireResistant()
				.rarity(Rarity.EPIC);

		public static final Supplier<Item.Properties> SHARDPLATE = () -> new Item.Properties()
				.stacksTo(1)
				.rarity(Rarity.EPIC)
				.fireResistant();

		public static final Supplier<Item.Properties> ONE = () -> new Item.Properties().stacksTo(1);
		public static final Supplier<Item.Properties> SIXTEEN = () -> new Item.Properties().stacksTo(16);
		public static final Supplier<Item.Properties> SIXTY_FOUR = () -> new Item.Properties().stacksTo(64);
		public static final Supplier<Item.Properties> MISTCLOAK = () -> new Item.Properties()
				.stacksTo(1)
				.rarity(Rarity.UNCOMMON)
				.defaultDurability(500);

	}
}
