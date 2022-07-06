/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.commands.arguments.ManifestationsArgumentType;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.ManifestationRegistry;
import leaf.cosmere.utils.helpers.EntityHelper;
import leaf.cosmere.utils.helpers.TextHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class ManifestationCommand extends ModCommand
{

	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		return Command.SINGLE_SUCCESS;
	}

	private static int check(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		Collection<ServerPlayer> players = getPlayers(context, 3);

		for (ServerPlayer player : players)
		{
			ReportPowersFoundOnPlayer(context, player);
		}

		return Command.SINGLE_SUCCESS;
	}

	private static void ReportPowersFoundOnPlayer(CommandContext<CommandSourceStack> context, ServerPlayer player)
	{
		SpiritwebCapability.get(player).ifPresent(spiritweb ->
		{
			CommandSourceStack source = context.getSource();

			MutableComponent powersFound = Component.translatable(Constants.Strings.POWERS_FOUND, TextHelper.getPlayerTextObject(player.getLevel(), player.getUUID()));

			final MutableComponent leftBracketTextComponent = Component.literal("[");
			final MutableComponent rightBracketTextComponent = Component.literal("]");
			final MutableComponent space = Component.literal(" ");

			//figure out which manifestations a player has
			for (AManifestation manifestation : spiritweb.getAvailableManifestations())
			{
				powersFound.append(leftBracketTextComponent);
				powersFound.append(TextHelper.createTextWithTooltip(
						manifestation.translation(),
						manifestation.description()));
				powersFound.append(rightBracketTextComponent);
				powersFound.append(space);
			}
			source.sendSuccess(powersFound, true);
		});
	}

	private static int clear(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		Collection<ServerPlayer> players = getPlayers(context, 3);

		for (ServerPlayer player : players)
		{
			SpiritwebCapability.get(player).ifPresent(iSpiritweb ->
			{
				CommandSourceStack source = context.getSource();
				iSpiritweb.clearManifestations();
				iSpiritweb.syncToClients(null);
				MutableComponent playerTextObject = TextHelper.getPlayerTextObject(context.getSource().getLevel(), player.getUUID());
				source.sendSuccess(Component.translatable(Constants.Strings.POWER_SET_SUCCESS, playerTextObject), false);
			});
		}

		return Command.SINGLE_SUCCESS;
	}

	private static int reroll(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		Collection<ServerPlayer> players = getPlayers(context, 3);

		for (ServerPlayer player : players)
		{
			SpiritwebCapability.get(player).ifPresent(iSpiritweb ->
			{
				CommandSourceStack source = context.getSource();
				iSpiritweb.clearManifestations();
				EntityHelper.giveEntityStartingManifestation(player, (SpiritwebCapability) iSpiritweb);
				//set to none so that it auto updates to the new available ones on sync
				iSpiritweb.setSelectedManifestation(ManifestationRegistry.NONE.get());
				iSpiritweb.syncToClients(null);
				MutableComponent playerTextObject = TextHelper.getPlayerTextObject(player.getLevel(), player.getUUID());
				source.sendSuccess(Component.translatable(Constants.Strings.POWER_SET_SUCCESS, playerTextObject), false);
				ReportPowersFoundOnPlayer(context, player);
			});
		}


		return Command.SINGLE_SUCCESS;
	}


	private static int give(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		Collection<ServerPlayer> players = getPlayers(context, 4);

		for (ServerPlayer player : players)
		{
			CommandSourceStack source = context.getSource();
			AManifestation manifestation = context.getArgument("manifestation", AManifestation.class);

			MutableComponent playerText = TextHelper.getPlayerTextObject(player.getLevel(), player.getUUID());

			MutableComponent manifestationText = TextHelper.createTextWithTooltip(manifestation.translation(), manifestation.description());

			if (manifestation == null)
			{
				source.sendFailure(Component.translatable(Constants.Strings.POWER_SET_FAIL, playerText, manifestationText));
				return 0;
			}
			SpiritwebCapability.get(player).ifPresent((spiritweb) ->
			{
				//todo config ability strength
				spiritweb.giveManifestation(manifestation, 10);
				source.sendSuccess(Component.translatable(Constants.Strings.POWER_SET_SUCCESS, playerText, manifestationText), false);
				ReportPowersFoundOnPlayer(context, player);
				spiritweb.syncToClients(null);
			});
		}
		return Command.SINGLE_SUCCESS;
	}

	private static int remove(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		Collection<ServerPlayer> players = getPlayers(context, 4);

		for (ServerPlayer player : players)
		{
			CommandSourceStack source = context.getSource();
			AManifestation manifestation = context.getArgument("manifestation", AManifestation.class);

			MutableComponent playerText = TextHelper.getPlayerTextObject(source.getLevel(), player.getUUID());

			MutableComponent manifestationText = TextHelper.createTextWithTooltip(manifestation.translation(), manifestation.description());

			if (manifestation == null)
			{
				source.sendFailure(Component.translatable(Constants.Strings.POWER_SET_FAIL, playerText, manifestationText));
				return 0;
			}
			SpiritwebCapability.get(player).ifPresent((spiritweb) ->
			{
				spiritweb.removeManifestation(manifestation);
				spiritweb.syncToClients(null);
				source.sendSuccess(Component.translatable(Constants.Strings.POWER_SET_SUCCESS, playerText, manifestationText), false);
				ReportPowersFoundOnPlayer(context, player);
			});
		}
		return Command.SINGLE_SUCCESS;
	}

	public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		return Commands.literal("powers")
				.then(Commands.literal("check")
						.executes(ManifestationCommand::check)
						.then(Commands.argument("target", EntityArgument.players())
								.requires(context -> context.hasPermission(2))
								.executes(ManifestationCommand::check)))
                .then(Commands.literal("clear")
		                .requires(context -> context.hasPermission(2))
                        .executes(ManifestationCommand::clear)
		                .then(Commands.argument("target", EntityArgument.players())
				                .executes(ManifestationCommand::clear)))
				.then(Commands.literal("reroll")
						.requires(context -> context.hasPermission(2))
						.executes(ManifestationCommand::reroll)
						.then(Commands.argument("target", EntityArgument.players())
								.executes(ManifestationCommand::reroll)))
				.then(Commands.literal("give")
						.requires(context -> context.hasPermission(2))
						.then(Commands.argument("manifestation", ManifestationsArgumentType.createArgument())
								.executes(ManifestationCommand::give)
								.then(Commands.argument("target", EntityArgument.players())
										.executes(ManifestationCommand::give))))
				.then(Commands.literal("remove")
						.requires(context -> context.hasPermission(2))
						.then(Commands.argument("manifestation", ManifestationsArgumentType.createArgument())
								.executes(ManifestationCommand::remove)
								.then(Commands.argument("target", EntityArgument.players())
										.executes(ManifestationCommand::remove))))
				; // end add
	}
}