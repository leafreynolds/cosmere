package leaf.cosmere.sandmastery.common.blocks;

import com.mojang.serialization.MapCodec;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.sandmastery.common.blocks.entities.SandSpreaderBE;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlockEntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class SandSpreadingTubBlock extends BaseEntityBlock
{
	public static final MapCodec<SandSpreadingTubBlock> CODEC = MapCodec.unit(SandSpreadingTubBlock::new);

	public SandSpreadingTubBlock()
	{
		super(PropTypes.Blocks.METAL.get().noOcclusion());
	}

	@Override
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.MODEL;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (state.getBlock() != newState.getBlock())
		{
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof SandSpreaderBE)
			{
				((SandSpreaderBE) blockEntity).drops();
			}
		}
		super.onRemove(state, level, pos, state, isMoving);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
	                                           Player player, BlockHitResult hit)
	{
		if (!level.isClientSide())
		{
			BlockEntity entity = level.getBlockEntity(pos);
			if (entity instanceof SandSpreaderBE)
			{
				((ServerPlayer) player).openMenu((SandSpreaderBE) entity, pos);
			}
			else
			{
				throw new IllegalStateException("Our Container provider is missing!");
			}
		}

		return InteractionResult.sidedSuccess(level.isClientSide());
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new SandSpreaderBE(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, SandmasteryBlockEntitiesRegistry.SAND_SPREADER_BE.get(), SandSpreaderBE::tick);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec()
	{
		return CODEC;
	}
}
