/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class LavisPolypBlock extends HorizontalDirectionalBlock
{
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final VoxelShape SHAPE;

	public LavisPolypBlock()
	{
		super(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).noCollission());
	}

	@Override
	public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext)
	{
		return SHAPE;
	}

	public BlockState getStateForPlacement(BlockPlaceContext pContext)
	{
		return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
	{
		pBuilder.add(FACING);
	}

	static
	{
		SHAPE = Shapes.box(0.25, 0, 0.25, 0.75, 0.375, 0.75);
	}
}
