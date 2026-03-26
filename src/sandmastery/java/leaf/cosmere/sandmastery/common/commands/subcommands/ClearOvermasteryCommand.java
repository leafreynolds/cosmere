package leaf.cosmere.sandmastery.common.commands.subcommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import leaf.cosmere.common.commands.subcommands.ModCommand;
import leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import java.util.Collection;

public class ClearOvermasteryCommand extends ModCommand
{
	public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		return Commands.literal("clear_overmastery")
				.requires(context -> context.hasPermission(2))
				.executes(ClearOvermasteryCommand::clearOvermastery)
				.then(Commands.argument("target", EntityArgument.players())
						.requires(context -> context.hasPermission(2))
						.executes(ClearOvermasteryCommand::clearOvermastery));
	}

	private static int clearOvermastery(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		Collection<ServerPlayer> players = getPlayers(context, 3);

		for (ServerPlayer player : players)
		{
			AttributeInstance availableRibbons = player.getAttribute(SandmasteryAttributes.RIBBONS.getAttribute());

			if (availableRibbons == null)
			{
				continue;
			}

			if (availableRibbons.getModifier(SandmasteryAttributes.OVERMASTERY_UUID) != null)
			{
				availableRibbons.removeModifier(SandmasteryAttributes.OVERMASTERY_UUID);
			}

			if (availableRibbons.getModifier(SandmasteryAttributes.OVERMASTERY_SECONDARY_UUID) != null)
			{
				availableRibbons.removeModifier(SandmasteryAttributes.OVERMASTERY_SECONDARY_UUID);
			}
		}

		return SINGLE_SUCCESS;
	}

	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		return SINGLE_SUCCESS;
	}
}
