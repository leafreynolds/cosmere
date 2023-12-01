/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.awakening.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.awakening.common.commands.subcommands.AwakeningCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;


public class AwakeningCommands
{

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal(Cosmere.MODID)
				.then(AwakeningCommand.register(dispatcher))
		);
	}
}
