package leaf.cosmere.sandmastery.common.blocks;

import leaf.cosmere.common.blocks.BaseBlock;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class SandJarBlock extends BaseBlock {
    public SandJarBlock() {
        super(PropTypes.Blocks.SAND.get().noOcclusion(), SoundType.GLASS, 1F, 2F);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(INVESTITURE, 0)
        );
    }

    public static final IntegerProperty INVESTITURE = IntegerProperty.create("investiture", 0, 100);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(INVESTITURE);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos) {
        int investiture = pState.getValue(INVESTITURE);
        int res = Math.round((investiture / 100F) * 15);
        return res;
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState pState) {
        return true;
    }

    @Override
    public void randomTick(@NotNull BlockState pState, ServerLevel pLevel, BlockPos pPos, @NotNull RandomSource pRandom) {
        BlockState state = this.defaultBlockState();
        int investiture = pState.getValue(INVESTITURE);
        if(MiscHelper.checkIfNearbyInvestiture(pLevel, pPos))
            pLevel.setBlockAndUpdate(pPos, state.setValue(INVESTITURE, Math.min(investiture+5, 100)));
        else
            pLevel.setBlockAndUpdate(pPos, pState.setValue(INVESTITURE, Math.max(investiture-1, 0)));
    }

}
