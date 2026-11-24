package leaf.cosmere.sandmastery.api.trial;

import leaf.cosmere.sandmastery.common.entities.DiemTargetEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public interface ITrialContext
{
	ServerPlayer player();

	ServerLevel level();

	// the Diem stone the trial is measured from. Offsets in pieces are measured from its centre.
	BlockPos stone();

	default Vec3 stoneCentre()
	{
		return Vec3.atCenterOf(stone());
	}

	int ticksElapsed();

	DiemTargetEntity spawnTarget(Vec3 pos, float size, boolean decoy, boolean moving);

	void particle(ParticleOptions particle, Vec3 pos);

	// the trialist may go this far from the stone without failing
	void extendLeash(double distance);

	// shows these points on the trialist's checkpoint bar; next is the index to highlight, or -1
	void showCheckpoints(List<Vec3> points, int next);
}
