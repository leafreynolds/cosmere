/*
 * File created ~ 26 - 11 - 2026 ~ Leaf
 */

package leaf.cosmere.surgebinding.gametest;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.gametest.HarnessGameTests;
import leaf.cosmere.gametest.OreTagGameTests;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.registries.SurgebindingBlocks;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

//gem ores need the same common ore tags as the metal ores
@GameTestHolder(Surgebinding.MODID)
@PrefixGameTestTemplate(false)
public class GemOreTagGameTests
{
	@GameTest(template = HarnessGameTests.FLOOR, templateNamespace = HarnessGameTests.FLOOR_NAMESPACE)
	public static void gemOresHaveCommonTags(GameTestHelper helper)
	{
		for (Roshar.Gemstone gem : EnumUtils.GEMSTONE_TYPES_ORE)
		{
			TagKey<Block> oreBlockTag = CosmereTags.Blocks.GEM_ORE_BLOCK_TAGS.get(gem);
			TagKey<Item> oreItemTag = CosmereTags.Items.GEM_ORE_ITEM_TAGS.get(gem);

			OreTagGameTests.assertOreTags(helper, SurgebindingBlocks.GEM_ORE.get(gem).getBlock(), oreBlockTag, oreItemTag, Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE);
			OreTagGameTests.assertOreTags(helper, SurgebindingBlocks.GEM_ORE_DEEPSLATE.get(gem).getBlock(), oreBlockTag, oreItemTag, Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE);
		}
		helper.succeed();
	}
}
