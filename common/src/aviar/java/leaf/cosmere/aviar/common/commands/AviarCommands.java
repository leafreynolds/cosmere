/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.aviar.common.commands.subcommands.AviarCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;


public class AviarCommands
{

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal(Cosmere.MODID)
				.then(AviarCommand.register(dispatcher))
		);
	}
}
