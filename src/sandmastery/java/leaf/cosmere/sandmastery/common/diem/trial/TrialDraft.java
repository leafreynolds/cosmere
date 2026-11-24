package leaf.cosmere.sandmastery.common.diem.trial;

import leaf.cosmere.sandmastery.api.trial.DiemTrial;
import leaf.cosmere.sandmastery.api.trial.TrialPiece;
import leaf.cosmere.sandmastery.api.trial.pieces.Checkpoints;
import leaf.cosmere.sandmastery.api.trial.pieces.Tiles;
import leaf.cosmere.sandmastery.common.diem.DiemRank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A trial being built in game with /cosmere diem trial draft. One per player, kept in memory only:
 * export it to Java to keep it.
 */
public class TrialDraft
{
	private static final Map<UUID, TrialDraft> DRAFTS = new HashMap<>();
	private static final double DEFAULT_RING_RADIUS = 2.0;

	public DiemRank rank;
	public int timeLimitSeconds;
	public final List<TrialPiece> pieces = new ArrayList<>();

	public TrialDraft(DiemRank rank, int timeLimitSeconds)
	{
		this.rank = rank;
		this.timeLimitSeconds = timeLimitSeconds;
	}

	@Nullable
	public static TrialDraft get(UUID player)
	{
		return DRAFTS.get(player);
	}

	public static void set(UUID player, TrialDraft draft)
	{
		DRAFTS.put(player, draft);
	}

	public DiemTrial build()
	{
		return new DiemTrial(rank, timeLimitSeconds, pieces);
	}

	// adds a checkpoint to the draft's offset checkpoints, creating them as rings if there are none yet
	public void addCheckpoint(Vec3 offset, @Nullable Double radius)
	{
		int index = indexOfOffsetCheckpoints();
		Checkpoints checkpoints = index >= 0 ? (Checkpoints) pieces.get(index) : Checkpoints.rings(DEFAULT_RING_RADIUS);
		checkpoints = checkpoints.withOffset(offset);
		if (radius != null)
		{
			checkpoints = checkpoints.withRadius(radius);
		}
		replaceOrAdd(index, checkpoints);
	}

	// adds a tile, creating the draft's tiles if there are none yet. Every tile in a draft shares one size.
	public void addTile(BlockPos offset, int size)
	{
		int index = indexOf(Tiles.class);
		Tiles tiles = index >= 0 ? (Tiles) pieces.get(index) : new Tiles(List.of(), size);
		replaceOrAdd(index, tiles.withTile(offset).withSize(size));
	}

	public boolean removeLastCheckpoint()
	{
		int index = indexOfOffsetCheckpoints();
		if (index < 0)
		{
			return false;
		}
		pieces.set(index, ((Checkpoints) pieces.get(index)).withoutLastOffset());
		return true;
	}

	public boolean removeLastTile()
	{
		int index = indexOf(Tiles.class);
		if (index < 0)
		{
			return false;
		}
		pieces.set(index, ((Tiles) pieces.get(index)).withoutLastTile());
		return true;
	}

	public boolean setOrdered(boolean ordered)
	{
		int index = indexOf(Checkpoints.class);
		if (index < 0)
		{
			return false;
		}
		pieces.set(index, ((Checkpoints) pieces.get(index)).withOrdered(ordered));
		return true;
	}

	public boolean setLook(Checkpoints.Look look)
	{
		int index = indexOf(Checkpoints.class);
		if (index < 0)
		{
			return false;
		}
		pieces.set(index, ((Checkpoints) pieces.get(index)).withLook(look));
		return true;
	}

	private int indexOf(Class<? extends TrialPiece> type)
	{
		for (int i = 0; i < pieces.size(); i++)
		{
			if (type.isInstance(pieces.get(i)))
			{
				return i;
			}
		}
		return -1;
	}

	private int indexOfOffsetCheckpoints()
	{
		for (int i = 0; i < pieces.size(); i++)
		{
			if (pieces.get(i) instanceof Checkpoints checkpoints && !checkpoints.fromBlocks())
			{
				return i;
			}
		}
		return -1;
	}

	private void replaceOrAdd(int index, TrialPiece piece)
	{
		if (index >= 0)
		{
			pieces.set(index, piece);
		}
		else
		{
			pieces.add(piece);
		}
	}
}
