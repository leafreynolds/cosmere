/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.soulforgery.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.soulforgery.common.commands.subcommands.SoulforgeryCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;


public class SoulforgeryCommands
{

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal(Cosmere.MODID)
				.then(SoulforgeryCommand.register(dispatcher))
		);
	}
}
