package leaf.cosmere.sandmastery.common.blocks;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.sandmastery.api.trial.pieces.Checkpoints;
import leaf.cosmere.sandmastery.common.blocks.entities.DiemStoneBE;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.diem.DiemIntroduction;
import leaf.cosmere.sandmastery.common.diem.trial.DiemTrials;
import leaf.cosmere.sandmastery.common.registries.SandmasteryPoiTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DiemStoneBlock extends Block implements EntityBlock
{
	public DiemStoneBlock()
	{
		super(Properties.of().strength(1.5F, 6.0F).sound(SoundType.STONE).noOcclusion());
	}

	private static final VoxelShape SHAPE = Shapes.or(
			Block.box(2, 0, 2, 14, 2, 14),
			Block.box(4, 2, 4, 12, 10, 12),
			Block.box(2, 10, 2, 14, 12, 14),
			Block.box(0, 12, 0, 16, 14, 16),
			Block.box(0, 14, 0, 2, 16, 16),
			Block.box(14, 14, 0, 16, 16, 16),
			Block.box(2, 14, 0, 14, 16, 2),
			Block.box(2, 14, 14, 14, 16, 16));

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new DiemStoneBE(pos, state);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
	{
		if (player instanceof ServerPlayer serverPlayer)
		{
			SpiritwebCapability.get(serverPlayer).ifPresent(spiritweb ->
			{
				if (SandmasterySpiritwebSubmodule.get(spiritweb).getDiemRank() == null)
				{
					DiemIntroduction.onStoneUsed(serverPlayer, pos);
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
