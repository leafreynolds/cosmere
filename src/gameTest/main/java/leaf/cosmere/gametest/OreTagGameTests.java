/*
 * File created ~ 26 - 11 - 2026 ~ Leaf
 */

package leaf.cosmere.gametest;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registry.BlocksRegistry;
import leaf.cosmere.common.resource.ore.OreBlockType;
import leaf.cosmere.common.resource.ore.OreType;
import leaf.cosmere.common.util.CosmereEnumUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.List;

//our ores need the common ore tags so other mods treat them like any other ore
@GameTestHolder(Cosmere.MODID)
@PrefixGameTestTemplate(false)
public class OreTagGameTests
{
	@GameTest(template = HarnessGameTests.FLOOR)
	public static void metalOresHaveCommonTags(GameTestHelper helper)
	{
		for (OreType oreType : CosmereEnumUtils.ORE_TYPES)
		{
			Metals.MetalType metal = oreType.getMetalType();
			OreBlockType ore = BlocksRegistry.METAL_ORE.get(oreType);
			TagKey<Block> oreBlockTag = CosmereTags.Blocks.METAL_ORE_BLOCK_TAGS.get(metal);
			TagKey<Item> oreItemTag = CosmereTags.Items.METAL_ORE_TAGS.get(metal);

			assertOreTags(helper, ore.stone().getBlock(), oreBlockTag, oreItemTag, Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE);
			assertOreTags(helper, ore.deepslate().getBlock(), oreBlockTag, oreItemTag, Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE);
		}
		helper.succeed();
	}

	//checks block and item side, the gem ore test in surgebinding uses this too
	public static void assertOreTags(GameTestHelper helper, Block block, TagKey<Block> oreBlockTag, TagKey<Item> oreItemTag, TagKey<Block> groundBlockTag, TagKey<Item> groundItemTag)
	{
		String name = BuiltInRegistries.BLOCK.getKey(block).toString();

		for (TagKey<Block> tag : List.of(Tags.Blocks.ORES, Tags.Blocks.ORE_RATES_SINGULAR, oreBlockTag, groundBlockTag))
		{
			helper.assertTrue(block.defaultBlockState().is(tag), name + " block is missing #" + tag.location());
		}

		ItemStack stack = new ItemStack(block);
		for (TagKey<Item> tag : List.of(Tags.Items.ORES, Tags.Items.ORE_RATES_SINGULAR, oreItemTag, groundItemTag))
		{
			helper.assertTrue(stack.is(tag), name + " item is missing #" + tag.location());
		}
	}
}
