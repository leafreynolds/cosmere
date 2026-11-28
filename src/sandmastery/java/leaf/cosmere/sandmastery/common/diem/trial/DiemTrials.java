package leaf.cosmere.sandmastery.common.diem.trial;

import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.sandmastery.api.trial.DiemTrial;
import leaf.cosmere.sandmastery.api.trial.SandmasteryTrialAPI;
import leaf.cosmere.sandmastery.api.trial.TrialPiece;
import leaf.cosmere.sandmastery.api.trial.pieces.Checkpoints;
import leaf.cosmere.sandmastery.api.trial.pieces.Targets;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.config.SandmasteryConfigs;
import leaf.cosmere.sandmastery.common.blocks.entities.DiemStoneBE;
import leaf.cosmere.sandmastery.common.diem.DiemProgression;
import leaf.cosmere.sandmastery.common.diem.DiemRank;
import leaf.cosmere.sandmastery.common.entities.DiemTargetEntity;
import leaf.cosmere.sandmastery.common.network.packets.DiemCheckpointsMessage;
import leaf.cosmere.sandmastery.common.network.packets.OpenDiemTrialScreenMessage;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import leaf.cosmere.sandmastery.common.registries.SandmasteryEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// runs Diem trials: starting them, ticking them, and ending them with a pass or a fail
public final class DiemTrials
{
	// how close to the stone a player must be to start a trial from the trial screen
	private static final double STONE_REACH = 8;

	private static final Map<UUID, ActiveTrial> ACTIVE = new HashMap<>();

	private DiemTrials()
	{
	}

	public static boolean isInTrial(ServerPlayer player)
	{
		return ACTIVE.containsKey(player.getUUID());
	}

	// called when a ranked player uses the Diem stone
	public static void openTrialScreen(ServerPlayer player, ISpiritweb spiritweb, BlockPos stone)
	{
		SandmasterySpiritwebSubmodule submodule = SandmasterySpiritwebSubmodule.get(spiritweb);
		DiemRank next = submodule.getDiemRank() == null ? null : submodule.getDiemRank().next();
		if (next == null)
		{
			DiemProgression.sendStatus(player, spiritweb);
			return;
		}

		List<OpenDiemTrialScreenMessage.Entry> entries = new ArrayList<>();
		for (var entry : SandmasteryTrialAPI.trialRegistry().entrySet())
		{
			DiemTrial trial = entry.getValue();
			if (trial.rank() != next)
			{
				continue;
			}
			ResourceLocation id = entry.getKey().location();
			entries.add(new OpenDiemTrialScreenMessage.Entry(
					id,
					(int) Math.round(timeLimitSeconds(trial, player, stone)),
					hasCheckpoints(trial),
					Optional.ofNullable(unavailableReason(trial, player, stone))));
		}

		long cooldownTicks = Math.max(0, submodule.getTrialCooldownUntil() - player.level().getGameTime());
		int ribbons = SandmasterySpiritwebSubmodule.getRankRibbons(player);
		Sandmastery.packetHandler().sendTo(new OpenDiemTrialScreenMessage(stone, next.ordinal(), ribbons, (int) (cooldownTicks / 20), entries), player);
	}

	// called when the player presses Start or Scout on the trial screen. Everything the client sent is checked again here.
	public static void handleStartRequest(ServerPlayer player, ResourceLocation trialId, BlockPos stone, boolean scoutOnly)
	{
		boolean nearRealStone = player.level().getBlockState(stone).is(SandmasteryBlocks.DIEM_STONE.getBlock())
				&& player.distanceToSqr(Vec3.atCenterOf(stone)) <= STONE_REACH * STONE_REACH;
		DiemTrial trial = SandmasteryTrialAPI.get(trialId);
		if (!nearRealStone || trial == null)
		{
			return;
		}

		SpiritwebCapability.get(player).ifPresent(spiritweb ->
		{
			SandmasterySpiritwebSubmodule submodule = SandmasterySpiritwebSubmodule.get(spiritweb);
			DiemRank rank = submodule.getDiemRank();
			if (rank == null || rank.next() != trial.rank())
			{
				return;
			}
			if (scoutOnly)
			{
				TrialScouting.start(player, trial, stone);
				return;
			}

			Component refusal;
			if (SandmasterySpiritwebSubmodule.getRankRibbons(player) < trial.rank().getMinRibbons())
			{
				refusal = Component.translatable("diem.sandmastery.trial.refused.ribbons", trial.rank().getMinRibbons());
			}
			else if (player.level().getGameTime() < submodule.getTrialCooldownUntil())
			{
				refusal = Component.translatable("diem.sandmastery.trial.refused.cooldown");
			}
			else
			{
				refusal = start(player, trial, Component.translatable(SandmasteryTrialAPI.nameKey(trialId)), stone, false);
			}

			if (refusal != null)
			{
				player.sendSystemMessage(refusal.copy().withStyle(ChatFormatting.RED));
			}
		});
	}

