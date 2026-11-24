package leaf.cosmere.sandmastery.common.commands.subcommands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.sandmastery.api.trial.DiemTrial;
import leaf.cosmere.sandmastery.api.trial.SandmasteryTrialAPI;
import leaf.cosmere.sandmastery.api.trial.TrialPiece;
import leaf.cosmere.sandmastery.api.trial.pieces.Checkpoints;
import leaf.cosmere.sandmastery.api.trial.pieces.HydrationBudget;
import leaf.cosmere.sandmastery.api.trial.pieces.NoGround;
import leaf.cosmere.sandmastery.api.trial.pieces.SandBudget;
import leaf.cosmere.sandmastery.api.trial.pieces.Targets;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.diem.DiemRank;
import leaf.cosmere.sandmastery.common.diem.trial.DiemTrials;
import leaf.cosmere.sandmastery.common.diem.trial.TrialDraft;
import leaf.cosmere.sandmastery.common.diem.trial.TrialScouting;
import leaf.cosmere.sandmastery.common.registries.SandmasteryPoiTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * /cosmere diem trial ... : testing and building trials in game.
 * <ul>
 *     <li>stop / clearcooldown / start &lt;id&gt; [target]</li>
 *     <li>draft new / add / set / remove / show / run / export / load: build a trial from pieces, run it, then export it as Java</li>
 * </ul>
 * Starting a trial by command is a practice run: no promotion, no cooldown, no outside help rules.
 */
public class DiemTrialCommand
{
	// how far away a Diem stone may be for commands that work relative to "the nearest stone"
	private static final int STONE_SEARCH_RADIUS = 64;

	public static ArgumentBuilder<CommandSourceStack, ?> register()
	{
		return Commands.literal("trial")
				.then(Commands.literal("stop")
						.executes(context -> stop(context, false))
						.then(Commands.argument("target", EntityArgument.players())
								.executes(context -> stop(context, true))))
				.then(Commands.literal("clearcooldown")
						.executes(context -> clearCooldown(context, false))
						.then(Commands.argument("target", EntityArgument.players())
								.executes(context -> clearCooldown(context, true))))
				.then(Commands.literal("start")
						.then(Commands.argument("id", ResourceLocationArgument.id())
								.suggests((context, builder) -> SharedSuggestionProvider.suggestResource(SandmasteryTrialAPI.trialRegistry().keySet(), builder))
								.executes(context -> start(context, false))
								.then(Commands.argument("target", EntityArgument.players())
										.executes(context -> start(context, true)))))
				.then(registerDraft());
	}

	private static ArgumentBuilder<CommandSourceStack, ?> registerDraft()
	{
		return Commands.literal("draft")
				.then(Commands.literal("new")
						.then(Commands.argument("rank", StringArgumentType.word())
								.suggests((context, builder) -> SharedSuggestionProvider.suggest(Arrays.stream(DiemRank.values()).map(DiemRank::getName), builder))
								.then(Commands.argument("seconds", IntegerArgumentType.integer(0))
										.executes(DiemTrialCommand::draftNew))))
				.then(Commands.literal("add")
						.then(registerAddTargets())
						.then(Commands.literal("checkpoint")
								.then(Commands.literal("here")
										.executes(context -> addCheckpoint(context, null))
										.then(Commands.argument("radius", DoubleArgumentType.doubleArg(0.5, 16))
												.executes(context -> addCheckpoint(context, DoubleArgumentType.getDouble(context, "radius"))))))
						.then(Commands.literal("checkpoints")
								.then(Commands.literal("blocks")
										.then(Commands.argument("parMultiplier", DoubleArgumentType.doubleArg(0.1, 10))
												.executes(context -> addPiece(context, Checkpoints.course(DoubleArgumentType.getDouble(context, "parMultiplier")))))))
						.then(Commands.literal("tile")
								.then(Commands.literal("here")
										.then(Commands.argument("size", IntegerArgumentType.integer(1, 15))
												.executes(DiemTrialCommand::addTile))))
						.then(Commands.literal("noground")
								.executes(context -> addPiece(context, new NoGround())))
						.then(Commands.literal("sandbudget")
								.then(Commands.argument("amount", IntegerArgumentType.integer(1))
										.executes(context -> addPiece(context, new SandBudget(IntegerArgumentType.getInteger(context, "amount"))))))
						.then(Commands.literal("hydrationbudget")
								.then(Commands.argument("amount", IntegerArgumentType.integer(1))
										.executes(context -> addPiece(context, new HydrationBudget(IntegerArgumentType.getInteger(context, "amount")))))))
				.then(Commands.literal("set")
						.then(Commands.literal("ordered")
								.then(Commands.argument("ordered", BoolArgumentType.bool())
										.executes(DiemTrialCommand::setOrdered)))
						.then(Commands.literal("look")
								.then(Commands.argument("look", StringArgumentType.word())
										.suggests((context, builder) -> SharedSuggestionProvider.suggest(Arrays.stream(Checkpoints.Look.values()).map(look -> look.name().toLowerCase(Locale.ROOT)), builder))
										.executes(DiemTrialCommand::setLook))))
				.then(Commands.literal("remove")
						.then(Commands.argument("index", IntegerArgumentType.integer(0))
								.executes(DiemTrialCommand::removePiece))
						.then(Commands.literal("last")
								.then(Commands.literal("checkpoint")
										.executes(context -> removeLast(context, true)))
								.then(Commands.literal("tile")
										.executes(context -> removeLast(context, false)))))
				.then(Commands.literal("show")
						.executes(DiemTrialCommand::show))
				.then(Commands.literal("run")
						.executes(DiemTrialCommand::run))
				.then(Commands.literal("export")
						.executes(DiemTrialCommand::export))
				.then(Commands.literal("load")
						.then(Commands.argument("id", ResourceLocationArgument.id())
								.suggests((context, builder) -> SharedSuggestionProvider.suggestResource(SandmasteryTrialAPI.trialRegistry().keySet(), builder))
								.executes(DiemTrialCommand::load)));
	}

