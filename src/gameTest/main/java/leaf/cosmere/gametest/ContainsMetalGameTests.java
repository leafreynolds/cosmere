/*
 * File updated ~ 26 - 11 - 2026 ~ Leaf
 */

/*
 * File created ~ 26 - 11 - 2026 ~ Leaf
 */

package leaf.cosmere.gametest;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.common.Cosmere;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.List;

@GameTestHolder(Cosmere.MODID)
@PrefixGameTestTemplate(false)
public class ContainsMetalGameTests
{
	@GameTest(template = HarnessGameTests.FLOOR)
	public static void uncraftableMetalItemsContainMetal(GameTestHelper helper)
	{
		List<Item> items = List.of(
				Items.CHAINMAIL_CHESTPLATE,
				Items.IRON_HORSE_ARMOR,
				Items.BELL,
				Items.DAMAGED_ANVIL,
				Items.TRIAL_KEY,
				Items.WATER_BUCKET,
				Items.COD_BUCKET,
				Items.IRON_ORE,
				Items.NETHERITE_INGOT);

		//things that should contain metal
		for (Item item : items)
		{
			helper.assertTrue(new ItemStack(item).is(CosmereTags.Items.CONTAINS_METAL), BuiltInRegistries.ITEM.getKey(item) + " item should contain metal");
		}

		//things that don't contain metal
		for (Item item : List.of(Items.ANCIENT_DEBRIS, Items.NETHERITE_SCRAP, Items.HEAVY_CORE, Items.MACE))
		{
			helper.assertFalse(new ItemStack(item).is(CosmereTags.Items.CONTAINS_METAL), BuiltInRegistries.ITEM.getKey(item) + " item should not contain metal");
		}

		List<Block> blocks = List.of(Blocks.BELL, Blocks.WATER_CAULDRON);
		for (Block block : blocks)
		{
			helper.assertTrue(block.defaultBlockState().is(CosmereTags.Blocks.CONTAINS_METAL), BuiltInRegistries.BLOCK.getKey(block) + " block should contain metal");
		}

		//same check iron and steel use, entities without the entity tag fall back to their pick item
		for (EntityType<?> type : List.of(EntityType.SPAWNER_MINECART, EntityType.COMMAND_BLOCK_MINECART))
		{
			Entity cart = type.create(helper.getLevel());
			boolean containsMetal = cart != null
					&& (type.is(CosmereTags.EntityTypes.CONTAINS_METAL) || cart.getPickResult().is(CosmereTags.Items.CONTAINS_METAL));
			helper.assertTrue(containsMetal, EntityType.getKey(type) + " should contain metal");
		}
		helper.succeed();
	}
}
