package leaf.cosmere.sandmastery.common.commands.subcommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import leaf.cosmere.common.commands.subcommands.ModCommand;
import leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Collection;

public class AddOvermasteryCommand extends ModCommand
{
	public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		return Commands.literal("add_overmastery")
				.requires(context -> context.hasPermission(2))
				.then(Commands.argument("ribbons", IntegerArgumentType.integer(1))
						.executes(AddOvermasteryCommand::addOvermastery)
						.then(Commands.argument("target", EntityArgument.players())
								.requires(context -> context.hasPermission(2))
								.executes(AddOvermasteryCommand::addOvermastery)));
	}

	private static int addOvermastery(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		int ribbons = IntegerArgumentType.getInteger(context, "ribbons");
		Collection<ServerPlayer> players = getPlayers(context, 3);

		for (ServerPlayer player : players)
		{
			AttributeInstance availableRibbons = player.getAttribute(SandmasteryAttributes.RIBBONS.getHolder());

			if (availableRibbons == null)
			{
				continue;
			}

			if (availableRibbons.getModifier(SandmasteryAttributes.OVERMASTERY_ID) == null)
			{
				availableRibbons.addPermanentModifier(new AttributeModifier(
						SandmasteryAttributes.OVERMASTERY_ID,
						ribbons,
						AttributeModifier.Operation.ADD_VALUE
				));
				context.getSource().sendSuccess(() -> Component.literal(String.format("Filled overmastery slot 1 with %d ribbons for %s", ribbons, player.getName().getString())), true);
			}
			else if (availableRibbons.getModifier(SandmasteryAttributes.OVERMASTERY_SECONDARY_ID) == null)
			{
				availableRibbons.addPermanentModifier(new AttributeModifier(
						SandmasteryAttributes.OVERMASTERY_SECONDARY_ID,
						ribbons,
						AttributeModifier.Operation.ADD_VALUE
				));
				context.getSource().sendSuccess(() -> Component.literal(String.format("Filled overmastery slot 2 with %d ribbons for %s", ribbons, player.getName().getString())), true);
			}
			else
			{
				context.getSource().sendFailure(Component.literal("Both overmastery slots are filled; use clear_overmastery first"));
				return 0;
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
