package leaf.cosmere.sandmastery.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.sandmastery.common.commands.subcommands.ClearOvermasteryCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class SandmasteryCommands
{
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal(Cosmere.MODID)
				.then(ClearOvermasteryCommand.register(dispatcher))
		);
	}
}