	// draft add targets <count> <spawned> <size> <radius> <maxShots> [moving] [decoys], built from the last argument back
	private static ArgumentBuilder<CommandSourceStack, ?> registerAddTargets()
	{
		var decoys = Commands.argument("decoys", IntegerArgumentType.integer(0))
				.executes(context -> addTargets(context, BoolArgumentType.getBool(context, "moving"), IntegerArgumentType.getInteger(context, "decoys")));
		var moving = Commands.argument("moving", BoolArgumentType.bool())
				.executes(context -> addTargets(context, BoolArgumentType.getBool(context, "moving"), 0))
				.then(decoys);
		var maxShots = Commands.argument("maxShots", IntegerArgumentType.integer(1))
				.executes(context -> addTargets(context, false, 0))
				.then(moving);
		var radius = Commands.argument("radius", DoubleArgumentType.doubleArg(1, 64)).then(maxShots);
		var size = Commands.argument("size", FloatArgumentType.floatArg(0.25F, 4F)).then(radius);
		var spawned = Commands.argument("spawned", IntegerArgumentType.integer(1)).then(size);
		var count = Commands.argument("count", IntegerArgumentType.integer(1)).then(spawned);
		return Commands.literal("targets").then(count);
	}


	private static int stop(CommandContext<CommandSourceStack> context, boolean hasTarget) throws CommandSyntaxException
	{
		for (ServerPlayer player : targets(context, hasTarget))
		{
			if (!DiemTrials.stop(player))
			{
				context.getSource().sendFailure(Component.literal(player.getName().getString() + " is not in a trial"));
			}
			TrialScouting.stop(player);
		}
		return 1;
	}

	private static int clearCooldown(CommandContext<CommandSourceStack> context, boolean hasTarget) throws CommandSyntaxException
	{
		for (ServerPlayer player : targets(context, hasTarget))
		{
			SpiritwebCapability.get(player).ifPresent(spiritweb ->
			{
				SandmasterySpiritwebSubmodule.get(spiritweb).setTrialCooldownUntil(0);
				spiritweb.syncToClients(null);
			});
			context.getSource().sendSuccess(() -> Component.literal("Cleared the trial cooldown of " + player.getName().getString()), true);
		}
		return 1;
	}

	private static int start(CommandContext<CommandSourceStack> context, boolean hasTarget) throws CommandSyntaxException
	{
		ResourceLocation id = ResourceLocationArgument.getId(context, "id");
		DiemTrial trial = SandmasteryTrialAPI.get(id);
		if (trial == null)
		{
			context.getSource().sendFailure(Component.literal("Unknown trial: " + id));
			return 0;
		}
		for (ServerPlayer player : targets(context, hasTarget))
		{
			startPractice(context, player, trial, Component.translatable(SandmasteryTrialAPI.nameKey(id)));
		}
		return 1;
	}

	private static void startPractice(CommandContext<CommandSourceStack> context, ServerPlayer player, DiemTrial trial, Component name)
	{
		Component refusal = DiemTrials.start(player, trial, name, stoneOrPlayerPos(player), true);
		if (refusal != null)
		{
			context.getSource().sendFailure(refusal);
		}
	}


