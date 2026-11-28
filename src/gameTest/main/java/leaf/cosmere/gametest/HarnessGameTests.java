/*
 * File created ~ 26 - 11 - 2026 ~ Leaf
 */

package leaf.cosmere.gametest;

import leaf.cosmere.common.Cosmere;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

//proves the game test harness itself works
@GameTestHolder(Cosmere.MODID)
@PrefixGameTestTemplate(false)
public class HarnessGameTests
{
	//relative y=0 is the structure block, so the floor is at y=1
	public static final String FLOOR = "floor_7x4x7";
	//every module's tests share main's copy of the floor
	public static final String FLOOR_NAMESPACE = "cosmere";

	@GameTest(template = FLOOR)
	public static void templateLoadsAndBlocksPlace(GameTestHelper helper)
	{
		helper.assertBlockPresent(Blocks.STONE, new BlockPos(3, 1, 3));

		BlockPos pos = new BlockPos(3, 2, 3);
		helper.setBlock(pos, Blocks.GOLD_BLOCK);
		helper.assertBlockPresent(Blocks.GOLD_BLOCK, pos);
		helper.succeed();
	}
}
