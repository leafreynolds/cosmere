/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.commands.subcommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class TestCommand extends ModCommand
{

	private static int testSub(CommandContext<CommandSourceStack> context, ServerPlayer player)
	{
		return testSub(context, player.getLevel());
	}

	private static int testSub(CommandContext<CommandSourceStack> context, ServerLevel world)
	{
		CommandSourceStack source = context.getSource();
		source.sendSuccess(new TranslatableComponent("command.cosmere.test.sub"), true);

		return SINGLE_SUCCESS;
	}

	public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		return Commands.literal("test")
				.requires(context -> context.hasPermission(2)).then(Commands.literal("sub").executes(context -> testSub(context, context.getSource().getPlayerOrException()))); // end add
	}

	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		return SINGLE_SUCCESS;
	}

}