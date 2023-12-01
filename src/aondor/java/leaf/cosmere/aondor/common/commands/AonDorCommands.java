/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aondor.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import leaf.cosmere.aondor.common.commands.subcommands.AonDorCommand;
import leaf.cosmere.common.Cosmere;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;


public class AonDorCommands
{

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal(Cosmere.MODID)
				.then(AonDorCommand.register(dispatcher))
		);
	}
}
