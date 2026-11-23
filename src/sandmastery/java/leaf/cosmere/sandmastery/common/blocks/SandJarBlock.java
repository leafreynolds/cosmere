package leaf.cosmere.sandmastery.common.blocks;

import com.mojang.serialization.MapCodec;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.sandmastery.common.blocks.entities.SandJarBE;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlockEntitiesRegistry;
import leaf.cosmere.sandmastery.common.utils.SandmasteryConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SandJarBlock extends BaseEntityBlock
{
	public static final MapCodec<SandJarBlock> CODEC = MapCodec.unit(SandJarBlock::new);

	public SandJarBlock()
	{
		super(PropTypes.Blocks.SAND.get().noOcclusion().lightLevel(state -> state.getValue(GLOWING) ? SandmasteryConstants.GLOWING_SAND_LIGHT_LEVEL : 0));
		this.registerDefaultState(
				this.stateDefinition.any()
						.setValue(INVESTITURE, 0)
						.setValue(GLOWING, false)
		);
	}

	public static final IntegerProperty INVESTITURE = IntegerProperty.create("investiture", 0, 100);
	public static final BooleanProperty GLOWING = BooleanProperty.create("glowing");

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
	{
		pBuilder.add(INVESTITURE, GLOWING);
	}

	private static final VoxelShape SHAPE = Shapes.or(
			Block.box(3, 0, 3, 13, 13, 13),
			Block.box(5, 12, 5, 11, 14, 11));

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState pState)
	{
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos)
	{
		if (pState.getValue(GLOWING))
		{
			return 0;
		}
		int investiture = pState.getValue(INVESTITURE);
		int res = Math.round((investiture / 100F) * 15);
		return res;
	}

	@Override
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.MODEL;
	}


	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new SandJarBE(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, SandmasteryBlockEntitiesRegistry.SAND_JAR_BE.get(), SandJarBE::tick);
	}


	@Override
	protected MapCodec<? extends BaseEntityBlock> codec()
	{
		return CODEC;
	}
}
