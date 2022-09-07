/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.properties;

import leaf.cosmere.itemgroups.CosmereItemGroups;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class PropTypes
{
	public static class Blocks
	{
		public static final Supplier<Block.Properties> EXAMPLE = () -> Block.Properties.of(Material.GLASS).strength(2.0F, 6.0F);
		public static final Supplier<Block.Properties> ORE = () ->
				Block.Properties
						.of(Material.STONE)
						.strength(2.0F, 6.0F)
						.requiresCorrectToolForDrops();

		public static final Supplier<Block.Properties> METAL = () ->
				Block.Properties
						.of(Material.METAL)
						.strength(2.0F, 6.0F)
						.requiresCorrectToolForDrops();
	}

	public static class Items
	{
		public static final Supplier<Item.Properties> SHARDBLADE = () -> new Item.Properties()
				.tab(CosmereItemGroups.ITEMS)
				.stacksTo(1)
				.fireResistant()
				.rarity(Rarity.EPIC);

		public static final Supplier<Item.Properties> ONE = () -> new Item.Properties().tab(CosmereItemGroups.ITEMS).stacksTo(1);
		public static final Supplier<Item.Properties> SIXTEEN = () -> new Item.Properties().tab(CosmereItemGroups.ITEMS).stacksTo(16);
		public static final Supplier<Item.Properties> SIXTY_FOUR = () -> new Item.Properties().tab(CosmereItemGroups.ITEMS).stacksTo(64);
	}
}
