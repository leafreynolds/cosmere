package leaf.cosmere.sandmastery.api.trial.pieces;

import leaf.cosmere.sandmastery.api.trial.ITrialContext;
import leaf.cosmere.sandmastery.api.trial.TrialPiece;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Marked squares in the air. Platform sand placed anywhere outside them fails the trial.
 *
 * @param offsets tile centres, measured in blocks from the stone
 * @param size    width of each square tile in blocks
 */
public record Tiles(List<BlockPos> offsets, int size) implements TrialPiece
{
	private static final DustParticleOptions TILE_EDGE = new DustParticleOptions(new Vector3f(0.4F, 0.8F, 1.0F), 1.0F);

	public Tiles
	{
		offsets = List.copyOf(offsets);
	}

	public Tiles withTile(BlockPos offset)
	{
		List<BlockPos> more = new ArrayList<>(offsets);
		more.add(offset);
		return new Tiles(more, size);
	}

	public Tiles withSize(int newSize)
	{
		return new Tiles(offsets, newSize);
	}

	public Tiles withoutLastTile()
	{
		List<BlockPos> fewer = new ArrayList<>(offsets);
		if (!fewer.isEmpty())
		{
			fewer.removeLast();
		}
		return new Tiles(fewer, size);
	}

	public boolean isOnATile(BlockPos stone, BlockPos placed)
	{
		int half = size / 2;
		for (BlockPos offset : offsets)
		{
			BlockPos tile = stone.offset(offset);
			// crouching places Platform sand one block lower, so allow one block of height either way
			boolean inside = Math.abs(placed.getX() - tile.getX()) <= half
					&& Math.abs(placed.getZ() - tile.getZ()) <= half
					&& Math.abs(placed.getY() - tile.getY()) <= 1;
			if (inside)
			{
				return true;
			}
		}
		return false;
	}

	// draws the outline of every tile
	public void draw(BiConsumer<ParticleOptions, Vec3> particle, BlockPos stone)
	{
		double half = size / 2 + 0.5;
		for (BlockPos offset : offsets)
		{
			Vec3 centre = Vec3.atCenterOf(stone.offset(offset)).add(0, 0.55, 0);
			for (double step = -half; step <= half; step += 0.5)
			{
				particle.accept(TILE_EDGE, centre.add(step, 0, -half));
				particle.accept(TILE_EDGE, centre.add(step, 0, half));
				particle.accept(TILE_EDGE, centre.add(-half, 0, step));
				particle.accept(TILE_EDGE, centre.add(half, 0, step));
			}
		}
	}

	@Override
	public TrialPiece.Instance start(ITrialContext context)
	{
		return new Instance(context);
	}

	@Override
	public String toJava()
	{
		String tiles = offsets.stream()
				.map(o -> String.format(Locale.ROOT, "new BlockPos(%d, %d, %d)", o.getX(), o.getY(), o.getZ()))
				.collect(Collectors.joining(", "));
		return "new Tiles(List.of(" + tiles + "), " + size + ")";
	}

	private class Instance implements TrialPiece.Instance
	{
		private final ITrialContext context;
		private boolean placedOffTile = false;

		Instance(ITrialContext context)
		{
			this.context = context;
		}

		@Override
		public void tick()
		{
			if (context.ticksElapsed() % 10 == 0)
			{
				draw(context::particle, context.stone());
			}
		}

		@Override
		public void onSandPlaced(BlockPos pos)
		{
			if (!isOnATile(context.stone(), pos))
			{
				placedOffTile = true;
			}
		}

		@Override
		public Component failReason()
		{
			return placedOffTile ? Component.translatable("diem.sandmastery.trial.fail.off_tile") : null;
		}
	}
}