	private static int draftNew(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		DiemRank rank = DiemRank.byName(StringArgumentType.getString(context, "rank"));
		if (rank == null)
		{
			context.getSource().sendFailure(Component.literal("Unknown Diem rank"));
			return 0;
		}
		TrialDraft.set(context.getSource().getPlayerOrException().getUUID(), new TrialDraft(rank, IntegerArgumentType.getInteger(context, "seconds")));
		context.getSource().sendSuccess(() -> Component.literal("Started a new draft trial"), false);
		return 1;
	}

	private static int addTargets(CommandContext<CommandSourceStack> context, boolean moving, int decoys) throws CommandSyntaxException
	{
		return addPiece(context, new Targets(
				IntegerArgumentType.getInteger(context, "count"),
				IntegerArgumentType.getInteger(context, "spawned"),
				FloatArgumentType.getFloat(context, "size"),
				DoubleArgumentType.getDouble(context, "radius"),
				IntegerArgumentType.getInteger(context, "maxShots"),
				moving,
				decoys));
	}

	private static int addPiece(CommandContext<CommandSourceStack> context, TrialPiece piece) throws CommandSyntaxException
	{
		TrialDraft draft = draft(context);
		if (draft == null)
		{
			return 0;
		}
		draft.pieces.add(piece);
		context.getSource().sendSuccess(() -> Component.literal("Added " + piece.toJava()), false);
		return 1;
	}

	private static int addCheckpoint(CommandContext<CommandSourceStack> context, @Nullable Double radius) throws CommandSyntaxException
	{
		TrialDraft draft = draft(context);
		ServerPlayer player = context.getSource().getPlayerOrException();
		BlockPos stone = nearestStone(player);
		if (draft == null || stone == null)
		{
			return failNoStone(context, draft);
		}
		// measured from the stone's centre to the player's body, rounded so exported code stays tidy
		Vec3 offset = player.getBoundingBox().getCenter().subtract(Vec3.atCenterOf(stone));
		offset = new Vec3(Math.round(offset.x * 2) / 2.0, Math.round(offset.y * 2) / 2.0, Math.round(offset.z * 2) / 2.0);
		draft.addCheckpoint(offset, radius);

		Vec3 added = offset;
		context.getSource().sendSuccess(() -> Component.literal(String.format(Locale.ROOT, "Added a checkpoint at %.1f, %.1f, %.1f from the stone", added.x, added.y, added.z)), false);
		return 1;
	}

	private static int addTile(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		TrialDraft draft = draft(context);
		ServerPlayer player = context.getSource().getPlayerOrException();
		BlockPos stone = nearestStone(player);
		if (draft == null || stone == null)
		{
			return failNoStone(context, draft);
		}
		BlockPos offset = player.getOnPos().subtract(stone);
		draft.addTile(offset, IntegerArgumentType.getInteger(context, "size"));
		context.getSource().sendSuccess(() -> Component.literal("Added a tile at " + offset.toShortString() + " from the stone"), false);
		return 1;
	}

	private static int setOrdered(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		TrialDraft draft = draft(context);
		if (draft == null)
		{
			return 0;
		}
		if (!draft.setOrdered(BoolArgumentType.getBool(context, "ordered")))
		{
			context.getSource().sendFailure(Component.literal("The draft has no checkpoints"));
			return 0;
		}
		context.getSource().sendSuccess(() -> Component.literal("Updated the checkpoints"), false);
		return 1;
	}

	private static int setLook(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		TrialDraft draft = draft(context);
		if (draft == null)
		{
			return 0;
		}
		Checkpoints.Look look;
		try
		{
			look = Checkpoints.Look.valueOf(StringArgumentType.getString(context, "look").toUpperCase(Locale.ROOT));
		}
		catch (IllegalArgumentException e)
		{
			context.getSource().sendFailure(Component.literal("Unknown look, use ring, beacon or pad"));
			return 0;
		}
		if (!draft.setLook(look))
		{
			context.getSource().sendFailure(Component.literal("The draft has no checkpoints"));
			return 0;
		}
		context.getSource().sendSuccess(() -> Component.literal("Updated the checkpoints"), false);
		return 1;
	}

	private static int removePiece(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		TrialDraft draft = draft(context);
		int index = IntegerArgumentType.getInteger(context, "index");
		if (draft == null)
		{
			return 0;
		}
		if (index >= draft.pieces.size())
		{
			context.getSource().sendFailure(Component.literal("The draft has no piece " + index + ", see /cosmere diem trial draft show"));
			return 0;
		}
		TrialPiece removed = draft.pieces.remove(index);
		context.getSource().sendSuccess(() -> Component.literal("Removed " + removed.toJava()), false);
		return 1;
	}

