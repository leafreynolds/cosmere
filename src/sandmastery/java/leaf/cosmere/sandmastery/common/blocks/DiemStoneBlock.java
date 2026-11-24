package leaf.cosmere.sandmastery.common.blocks;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.sandmastery.api.trial.pieces.Checkpoints;
import leaf.cosmere.sandmastery.common.registries.SandmasteryPoiTypes;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.diem.DiemProgression;
import leaf.cosmere.sandmastery.common.diem.trial.DiemTrials;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

// shortcut: placeholder for the Diem itself until the structure exists.
public class DiemStoneBlock extends Block
{
	public DiemStoneBlock()
	{
		super(Properties.of().strength(1.5F, 6.0F).sound(SoundType.STONE));
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
	{
		if (level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer)
		{
			SpiritwebCapability.get(serverPlayer).ifPresent(spiritweb ->
			{
				if (SandmasterySpiritwebSubmodule.get(spiritweb).getDiemRank() == null)
				{
					DiemProgression.detect(serverPlayer, spiritweb);
					serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.GOLD_BLOCK.defaultBlockState()),
							pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 40, 0.4, 0.4, 0.4, 0.05);
					serverLevel.playSound(null, pos, SoundEvents.SAND_BREAK, SoundSource.BLOCKS, 1.0F, 0.8F);
				}
				else
				{
					DiemTrials.openTrialScreen(serverPlayer, spiritweb, pos);
				}
			});
		}
		return InteractionResult.sidedSuccess(level.isClientSide());
	}

	// a course belongs to its stone: when the stone goes, so do its checkpoints
	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
	{
		if (level instanceof ServerLevel serverLevel && !newState.is(this))
		{
			removeCheckpointsOf(serverLevel, pos);
		}
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	private static void removeCheckpointsOf(ServerLevel level, BlockPos removedStone)
	{
		for (BlockPos checkpoint : Checkpoints.findCheckpointBlocks(level, removedStone))
		{
			// keep checkpoints another stone's course still uses
			boolean usedByAnotherStone = level.getPoiManager()
					.findAll(poi -> poi.is(SandmasteryPoiTypes.DIEM_STONE.getKey()), stone -> !stone.equals(removedStone), checkpoint, Checkpoints.COURSE_SEARCH_RADIUS, PoiManager.Occupancy.ANY)
					.findAny()
					.isPresent();
			if (!usedByAnotherStone)
			{
				level.removeBlock(checkpoint, false);
			}
		}
	}
}
