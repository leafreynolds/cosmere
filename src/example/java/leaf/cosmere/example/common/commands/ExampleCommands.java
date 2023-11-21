/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.example.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.example.common.commands.subcommands.ExampleCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;


public class ExampleCommands
{

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal(Cosmere.MODID)
				.then(ExampleCommand.register(dispatcher))
		);
	}
}
