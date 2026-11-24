package leaf.cosmere.sandmastery.common.blocks;

import leaf.cosmere.sandmastery.common.blocks.entities.DiemCheckpointBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

// a course checkpoint for the Diem's Course trial. Placed by structures or in creative; it can't be broken or walked on.
// Like vanilla's light block it's invisible, and can only be seen and targeted while holding one (see DiemCheckpointRenderer).
public class DiemCheckpointBlock extends Block implements EntityBlock
{
	private static final VoxelShape PAD = Block.box(0, 0, 0, 16, 1, 16);

	public DiemCheckpointBlock()
	{
		super(Properties.of()
				.noCollission()
				.noOcclusion()
				.strength(-1.0F, 3600000.0F)
				.sound(SoundType.STONE));
	}

	@Override
	protected RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.INVISIBLE;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return context.isHoldingItem(asItem()) ? PAD : Shapes.empty();
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new DiemCheckpointBE(pos, state);
	}
}
