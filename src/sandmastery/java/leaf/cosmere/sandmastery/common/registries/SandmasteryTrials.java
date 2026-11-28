package leaf.cosmere.sandmastery.common.registries;

import leaf.cosmere.sandmastery.api.trial.DiemTrial;
import leaf.cosmere.sandmastery.api.trial.SandmasteryTrialAPI;
import leaf.cosmere.sandmastery.api.trial.pieces.Checkpoints;
import leaf.cosmere.sandmastery.api.trial.pieces.NoGround;
import leaf.cosmere.sandmastery.api.trial.pieces.Targets;
import leaf.cosmere.sandmastery.api.trial.pieces.Tiles;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.diem.DiemRank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;


public class SandmasteryTrials
{
	public static final DeferredRegister<DiemTrial> TRIALS = DeferredRegister.create(SandmasteryTrialAPI.TRIAL_REGISTRY_KEY, Sandmastery.MODID);

	// Underfen
	static
	{
		TRIALS.register("accuracy_underfen", () -> new DiemTrial(DiemRank.UNDERFEN, 60, List.of(
				new Targets(3, 4, 1.5F, 8, 8, false, 0))));
		TRIALS.register("crossing_underfen", () -> new DiemTrial(DiemRank.UNDERFEN, 60, List.of(
				Checkpoints.beacon(new Vec3(0, 2, 8)),
				new NoGround(),
				new Tiles(List.of(new BlockPos(0, 0, 2), new BlockPos(0, 0, 4), new BlockPos(0, 0, 6)), 5))));
	}

	// Fen
	static
	{
		TRIALS.register("accuracy_fen", () -> new DiemTrial(DiemRank.FEN, 60, List.of(
				new Targets(5, 6, 1.25F, 10, 10, false, 0))));
		TRIALS.register("crossing_tiles_fen", () -> new DiemTrial(DiemRank.FEN, 60, List.of(
				Checkpoints.beacon(new Vec3(0, 3, 9)),
				new NoGround(),
				new Tiles(List.of(new BlockPos(0, 1, 2), new BlockPos(0, 2, 5), new BlockPos(0, 2, 8)), 3))));
		TRIALS.register("crossing_fen", () -> new DiemTrial(DiemRank.FEN, 45, List.of(
				Checkpoints.beacon(new Vec3(0, 4, 12)),
				new NoGround())));
		TRIALS.register("course_fen", () -> new DiemTrial(DiemRank.FEN, 0, List.of(
				Checkpoints.course(2.0))));
	}

	// Diemfen
	static
	{
		TRIALS.register("accuracy_diemfen", () -> new DiemTrial(DiemRank.DIEMFEN, 60, List.of(
				new Targets(6, 7, 1.0F, 12, 11, false, 0))));
		TRIALS.register("crossing_diemfen", () -> new DiemTrial(DiemRank.DIEMFEN, 60, List.of(
				Checkpoints.beacon(new Vec3(8, 5, 16)),
				new NoGround())));
		TRIALS.register("rings_diemfen", () -> new DiemTrial(DiemRank.DIEMFEN, 60, List.of(
				Checkpoints.rings(2.5, new Vec3(0, 4, 6), new Vec3(6, 6, 6), new Vec3(6, 8, 0)))));
		TRIALS.register("course_diemfen", () -> new DiemTrial(DiemRank.DIEMFEN, 0, List.of(
				Checkpoints.course(1.75))));
	}

	// Underlestrell
	static
	{
		TRIALS.register("accuracy_underlestrell", () -> new DiemTrial(DiemRank.UNDERLESTRELL, 60, List.of(
				new Targets(6, 7, 1.0F, 14, 10, true, 0))));
		TRIALS.register("crossing_tiles_underlestrell", () -> new DiemTrial(DiemRank.UNDERLESTRELL, 60, List.of(
				Checkpoints.beacon(new Vec3(0, 4, 10)),
				new NoGround(),
				new Tiles(List.of(new BlockPos(0, 1, 3), new BlockPos(0, 2, 6), new BlockPos(0, 3, 9)), 1))));
		TRIALS.register("rings_underlestrell", () -> new DiemTrial(DiemRank.UNDERLESTRELL, 75, List.of(
				Checkpoints.rings(2.0, new Vec3(0, 5, 8), new Vec3(8, 8, 8), new Vec3(8, 11, 0), new Vec3(0, 14, 0)))));
		TRIALS.register("course_underlestrell", () -> new DiemTrial(DiemRank.UNDERLESTRELL, 0, List.of(
				Checkpoints.course(1.5))));
	}

	// Lestrell
	static
	{
		TRIALS.register("accuracy_lestrell", () -> new DiemTrial(DiemRank.LESTRELL, 60, List.of(
				new Targets(8, 9, 0.8F, 16, 12, false, 3))));
		TRIALS.register("rings_lestrell", () -> new DiemTrial(DiemRank.LESTRELL, 75, List.of(
				Checkpoints.rings(1.5, new Vec3(0, 6, 12), new Vec3(12, 9, 12), new Vec3(12, 12, -12), new Vec3(-12, 15, 0)))));
		TRIALS.register("rings_targets_lestrell", () -> new DiemTrial(DiemRank.LESTRELL, 90, List.of(
				Checkpoints.rings(2.0, new Vec3(0, 6, 10), new Vec3(10, 9, 0), new Vec3(0, 12, -10)),
				new Targets(4, 5, 1.0F, 12, 8, false, 0))));
		TRIALS.register("course_lestrell", () -> new DiemTrial(DiemRank.LESTRELL, 0, List.of(
				Checkpoints.course(1.3))));
	}

	// Undermastrell
	static
	{
		TRIALS.register("crossing_decoys_undermastrell", () -> new DiemTrial(DiemRank.UNDERMASTRELL, 90, List.of(
				Checkpoints.beacon(new Vec3(0, 5, 13)),
				new NoGround(),
				new Tiles(List.of(new BlockPos(0, 2, 3), new BlockPos(0, 3, 7), new BlockPos(0, 4, 11)), 2),
				new Targets(3, 4, 1.0F, 10, 8, false, 2))));
		TRIALS.register("rings_undermastrell", () -> new DiemTrial(DiemRank.UNDERMASTRELL, 90, List.of(
				Checkpoints.rings(1.25, new Vec3(0, 10, 10), new Vec3(10, 14, 10), new Vec3(10, 18, -10), new Vec3(-10, 22, -10), new Vec3(0, 26, 0)))));
		TRIALS.register("crossing_undermastrell", () -> new DiemTrial(DiemRank.UNDERMASTRELL, 60, List.of(
				Checkpoints.beacon(new Vec3(14, 10, 24)),
				new NoGround())));
		TRIALS.register("course_undermastrell", () -> new DiemTrial(DiemRank.UNDERMASTRELL, 0, List.of(
				Checkpoints.course(1.15))));
	}

	// Mastrell
	static
	{
		TRIALS.register("gauntlet_mastrell", () -> new DiemTrial(DiemRank.MASTRELL, 150, List.of(
				Checkpoints.rings(1.5, new Vec3(0, 8, 14), new Vec3(14, 12, 14), new Vec3(14, 16, -14), new Vec3(-14, 20, -14), new Vec3(0, 24, 0)),
				new Targets(8, 9, 0.8F, 16, 12, true, 4))));
		TRIALS.register("course_mastrell", () -> new DiemTrial(DiemRank.MASTRELL, 0, List.of(
				Checkpoints.course(1.0))));
	}
}
