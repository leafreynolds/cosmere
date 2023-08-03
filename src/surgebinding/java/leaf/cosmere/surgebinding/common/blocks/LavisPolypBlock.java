package leaf.cosmere.surgebinding.common.blocks;

import leaf.cosmere.common.blocks.BaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class LavisPolypBlock extends BaseBlock {
    public static final VoxelShape SHAPE;

    public LavisPolypBlock () {
        super(BlockBehaviour.Properties.of(Material.GRASS).color(MaterialColor.COLOR_GREEN).noCollission());
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE;
    }

    static {
        SHAPE = Shapes.box(0.25, 0, 0.25, 0.75, 0.375, 0.75);
    }
}
