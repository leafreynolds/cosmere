/*
 * File created ~ 26 - 11 - 2026 ~ Leaf
 */

package leaf.cosmere.gametest;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registry.BlocksRegistry;
import leaf.cosmere.common.registry.PoiTypesRegistry;
import leaf.cosmere.common.registry.VillagerProfessionRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.GlobalPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

//villagers should take the metal trader job from a metalworking table
@GameTestHolder(Cosmere.MODID)
@PrefixGameTestTemplate(false)
public class VillagerProfessionGameTests
{
	private static final BlockPos TABLE = new BlockPos(3, 2, 3);
	private static final BlockPos VILLAGER = new BlockPos(1, 2, 1);

	@GameTest(template = HarnessGameTests.FLOOR)
	public static void metalworkingTableRegistersPoi(GameTestHelper helper)
	{
		helper.setBlock(TABLE, BlocksRegistry.METALWORKING_TABLE.getBlock());

		boolean isMetalTraderPoi = helper.getLevel().getPoiManager()
				.getType(helper.absolutePos(TABLE))
				.map(h -> h.is(PoiTypesRegistry.METAL_TRADER_POI.getRegistryObject().getId()))
				.orElse(false);
		helper.assertTrue(isMetalTraderPoi, "No cosmere:metal_trader POI at the Metalworking Table");
		helper.succeed();
	}

	@GameTest(template = HarnessGameTests.FLOOR)
	public static void metalTraderAcceptsTablePoi(GameTestHelper helper)
	{
		helper.setBlock(TABLE, BlocksRegistry.METALWORKING_TABLE.getBlock());

		Holder<PoiType> poi = helper.getLevel().getPoiManager()
				.getType(helper.absolutePos(TABLE))
				.orElseThrow(() -> new GameTestAssertException("No POI at the Metalworking Table"));
		VillagerProfession metalTrader = VillagerProfessionRegistry.METAL_TRADER.get();
		helper.assertTrue(metalTrader.acquirableJobSite().test(poi), "metal_trader acquirableJobSite rejects the table's POI holder " + poi);
		helper.assertTrue(metalTrader.heldJobSite().test(poi), "metal_trader heldJobSite rejects the table's POI holder " + poi);
		helper.succeed();
	}

	@GameTest(template = HarnessGameTests.FLOOR, timeoutTicks = 1200)
	public static void unemployedVillagerBecomesMetalTrader(GameTestHelper helper)
	{
		helper.setBlock(TABLE, BlocksRegistry.METALWORKING_TABLE.getBlock());
		Villager villager = helper.spawn(EntityType.VILLAGER, VILLAGER);
		GlobalPos tablePos = GlobalPos.of(helper.getLevel().dimension(), helper.absolutePos(TABLE));
		//vanilla clears JOB_SITE next tick if no profession matches, so remember the claim
		AtomicBoolean claimedTable = new AtomicBoolean(false);

		helper.succeedWhen(() ->
		{
			VillagerProfession profession = villager.getVillagerData().getProfession();
			if (profession == VillagerProfessionRegistry.METAL_TRADER.get())
			{
				return;
			}

			Optional<GlobalPos> jobSite = villager.getBrain().getMemory(MemoryModuleType.JOB_SITE);
			if (jobSite.isPresent() && jobSite.get().equals(tablePos))
			{
				claimedTable.set(true);
			}
			if (claimedTable.get())
			{
				helper.fail("Villager claimed the Metalworking Table but profession is still " + profession, villager);
			}
			helper.fail("Villager has not claimed the Metalworking Table yet (profession " + profession + ")", villager);
		});
	}
}
