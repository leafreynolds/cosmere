package leaf.cosmere.sandmastery.common.blocks;

import leaf.cosmere.common.blocks.BaseFallingBlock;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.sandmastery.common.blocks.entities.GlowingSandBE;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlockEntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TaldainGlowingSandBlock extends BaseFallingBlock implements EntityBlock
{
	public static final int LIGHT_LEVEL = 10;

	public TaldainGlowingSandBlock()
	{
		super(PropTypes.Blocks.SAND.get().lightLevel(state -> LIGHT_LEVEL), SoundType.SAND, 1F, 2F);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new GlowingSandBE(pos, state);
	}

	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		if (level.isClientSide || type != SandmasteryBlockEntitiesRegistry.GLOWING_SAND_BE.get())
		{
			return null;
		}
		return (BlockEntityTicker<T>) (BlockEntityTicker<GlowingSandBE>) GlowingSandBE::tick;
	}
}
