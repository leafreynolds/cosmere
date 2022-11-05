/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.blocks;

//import leaf.cosmere.allomancy.common.registries.AllomancyEffects;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import leaf.cosmere.common.blocks.BaseFallingBlock;
import leaf.cosmere.common.properties.PropTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
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

		if(MiscHelper.checkIfNearbyInvestiture(pLevel, pPos)) {
			pLevel.setBlockAndUpdate(pPos, state.setValue(INVESTED, true));
		}

		if(!pLevel.canSeeSky(pPos.above())) return;
		pLevel.setBlockAndUpdate(pPos, state.setValue(INVESTED, true));

		for(int i = 0; i < 2; ++i) {
			BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1);
			if (pLevel.getBlockState(blockpos).is(Blocks.SAND)) {
				pLevel.setBlockAndUpdate(blockpos, state.setValue(INVESTED, true));
			}
		}
	}
}