	/**
	 * Starts a trial.
	 *
	 * @param practice practice runs (debug commands) never promote, never set a cooldown and skip the outside help rules
	 * @return why the trial couldn't start, or null if it started
	 */
	@Nullable
	public static Component start(ServerPlayer player, DiemTrial trial, Component name, BlockPos stone, boolean practice)
	{
		if (isInTrial(player))
		{
			return Component.translatable("diem.sandmastery.trial.refused.already_in_trial");
		}
		Component unavailable = unavailableReason(trial, player, stone);
		if (unavailable != null)
		{
			return unavailable;
		}
		if (!practice)
		{
			Component outsideHelp = OutsideHelp.beforeTrial(player);
			if (outsideHelp != null)
			{
				return outsideHelp;
			}
		}
		if (isOvermastered(player))
		{
			return Component.translatable("diem.sandmastery.trial.refused.overmastered");
		}

		TrialScouting.stop(player);
		ActiveTrial active = new ActiveTrial(player, stone, trial, name, practice);
		ACTIVE.put(player.getUUID(), active);
		DiemStoneBE.setShared(active.level, stone, DiemStoneBE.Look.GLOWING, 0);
		active.updateBossBar();
		player.sendSystemMessage(Component.translatable("diem.sandmastery.trial.started", name, active.totalTicks / 20));
		return null;
	}


	public static void tick(ServerPlayer player)
	{
		ActiveTrial trial = ACTIVE.get(player.getUUID());
		if (trial == null)
		{
			return;
		}

		trial.elapsedTicks++;
		for (TrialPiece.Instance piece : trial.pieces)
		{
			piece.tick();
		}
		SpiritwebCapability.get(player).ifPresent(spiritweb ->
				trial.peakRibbonsInUse = Math.max(trial.peakRibbonsInUse, SandmasterySpiritwebSubmodule.get(spiritweb).getUsedRibbons()));

		Component failReason = findFailReason(trial);
		if (failReason != null)
		{
			end(trial, false, failReason);
		}
		else if (trial.pieces.stream().allMatch(TrialPiece.Instance::isComplete))
		{
			end(trial, true, null);
		}
		else if (trial.ticksLeft() <= 0)
		{
			end(trial, false, Component.translatable("diem.sandmastery.trial.fail.time"));
		}
		else if (trial.elapsedTicks % 5 == 0)
		{
			trial.updateBossBar();
		}
	}

	@Nullable
	private static Component findFailReason(ActiveTrial trial)
	{
		for (TrialPiece.Instance piece : trial.pieces)
		{
			Component reason = piece.failReason();
			if (reason != null)
			{
				return reason;
			}
		}

		ServerPlayer player = trial.player;
		if (player.level() != trial.level)
		{
			return Component.translatable("diem.sandmastery.trial.fail.left_area");
		}
		if (player.distanceToSqr(trial.stoneCentre()) > trial.leash * trial.leash)
		{
			return Component.translatable("diem.sandmastery.trial.fail.left_area");
		}
		if (isOvermastered(player))
		{
			return Component.translatable("diem.sandmastery.trial.fail.overmastered");
		}
		if (!trial.practice)
		{
			return OutsideHelp.duringTrial(player);
		}
		return null;
	}

	private static boolean isOvermastered(ServerPlayer player)
	{
		return SpiritwebCapability.get(player)
				.map(spiritweb -> spiritweb.hasEffect(SandmasteryEffects.OVERMASTERED_EFFECT.get()))
				.orElse(false);
	}

	// fails the player's trial, if they're in one (death, logging out)
	public static void fail(ServerPlayer player, Component reason)
	{
		ActiveTrial trial = ACTIVE.get(player.getUUID());
		if (trial != null)
		{
			end(trial, false, reason);
		}
	}

	// outside help noticed by an event (teleporting, placing blocks, gaining an effect). Practice runs ignore it.
	public static void onOutsideHelp(ServerPlayer player, String helpKey)
	{
		ActiveTrial trial = ACTIVE.get(player.getUUID());
		if (trial != null && !trial.practice)
		{
			end(trial, false, Component.translatable("diem.sandmastery.trial.outside_help." + helpKey));
		}
	}

	// ends the trial with no pass, fail or cooldown (the stop command)
	public static boolean stop(ServerPlayer player)
	{
		ActiveTrial trial = ACTIVE.get(player.getUUID());
		if (trial == null)
		{
			return false;
		}
		cleanUp(trial);
		player.sendSystemMessage(Component.translatable("diem.sandmastery.trial.stopped"));
		return true;
	}

