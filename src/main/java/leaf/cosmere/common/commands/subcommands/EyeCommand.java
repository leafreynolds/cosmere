/*
 * File updated ~ 15 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.common.commands.subcommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import leaf.cosmere.api.Constants;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class EyeCommand extends ModCommand
{
	//todo move this to hemalurgy?

	public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		return Commands.literal("eyeHeight")
				.then(Commands.argument("height", IntegerArgumentType.integer(0, 3))
						.executes(EyeCommand::setEyeHeight)
						.then(Commands.argument("target", EntityArgument.players())
								.requires(context -> context.hasPermission(2))
								.executes(EyeCommand::setEyeHeight)));
	}

	private static int setEyeHeight(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		Collection<ServerPlayer> players = getPlayers(context, 3);
		int value = IntegerArgumentType.getInteger(context, "height");

		for (ServerPlayer player : players)
		{
			SpiritwebCapability.get(player).ifPresent((spiritweb) ->
			{
				CompoundNBTHelper.setInt(spiritweb.getCompoundTag(), "eye_height", -value);
				spiritweb.syncToClients(null);
			});
		}

		context.getSource().sendSuccess(TextHelper.createTranslatedText(Constants.Strings.SET_EYE_HEIGHT_SUCCESS, value), true);

		return SINGLE_SUCCESS;
	}

	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		return SINGLE_SUCCESS;
	}

}