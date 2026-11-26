package leaf.cosmere.gametest;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registry.GameEventRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(Cosmere.MODID)
@PrefixGameTestTemplate(false)
public class SculkResonanceGameTests
{
	@SuppressWarnings("unchecked")
	@GameTest(template = HarnessGameTests.FLOOR, timeoutTicks = 100)
	public static void sculkOnAmethystHearsKineticInvestiture(GameTestHelper helper)
	{
		Holder<GameEvent> kineticInvestiture = (Holder<GameEvent>) GameEventRegistry.KINETIC_INVESTITURE.getHolder();
		int frequency = VibrationSystem.getGameEventFrequency(kineticInvestiture);
		helper.assertTrue(frequency >= 1 && frequency <= 15, "kinetic investiture needs a vibration frequency from 1 to 15, has " + frequency);

		BlockPos sensor = new BlockPos(3, 3, 3);
		helper.setBlock(sensor.below(), Blocks.AMETHYST_BLOCK);
		helper.setBlock(sensor, Blocks.SCULK_SENSOR);

		//a sensor on amethyst passes on what it hears by frequency
		Vec3 source = Vec3.atCenterOf(helper.absolutePos(new BlockPos(3, 2, 6)));
		helper.getLevel().gameEvent(kineticInvestiture, source, GameEvent.Context.of(Blocks.STONE.defaultBlockState()));
		helper.succeedWhen(() -> helper.assertBlockProperty(sensor, SculkSensorBlock.PHASE, SculkSensorPhase.ACTIVE));
	}
}
