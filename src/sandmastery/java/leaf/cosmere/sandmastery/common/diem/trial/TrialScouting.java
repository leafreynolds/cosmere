package leaf.cosmere.sandmastery.common.diem.trial;

import leaf.cosmere.sandmastery.api.trial.DiemTrial;
import leaf.cosmere.sandmastery.api.trial.TrialPiece;
import leaf.cosmere.sandmastery.api.trial.pieces.Checkpoints;
import leaf.cosmere.sandmastery.api.trial.pieces.Tiles;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.network.packets.DiemCheckpointsMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// shows a trial's checkpoints and tiles for a while, with no timer and no rules, so the player can plan a route
public final class TrialScouting
{
	public static final int SCOUT_SECONDS = 60;

	private static final Map<UUID, Scout> SCOUTS = new HashMap<>();

	private record Scout(DiemTrial trial, BlockPos stone, List<Vec3> checkpoints, int endTick)
	{
	}

	private TrialScouting()
	{
	}

	public static void start(ServerPlayer player, DiemTrial trial, BlockPos stone)
	{
		start(player, trial, stone, SCOUT_SECONDS);
	}

	public static void start(ServerPlayer player, DiemTrial trial, BlockPos stone, int seconds)
	{
		// a running trial owns the checkpoint bar
		if (DiemTrials.isInTrial(player))
		{
			return;
		}

		List<Vec3> checkpoints = new ArrayList<>();
		for (TrialPiece piece : trial.pieces())
		{
			if (piece instanceof Checkpoints checkpointPiece)
			{
				checkpoints.addAll(checkpointPiece.points(player.serverLevel(), stone));
			}
		}

		SCOUTS.put(player.getUUID(), new Scout(trial, stone, checkpoints, player.tickCount + seconds * 20));
		Sandmastery.packetHandler().sendTo(new DiemCheckpointsMessage(checkpoints, -1), player);
		player.sendSystemMessage(Component.translatable("diem.sandmastery.trial.scouting", seconds));
	}

	public static void stop(ServerPlayer player)
	{
		if (SCOUTS.remove(player.getUUID()) != null)
		{
			Sandmastery.packetHandler().sendTo(new DiemCheckpointsMessage(List.of(), -1), player);
		}
	}

	public static void tick(ServerPlayer player)
	{
		Scout scout = SCOUTS.get(player.getUUID());
		if (scout == null)
		{
			return;
		}
		if (player.tickCount >= scout.endTick())
		{
			stop(player);
			return;
		}
		if (player.tickCount % 5 != 0)
		{
			return;
		}

		Vec3 stoneCentre = Vec3.atCenterOf(scout.stone());
		for (TrialPiece piece : scout.trial().pieces())
		{
			if (piece instanceof Checkpoints checkpoints)
			{
				for (Vec3 point : checkpoints.points(player.serverLevel(), scout.stone()))
				{
					Checkpoints.draw((type, pos) -> particle(player, type, pos), point, stoneCentre, checkpoints.look(), checkpoints.radius(), true);
				}
			}
			else if (piece instanceof Tiles tiles)
			{
				tiles.draw((type, pos) -> particle(player, type, pos), scout.stone());
			}
		}
	}

	private static void particle(ServerPlayer player, ParticleOptions type, Vec3 pos)
	{
		player.serverLevel().sendParticles(player, type, true, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
	}
}
