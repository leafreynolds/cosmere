package leaf.cosmere.sandmastery.api.trial.pieces;

import leaf.cosmere.sandmastery.api.trial.ITrialContext;
import leaf.cosmere.sandmastery.api.trial.TrialPiece;
import leaf.cosmere.sandmastery.common.blocks.entities.DiemCheckpointBE;
import leaf.cosmere.sandmastery.common.registries.SandmasteryPoiTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Places the player must reach: rings to fly through, a beacon to reach, or the checkpoint blocks of a course.
 *
 * @param fromBlocks    use the checkpoint blocks built near the stone instead of {@code offsets}
 * @param offsets       checkpoint positions, measured from the centre of the stone
 * @param radius        how close the player's body must get to an offset checkpoint
 * @param ordered       checkpoints must be reached in list order
 * @param look          how the checkpoints are drawn
 * @param parMultiplier block checkpoints only: the time limit is the sum of the blocks' par times times this
 */
public record Checkpoints(boolean fromBlocks, List<Vec3> offsets, double radius, boolean ordered, Look look, double parMultiplier) implements TrialPiece
{
	// how far from the stone a course's checkpoint blocks may be
	public static final int COURSE_SEARCH_RADIUS = 128;
	// how close the player's body must get to a checkpoint block
	private static final double BLOCK_RADIUS = 1.0;
	private static final DustParticleOptions LATER_CHECKPOINT = new DustParticleOptions(new Vector3f(1.0F, 0.75F, 0.2F), 1.0F);

	public enum Look
	{
		RING, BEACON, PAD
	}

	public Checkpoints
	{
		offsets = List.copyOf(offsets);
	}

	public static Checkpoints rings(double radius, Vec3... offsets)
	{
		return new Checkpoints(false, List.of(offsets), radius, true, Look.RING, 0);
	}

	public static Checkpoints beacon(Vec3 offset)
	{
		return new Checkpoints(false, List.of(offset), 1.5, true, Look.BEACON, 0);
	}

	public static Checkpoints course(double parMultiplier)
	{
		return new Checkpoints(true, List.of(), BLOCK_RADIUS, false, Look.PAD, parMultiplier);
	}

	public Checkpoints withOffset(Vec3 offset)
	{
		List<Vec3> more = new ArrayList<>(offsets);
		more.add(offset);
		return new Checkpoints(fromBlocks, more, radius, ordered, look, parMultiplier);
	}

	public Checkpoints withoutLastOffset()
	{
		List<Vec3> fewer = new ArrayList<>(offsets);
		if (!fewer.isEmpty())
		{
			fewer.removeLast();
		}
		return new Checkpoints(fromBlocks, fewer, radius, ordered, look, parMultiplier);
	}

	public Checkpoints withRadius(double newRadius)
	{
		return new Checkpoints(fromBlocks, offsets, newRadius, ordered, look, parMultiplier);
	}

	public Checkpoints withOrdered(boolean newOrdered)
	{
		return new Checkpoints(fromBlocks, offsets, radius, newOrdered, look, parMultiplier);
	}

	public Checkpoints withLook(Look newLook)
	{
		return new Checkpoints(fromBlocks, offsets, radius, ordered, newLook, parMultiplier);
	}

	// where the checkpoints are in the world
	public List<Vec3> points(ServerLevel level, BlockPos stone)
	{
		if (fromBlocks)
		{
			return findCheckpointBlocks(level, stone).stream().map(Vec3::atCenterOf).toList();
		}
		Vec3 centre = Vec3.atCenterOf(stone);
		return offsets.stream().map(centre::add).toList();
	}

	public static List<BlockPos> findCheckpointBlocks(ServerLevel level, BlockPos stone)
	{
		return level.getPoiManager()
				.findAll(poi -> poi.is(SandmasteryPoiTypes.DIEM_CHECKPOINT.getKey()), pos -> true, stone, COURSE_SEARCH_RADIUS, PoiManager.Occupancy.ANY)
				.toList();
	}

	// time a course adds to the trial: every checkpoint block's par time added up, times parMultiplier
	public double courseSeconds(ServerLevel level, BlockPos stone)
	{
		if (!fromBlocks)
		{
			return 0;
		}
		double parSeconds = 0;
		for (BlockPos pos : findCheckpointBlocks(level, stone))
		{
			if (level.getBlockEntity(pos) instanceof DiemCheckpointBE checkpoint)
			{
				parSeconds += checkpoint.getPar();
			}
		}
		return parSeconds * parMultiplier;
	}

