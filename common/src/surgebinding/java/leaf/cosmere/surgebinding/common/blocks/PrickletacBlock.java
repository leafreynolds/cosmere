package leaf.cosmere.surgebinding.common.blocks;

import leaf.cosmere.common.blocks.BaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PrickletacBlock extends BaseBlock {
    public static final IntegerProperty STATE;
    public static final VoxelShape SHAPE;

    public PrickletacBlock() {
        super(Properties.of(Material.GRASS).color(MaterialColor.COLOR_GREEN).noCollission());
        this.registerDefaultState(this.defaultBlockState().setValue(STATE, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(STATE);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if (pLevel.isClientSide) return InteractionResult.SUCCESS;

        pLevel.setBlockAndUpdate(pPos, pState.cycle(STATE));

        return InteractionResult.SUCCESS;
    }

    static {
        STATE = IntegerProperty.create("state", 1, 3);
        SHAPE = Shapes.box(0.25, 0, 0.375, 0.5625, 0.6875, 0.6875);
    }
}