	private static void end(ActiveTrial trial, boolean passed, @Nullable Component failReason)
	{
		cleanUp(trial);
		ServerPlayer player = trial.player;
		float seconds = trial.elapsedTicks / 20F;

		if (passed)
		{
			player.sendSystemMessage(Component.translatable("diem.sandmastery.trial.passed", trial.name, String.format("%.1f", seconds)).withStyle(ChatFormatting.GREEN));
			player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1F, 1F);
			if (!trial.practice)
			{
				SpiritwebCapability.get(player).ifPresent(spiritweb -> DiemProgression.promote(player, spiritweb));
			}
			for (Component commendation : commendations(trial))
			{
				player.sendSystemMessage(commendation.copy().withStyle(ChatFormatting.GOLD));
			}
		}
		else
		{
			player.sendSystemMessage(Component.translatable("diem.sandmastery.trial.failed", trial.name, failReason).withStyle(ChatFormatting.RED));
			if (!trial.practice)
			{
				startCooldown(player);
			}
		}
	}

	private static void cleanUp(ActiveTrial trial)
	{
		ACTIVE.remove(trial.player.getUUID());
		boolean stoneStillInUse = ACTIVE.values().stream().anyMatch(other -> other.level == trial.level && other.stone.equals(trial.stone));
		if (!stoneStillInUse)
		{
			DiemStoneBE.setShared(trial.level, trial.stone, null, 0);
		}
		for (TrialPiece.Instance piece : trial.pieces)
		{
			piece.cleanup();
		}
		for (DiemTargetEntity target : trial.targets)
		{
			target.discard();
		}
		trial.bossBar.removeAllPlayers();
		Sandmastery.packetHandler().sendTo(new DiemCheckpointsMessage(List.of(), -1), trial.player);
	}

	private static void startCooldown(ServerPlayer player)
	{
		SpiritwebCapability.get(player).ifPresent(spiritweb ->
		{
			long until = player.level().getGameTime() + SandmasteryConfigs.SERVER.TRIAL_FAIL_COOLDOWN.get();
			SandmasterySpiritwebSubmodule.get(spiritweb).setTrialCooldownUntil(until);
			spiritweb.syncToClients(null);
		});
		player.sendSystemMessage(Component.translatable("diem.sandmastery.trial.cooldown"));
	}

	// the examiner's comments on an elegant pass
	private static List<Component> commendations(ActiveTrial trial)
	{
		List<Component> comments = new ArrayList<>();
		if (trial.peakRibbonsInUse <= trial.trial.rank().getMinRibbons())
		{
			comments.add(Component.translatable("diem.sandmastery.trial.commendation.few_ribbons"));
		}
		for (TrialPiece.Instance piece : trial.pieces)
		{
			if (piece instanceof Targets.Instance targets && targets.misses() == 0)
			{
				comments.add(Component.translatable("diem.sandmastery.trial.commendation.clean"));
			}
		}
		if (trial.ticksLeft() >= trial.totalTicks / 2)
		{
			comments.add(Component.translatable("diem.sandmastery.trial.commendation.swift"));
		}
		return comments;
	}

	// events from Sandmastery powers and targets

	public static void onShot(ServerPlayer player)
	{
		ActiveTrial trial = ACTIVE.get(player.getUUID());
		if (trial != null)
		{
			trial.pieces.forEach(TrialPiece.Instance::onShot);
		}
	}

	public static void onTargetHit(ServerPlayer player, DiemTargetEntity target)
	{
		ActiveTrial trial = ACTIVE.get(player.getUUID());
		if (trial != null)
		{
			trial.pieces.forEach(piece -> piece.onTargetHit(target));
		}
	}

	public static void onSandPlaced(ServerPlayer player, BlockPos pos)
	{
		ActiveTrial trial = ACTIVE.get(player.getUUID());
		if (trial != null)
		{
			trial.pieces.forEach(piece -> piece.onSandPlaced(pos));
		}
	}

	public static void onSandSpent(ServerPlayer player, int charge)
	{
		ActiveTrial trial = ACTIVE.get(player.getUUID());
		if (trial != null)
		{
			trial.pieces.forEach(piece -> piece.onSandSpent(charge));
		}
	}

	// targets remove themselves once their trial no longer lists them
	public static boolean ownsTarget(UUID owner, DiemTargetEntity target)
	{
		ActiveTrial trial = ACTIVE.get(owner);
		return trial != null && trial.targets.contains(target);
	}

	// helpers

	@Nullable
	private static Component unavailableReason(DiemTrial trial, ServerPlayer player, BlockPos stone)
	{
		for (TrialPiece piece : trial.pieces())
		{
			Component reason = piece.unavailableReason(player.serverLevel(), stone);
			if (reason != null)
			{
				return reason;
			}
		}
		return null;
	}

	private static double timeLimitSeconds(DiemTrial trial, ServerPlayer player, BlockPos stone)
	{
		double seconds = trial.timeLimitSeconds();
		for (TrialPiece piece : trial.pieces())
		{
			if (piece instanceof Checkpoints checkpoints)
			{
				seconds += checkpoints.courseSeconds(player.serverLevel(), stone);
			}
		}
		return seconds;
	}

	private static boolean hasCheckpoints(DiemTrial trial)
	{
		return trial.pieces().stream().anyMatch(piece -> piece instanceof Checkpoints);
	}
}
