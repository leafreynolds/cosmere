/*
 * File updated ~ 13 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import leaf.cosmere.allomancy.common.commands.subcommands.FillMetalReservesCommand;
import leaf.cosmere.common.Cosmere;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;


public class AllomancyCommands
{

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal(Cosmere.MODID)
				.then(FillMetalReservesCommand.register(dispatcher))
		);
	}
}