	private static int removeLast(CommandContext<CommandSourceStack> context, boolean checkpoint) throws CommandSyntaxException
	{
		TrialDraft draft = draft(context);
		if (draft == null)
		{
			return 0;
		}
		boolean removed = checkpoint ? draft.removeLastCheckpoint() : draft.removeLastTile();
		if (!removed)
		{
			context.getSource().sendFailure(Component.literal(checkpoint ? "The draft has no checkpoints" : "The draft has no tiles"));
			return 0;
		}
		context.getSource().sendSuccess(() -> Component.literal(checkpoint ? "Removed the last checkpoint" : "Removed the last tile"), false);
		return 1;
	}

	// lists the pieces and shows the checkpoints and tiles for 10 seconds
	private static int show(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		TrialDraft draft = draft(context);
		if (draft == null)
		{
			return 0;
		}
		context.getSource().sendSuccess(() -> Component.literal("Draft: " + draft.rank.getName() + ", " + draft.timeLimitSeconds + "s"), false);
		for (int i = 0; i < draft.pieces.size(); i++)
		{
			String line = i + ": " + draft.pieces.get(i).toJava();
			context.getSource().sendSuccess(() -> Component.literal(line).withStyle(ChatFormatting.GRAY), false);
		}
		ServerPlayer player = context.getSource().getPlayerOrException();
		TrialScouting.start(player, draft.build(), stoneOrPlayerPos(player), 10);
		return 1;
	}

	private static int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		TrialDraft draft = draft(context);
		if (draft == null)
		{
			return 0;
		}
		startPractice(context, context.getSource().getPlayerOrException(), draft.build(), Component.literal("Draft trial"));
		return 1;
	}

	private static int export(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		TrialDraft draft = draft(context);
		if (draft == null)
		{
			return 0;
		}
		String java = draft.build().toJava();
		Component copy = Component.literal("[Click to copy]").withStyle(style -> style
				.withColor(ChatFormatting.AQUA)
				.withUnderlined(true)
				.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, java)));
		context.getSource().sendSuccess(() -> Component.literal(java).withStyle(ChatFormatting.GRAY), false);
		context.getSource().sendSuccess(() -> copy, false);
		return 1;
	}

	private static int load(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		ResourceLocation id = ResourceLocationArgument.getId(context, "id");
		DiemTrial trial = SandmasteryTrialAPI.get(id);
		if (trial == null)
		{
			context.getSource().sendFailure(Component.literal("Unknown trial: " + id));
			return 0;
		}
		TrialDraft draft = new TrialDraft(trial.rank(), trial.timeLimitSeconds());
		draft.pieces.addAll(trial.pieces());
		TrialDraft.set(context.getSource().getPlayerOrException().getUUID(), draft);
		context.getSource().sendSuccess(() -> Component.literal("Loaded " + id + " into your draft"), false);
		return 1;
	}

	// helpers

	private static Collection<ServerPlayer> targets(CommandContext<CommandSourceStack> context, boolean hasTarget) throws CommandSyntaxException
	{
		return hasTarget ? EntityArgument.getPlayers(context, "target") : List.of(context.getSource().getPlayerOrException());
	}

	@Nullable
	private static TrialDraft draft(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		TrialDraft draft = TrialDraft.get(context.getSource().getPlayerOrException().getUUID());
		if (draft == null)
		{
			context.getSource().sendFailure(Component.literal("You have no draft trial, start one with /cosmere diem trial draft new <rank> <seconds>"));
		}
		return draft;
	}

	@Nullable
	private static BlockPos nearestStone(ServerPlayer player)
	{
		return player.serverLevel().getPoiManager()
				.findClosest(poi -> poi.is(SandmasteryPoiTypes.DIEM_STONE.getKey()), player.blockPosition(), STONE_SEARCH_RADIUS, PoiManager.Occupancy.ANY)
				.orElse(null);
	}

	private static BlockPos stoneOrPlayerPos(ServerPlayer player)
	{
		BlockPos stone = nearestStone(player);
		return stone != null ? stone : player.blockPosition();
	}

	private static int failNoStone(CommandContext<CommandSourceStack> context, @Nullable TrialDraft draft)
	{
		if (draft != null)
		{
			context.getSource().sendFailure(Component.literal("No Diem stone within " + STONE_SEARCH_RADIUS + " blocks"));
		}
		return 0;
	}
}
