/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.commands.subcommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.commands.subcommands.ModCommand;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.commands.arguments.RadiantOrderArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class HeraldCommand extends ModCommand
{
	public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		return Commands.literal("herald")
				.requires(context -> context.hasPermission(2))
				.then(Commands.literal("make")
						.then(Commands.argument("order", RadiantOrderArgumentType.createArgument())
								.executes(HeraldCommand::makeHerald)
								.then(Commands.argument("target", EntityArgument.players())
										.executes(HeraldCommand::makeHerald))))
				.then(Commands.literal("remove")
						.executes(HeraldCommand::removeHerald)
						.then(Commands.argument("target", EntityArgument.players())
								.executes(HeraldCommand::removeHerald)));
	}

	private static int makeHerald(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		Collection<ServerPlayer> players = getPlayers(context, 4);

		Roshar.RadiantOrder order = context.getArgument("order", Roshar.RadiantOrder.class);

		for (ServerPlayer player : players)
		{
			SpiritwebCapability.get(player).ifPresent((spiritweb) ->
			{
				SurgebindingSpiritwebSubmodule ssm = SurgebindingSpiritwebSubmodule.getSubmodule(spiritweb);
				ssm.setHerald(true);

				spiritweb.syncToClients(player);
			});
		}

		return SINGLE_SUCCESS;
	}

	private static int removeHerald(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		Collection<ServerPlayer> players = getPlayers(context, 4);

		for (ServerPlayer player : players)
		{
			SpiritwebCapability.get(player).ifPresent((spiritweb) ->
			{
				SurgebindingSpiritwebSubmodule ssm = SurgebindingSpiritwebSubmodule.getSubmodule(spiritweb);
				ssm.setHerald(false);

				spiritweb.syncToClients(player);
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