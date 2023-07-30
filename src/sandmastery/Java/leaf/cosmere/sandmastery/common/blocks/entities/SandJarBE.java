package leaf.cosmere.sandmastery.common.blocks.entities;

import leaf.cosmere.sandmastery.common.registries.SandmasteryBlockEntitiesRegistry;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static leaf.cosmere.sandmastery.common.blocks.SandJarBlock.INVESTITURE;

public class SandJarBE extends BlockEntity
{
    public SandJarBE(BlockPos pPos, BlockState pBlockState)
    {
        super(SandmasteryBlockEntitiesRegistry.SAND_JAR_BE.get(), pPos, pBlockState);
    }

    private int ticksSinceUpdate = 0;

    @Override
    protected void saveAdditional(CompoundTag nbt)
    {
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SandJarBE entity)
    {
        if (level.isClientSide()) return;
        entity.ticksSinceUpdate++;
        if (entity.ticksSinceUpdate < 4) return;
        int investiture = state.getValue(INVESTITURE);
        if (MiscHelper.checkIfNearbyInvestiture((ServerLevel) level, pos, true))
            level.setBlockAndUpdate(pos, state.setValue(INVESTITURE, Math.min(investiture + 1, 100)));
        else
            level.setBlockAndUpdate(pos, state.setValue(INVESTITURE, Math.max(investiture - 4, 0)));
        entity.ticksSinceUpdate = 0;
    }
}
