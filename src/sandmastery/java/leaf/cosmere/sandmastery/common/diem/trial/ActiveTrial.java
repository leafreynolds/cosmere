package leaf.cosmere.sandmastery.common.diem.trial;

import leaf.cosmere.sandmastery.api.trial.DiemTrial;
import leaf.cosmere.sandmastery.api.trial.ITrialContext;
import leaf.cosmere.sandmastery.api.trial.TrialPiece;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.entities.DiemTargetEntity;
import leaf.cosmere.sandmastery.common.network.packets.DiemCheckpointsMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

// one attempt at a trial by one player
public class ActiveTrial implements ITrialContext
{
	private static final double DEFAULT_LEASH = 16;

	final ServerPlayer player;
	final ServerLevel level;
	final BlockPos stone;
	final DiemTrial trial;
	final Component name;
	// practice runs (debug commands) never promote, never set a cooldown and skip the outside help rules
	final boolean practice;

	final List<TrialPiece.Instance> pieces = new ArrayList<>();
	final List<DiemTargetEntity> targets = new ArrayList<>();
	final ServerBossEvent bossBar;

	double leash = DEFAULT_LEASH;
	int totalTicks;
	int elapsedTicks = 0;
	int peakRibbonsInUse = 0;

	ActiveTrial(ServerPlayer player, BlockPos stone, DiemTrial trial, Component name, boolean practice)
	{
		this.player = player;
		this.level = player.serverLevel();
		this.stone = stone;
		this.trial = trial;
		this.name = name;
		this.practice = practice;

		for (TrialPiece piece : trial.pieces())
		{
			pieces.add(piece.start(this));
		}

		totalTicks = trial.timeLimitSeconds() * 20;
		for (TrialPiece.Instance piece : pieces)
		{
			totalTicks += piece.extraTimeTicks();
		}

		bossBar = new ServerBossEvent(name, BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS);
		bossBar.addPlayer(player);
	}

	int ticksLeft()
	{
		return totalTicks - elapsedTicks;
	}

	void updateBossBar()
	{
		bossBar.setProgress(Math.max(0, (float) ticksLeft() / totalTicks));

		Component title = name.copy().append(" - " + (ticksLeft() / 20) + "s");
		for (TrialPiece.Instance piece : pieces)
		{
			Component progress = piece.progress();
			if (progress != null)
			{
				title = title.copy().append(" - ").append(progress);
			}
		}
		bossBar.setName(title);
	}

	@Override
	public ServerPlayer player()
	{
		return player;
	}

	@Override
	public ServerLevel level()
	{
		return level;
	}

	@Override
	public BlockPos stone()
	{
		return stone;
	}

	@Override
	public int ticksElapsed()
	{
		return elapsedTicks;
	}

	@Override
	public DiemTargetEntity spawnTarget(Vec3 pos, float size, boolean decoy, boolean moving)
	{
		DiemTargetEntity target = DiemTargetEntity.create(level, player, pos, stoneCentre(), size, decoy, moving);
		// add to the list first, the target checks it each tick to know it's still wanted
		targets.add(target);
		level.addFreshEntity(target);
		return target;
	}

	@Override
	public void particle(ParticleOptions particle, Vec3 pos)
	{
		level.sendParticles(player, particle, true, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
	}

	@Override
	public void extendLeash(double distance)
	{
		leash = Math.max(leash, distance);
	}

	@Override
	public void showCheckpoints(List<Vec3> points, int next)
	{
		Sandmastery.packetHandler().sendTo(new DiemCheckpointsMessage(List.copyOf(points), next), player);
	}
}
