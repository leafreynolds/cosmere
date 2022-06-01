/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Collection;

public abstract class ModCommand implements Command<CommandSourceStack>
{
	public ModCommand()	{	}

	//I'm not entirely certain this works.
	public static Collection<ServerPlayer> getPlayers(CommandContext<CommandSourceStack> context, int numOfParams) throws CommandSyntaxException
	{
		Collection<ServerPlayer> players = new ArrayList<>();
		final String[] s = context.getInput().split(" ");
		if (s.length <= numOfParams)
		{
			players.add(context.getSource().getPlayerOrException());
		}
		else
		{
			players = EntityArgument.getPlayers(context, "target");
		}
		return players;
	}
}
