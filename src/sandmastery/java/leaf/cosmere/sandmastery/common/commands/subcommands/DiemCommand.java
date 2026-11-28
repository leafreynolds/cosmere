package leaf.cosmere.sandmastery.common.commands.subcommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.commands.subcommands.ModCommand;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.diem.DiemProgression;
import leaf.cosmere.sandmastery.common.diem.DiemRank;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Arrays;
import java.util.stream.Stream;

// /cosmere diem info|rank|xp, for testing Diem progression without the stone or trials
public class DiemCommand extends ModCommand
{
	private static final String UNTESTED = "untested";

	public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		return Commands.literal("diem")
				.requires(context -> context.hasPermission(2))
				.then(Commands.literal("info")
						.executes(DiemCommand::info)
						.then(Commands.argument("target", EntityArgument.players())
								.executes(DiemCommand::info)))
				.then(Commands.literal("rank")
						.then(Commands.argument("rank", StringArgumentType.word())
								.suggests((context, builder) -> SharedSuggestionProvider.suggest(
										Stream.concat(Stream.of(UNTESTED), Arrays.stream(DiemRank.values()).map(DiemRank::getName)), builder))
								.executes(DiemCommand::setRank)
								.then(Commands.argument("target", EntityArgument.players())
										.executes(DiemCommand::setRank))))
				.then(Commands.literal("promote")
						.executes(DiemCommand::promote)
						.then(Commands.argument("target", EntityArgument.players())
								.executes(DiemCommand::promote)))
				.then(DiemTrialCommand.register())
				.then(Commands.literal("xp")
						.then(Commands.argument("amount", IntegerArgumentType.integer(0))
								.executes(DiemCommand::setXp)
								.then(Commands.argument("target", EntityArgument.players())
										.executes(DiemCommand::setXp))));
	}

	private static int info(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		for (ServerPlayer player : getPlayers(context, 3))
		{
			SpiritwebCapability.get(player).ifPresent(spiritweb ->
			{
				SandmasterySpiritwebSubmodule submodule = SandmasterySpiritwebSubmodule.get(spiritweb);
				DiemRank rank = submodule.getDiemRank();
				Component rankName = rank == null ? Component.literal(UNTESTED) : rank.getDisplayName();
				context.getSource().sendSuccess(() -> Component.literal(player.getName().getString() + ": ")
						.append(rankName)
						.append(String.format(", %d ribbons, %d mastery xp", SandmasterySpiritwebSubmodule.getRankRibbons(player), submodule.getMasteryXp())), false);
			});
		}
		return SINGLE_SUCCESS;
	}

	private static int setRank(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		String name = StringArgumentType.getString(context, "rank");
		DiemRank rank = DiemRank.byName(name);
		if (rank == null && !name.equals(UNTESTED))
		{
			context.getSource().sendFailure(Component.literal("Unknown Diem rank: " + name));
			return 0;
		}

		for (ServerPlayer player : getPlayers(context, 4))
		{
			SpiritwebCapability.get(player).ifPresent(spiritweb ->
			{
				SandmasterySpiritwebSubmodule.get(spiritweb).setDiemRank(rank);
				spiritweb.syncToClients(null);
				context.getSource().sendSuccess(() -> Component.literal("Set Diem rank of " + player.getName().getString() + " to " + name), true);
			});
		}
		return SINGLE_SUCCESS;
	}

	// skips the trial; untested players must use the Diem stone first
	private static int promote(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		for (ServerPlayer player : getPlayers(context, 3))
		{
			SpiritwebCapability.get(player).ifPresent(spiritweb ->
			{
				DiemRank promoted = DiemProgression.promote(player, spiritweb);
				if (promoted == null)
				{
					context.getSource().sendFailure(Component.literal(player.getName().getString() + " is untested or already at the highest rank"));
				}
				else
				{
					context.getSource().sendSuccess(() -> Component.literal("Promoted " + player.getName().getString() + " to ").append(promoted.getDisplayName()), true);
				}
			});
		}
		return SINGLE_SUCCESS;
	}

	private static int setXp(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		int amount = IntegerArgumentType.getInteger(context, "amount");
		for (ServerPlayer player : getPlayers(context, 4))
		{
			SpiritwebCapability.get(player).ifPresent(spiritweb ->
			{
				SandmasterySpiritwebSubmodule.get(spiritweb).setMasteryXp(amount);
				spiritweb.syncToClients(null);
				context.getSource().sendSuccess(() -> Component.literal("Set mastery xp of " + player.getName().getString() + " to " + amount), true);
			});
		}
		return SINGLE_SUCCESS;
	}

	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		return SINGLE_SUCCESS;
	}
}