	@Override
	public Component unavailableReason(ServerLevel level, BlockPos stone)
	{
		if (fromBlocks && findCheckpointBlocks(level, stone).isEmpty())
		{
			return Component.translatable("diem.sandmastery.trial.unavailable.no_course");
		}
		return null;
	}

	@Override
	public TrialPiece.Instance start(ITrialContext context)
	{
		return new Instance(context);
	}

	@Override
	public String toJava()
	{
		if (fromBlocks)
		{
			return String.format(Locale.ROOT, "Checkpoints.course(%.2f)", parMultiplier);
		}
		String points = offsets.stream()
				.map(o -> String.format(Locale.ROOT, "new Vec3(%.1f, %.1f, %.1f)", o.x, o.y, o.z))
				.collect(Collectors.joining(", "));
		return String.format(Locale.ROOT, "new Checkpoints(false, List.of(%s), %.1f, %b, Checkpoints.Look.%s, 0)", points, radius, ordered, look.name());
	}

	// draws one checkpoint; the next one to reach is bright, the rest are gold
	public static void draw(BiConsumer<ParticleOptions, Vec3> particle, Vec3 point, Vec3 stoneCentre, Look look, double radius, boolean isNext)
	{
		ParticleOptions type = isNext ? ParticleTypes.END_ROD : LATER_CHECKPOINT;
		switch (look)
		{
			case RING ->
			{
				// an upright ring facing the stone
				Vec3 facing = new Vec3(point.x - stoneCentre.x, 0, point.z - stoneCentre.z);
				Vec3 across = facing.lengthSqr() < 0.01 ? new Vec3(1, 0, 0) : new Vec3(-facing.z, 0, facing.x).normalize();
				for (int i = 0; i < 16; i++)
				{
					double angle = 2 * Math.PI * i / 16;
					particle.accept(type, point.add(across.scale(Math.cos(angle) * radius)).add(0, Math.sin(angle) * radius, 0));
				}
			}
			case BEACON ->
			{
				for (int i = -4; i <= 4; i++)
				{
					particle.accept(type, point.add(0, i * 0.5, 0));
				}
			}
			case PAD ->
			{
				for (int i = 0; i < 8; i++)
				{
					double angle = 2 * Math.PI * i / 8;
					particle.accept(type, point.add(Math.cos(angle) * 0.5, -0.4, Math.sin(angle) * 0.5));
				}
			}
		}
	}

	public class Instance implements TrialPiece.Instance
	{
		private final ITrialContext context;
		private final List<Vec3> remaining;
		private final int total;
		private int extraTimeTicks = 0;

		Instance(ITrialContext context)
		{
			this.context = context;
			this.remaining = new ArrayList<>(points(context.level(), context.stone()));
			this.total = remaining.size();

			for (Vec3 point : remaining)
			{
				context.extendLeash(point.distanceTo(context.stoneCentre()) + 32);
			}

			if (fromBlocks)
			{
				extraTimeTicks = (int) Math.round(courseSeconds(context.level(), context.stone()) * 20);
			}

			updateBar();
		}

		@Override
		public void tick()
		{
			Vec3 body = context.player().getBoundingBox().getCenter();
			// ordered checkpoints only count the next one; unordered count any of them
			List<Vec3> reachable = ordered && !remaining.isEmpty() ? List.of(remaining.getFirst()) : List.copyOf(remaining);
			for (Vec3 point : reachable)
			{
				if (body.distanceTo(point) <= radius)
				{
					remaining.remove(point);
					context.level().playSound(null, context.player().blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.6F, 1.2F);
					updateBar();
				}
			}

			if (context.ticksElapsed() % 5 == 0)
			{
				for (int i = 0; i < remaining.size(); i++)
				{
					boolean isNext = !ordered || i == 0;
					draw(context::particle, remaining.get(i), context.stoneCentre(), look, radius, isNext);
				}
			}
		}

		private void updateBar()
		{
			context.showCheckpoints(remaining, ordered ? 0 : -1);
		}

		@Override
		public boolean isComplete()
		{
			return remaining.isEmpty();
		}

		@Override
		public int extraTimeTicks()
		{
			return extraTimeTicks;
		}

		@Override
		public Component progress()
		{
			return Component.translatable("diem.sandmastery.trial.progress.checkpoints", total - remaining.size(), total);
		}
	}
}
