package leaf.cosmere.sandmastery.api.trial.pieces;

import leaf.cosmere.sandmastery.api.trial.ITrialContext;
import leaf.cosmere.sandmastery.api.trial.TrialPiece;
import leaf.cosmere.sandmastery.common.entities.DiemTargetEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

import java.util.Locale;

/**
 * Spawns targets in a circle around the stone. Hit {@code count} of them with no more than {@code maxShots} shots.
 * Hitting a decoy fails the trial.
 *
 * @param count    real targets that must be hit
 * @param spawned  real targets spawned (more than count gives spares)
 * @param size     target size in blocks
 * @param radius   distance from the stone
 * @param maxShots shots allowed
 * @param moving   targets circle the stone and bob
 * @param decoys   decoy targets spawned between the real ones
 */
public record Targets(int count, int spawned, float size, double radius, int maxShots, boolean moving, int decoys) implements TrialPiece
{
	// how long to wait for the last shot to land before calling it a miss
	private static final int LAST_SHOT_GRACE_TICKS = 60;

	@Override
	public TrialPiece.Instance start(ITrialContext context)
	{
		int total = spawned + decoys;
		int realLeft = spawned;
		int decoysLeft = decoys;
		for (int i = 0; i < total; i++)
		{
			// spread evenly around the stone, at a few different heights
			double angle = 2 * Math.PI * i / total;
			double height = 2 + (i % 3) * 2;
			Vec3 pos = context.stoneCentre().add(Math.cos(angle) * radius, height, Math.sin(angle) * radius);

			// every other target is a decoy while decoys are left, so they sit between real targets
			boolean decoy = realLeft == 0 || (i % 2 == 1 && decoysLeft > 0);
			if (decoy)
			{
				decoysLeft--;
			}
			else
			{
				realLeft--;
			}
			context.spawnTarget(pos, size, decoy, moving);
		}
		return new Instance();
	}

	@Override
	public String toJava()
	{
		return String.format(Locale.ROOT, "new Targets(%d, %d, %.2fF, %.1f, %d, %b, %d)", count, spawned, size, radius, maxShots, moving, decoys);
	}

	public class Instance implements TrialPiece.Instance
	{
		private int hits = 0;
		private int shots = 0;
		private int ticksSinceLastShot = 0;
		private boolean hitDecoy = false;

		public int misses()
		{
			return shots - hits;
		}

		@Override
		public void tick()
		{
			ticksSinceLastShot++;
		}

		@Override
		public void onShot()
		{
			shots++;
			ticksSinceLastShot = 0;
		}

		@Override
		public void onTargetHit(DiemTargetEntity target)
		{
			if (target.isDecoy())
			{
				hitDecoy = true;
			}
			else
			{
				hits++;
			}
		}

		@Override
		public boolean isComplete()
		{
			return hits >= count;
		}

		@Override
		public Component failReason()
		{
			if (hitDecoy)
			{
				return Component.translatable("diem.sandmastery.trial.fail.decoy");
			}
			boolean outOfShots = shots >= maxShots && ticksSinceLastShot > LAST_SHOT_GRACE_TICKS;
			if (outOfShots && hits < count)
			{
				return Component.translatable("diem.sandmastery.trial.fail.out_of_shots");
			}
			return null;
		}

		@Override
		public Component progress()
		{
			return Component.translatable("diem.sandmastery.trial.progress.targets", hits, count, maxShots - shots);
		}
	}
}
