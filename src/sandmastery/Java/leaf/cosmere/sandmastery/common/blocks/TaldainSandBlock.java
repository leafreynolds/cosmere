/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.blocks;

//import leaf.cosmere.allomancy.common.registries.AllomancyEffects;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import leaf.cosmere.common.blocks.BaseFallingBlock;
import leaf.cosmere.common.properties.PropTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class TaldainSandBlock extends BaseFallingBlock
{

	public TaldainSandBlock() {
		super(PropTypes.Blocks.SAND.get(), SoundType.SAND, 1F, 2F);
		this.registerDefaultState(
				this.stateDefinition.any()
						.setValue(INVESTED, false)
		);
	}

	public static final BooleanProperty INVESTED = BooleanProperty.create("invested");

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(INVESTED);
	}

	@Override
	public boolean isRandomlyTicking(BlockState pState) {
		return true;
	}

	@Override
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		if (!pLevel.isAreaLoaded(pPos, 3)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
		BlockState state = this.defaultBlockState();
		boolean nearbyInvestiture = MiscHelper.checkIfNearbyInvestiture(pLevel, pPos);
		boolean offTaldain = !MiscHelper.onTaldain(pLevel);
		boolean canSeeSky = pLevel.canSeeSky(pPos.above());
		if(offTaldain && !nearbyInvestiture) return;
		if (!canSeeSky && !nearbyInvestiture) return; // Can't see the taldanian sky nor can I find any investiture, can't charge from it
		pLevel.setBlockAndUpdate(pPos, state.setValue(INVESTED, true));

		for(int i = 0; i < 4; ++i) {
			BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1);
			if (pLevel.getBlockState(blockpos).is(Blocks.SAND)) {
				pLevel.setBlockAndUpdate(blockpos, state.setValue(INVESTED, true));
			}
		}
	}

	private static boolean touchesLiquid(BlockGetter pLevel, BlockPos pPos, BlockState state) {
		boolean touching = false;
		BlockPos.MutableBlockPos blockpos$mutableblockpos = pPos.mutable();
		for(Direction direction : Direction.values()) {
			BlockState blockstate = pLevel.getBlockState(blockpos$mutableblockpos);
			if (direction != Direction.DOWN || state.canBeHydrated(pLevel, pPos, blockstate.getFluidState(), blockpos$mutableblockpos)) {
				blockpos$mutableblockpos.setWithOffset(pPos, direction);
				blockstate = pLevel.getBlockState(blockpos$mutableblockpos);
				if (state.canBeHydrated(pLevel, pPos, blockstate.getFluidState(), blockpos$mutableblockpos) && !blockstate.isFaceSturdy(pLevel, pPos, direction.getOpposite())) {
					touching = true;
					break;
				}
			}
		}
		return touching;
	}

	@Override
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
		return touchesLiquid(pLevel, pCurrentPos, pState) ?
				this.defaultBlockState() :
				super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
	}
}
