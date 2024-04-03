/*
 * File updated ~ 22 - 3 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.tools.common.commands.subcommands.ToolsCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;


public class ToolsCommands
{

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal(Cosmere.MODID)
				.then(ToolsCommand.register(dispatcher))
		);
	}
}
