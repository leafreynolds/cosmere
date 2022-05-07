/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;

public abstract class ModCommand implements Command<CommandSourceStack>
{
	private int permLevel = 2;

	public ModCommand()
	{
	}

	public ModCommand(int level)
	{
		this.permLevel = level;
	}

	public boolean canExecute(CommandSourceStack source) throws CommandSyntaxException
	{
		return source.hasPermission(permLevel);
	}
}
