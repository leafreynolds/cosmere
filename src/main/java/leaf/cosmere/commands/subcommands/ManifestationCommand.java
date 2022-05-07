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
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

import static leaf.cosmere.constants.Constants.Strings.POWERS_FOUND;

public class ManifestationCommand extends ModCommand
{

	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		return Command.SINGLE_SUCCESS;
	}

	private static int check(CommandContext<CommandSourceStack> context, ServerPlayer player)
	{
		SpiritwebCapability.get(player).ifPresent(spiritweb ->
		{
			CommandSourceStack source = context.getSource();

			TranslatableComponent powersFound = new TranslatableComponent(POWERS_FOUND, TextHelper.getPlayerTextObject(context.getSource().getLevel(), player.getUUID()));

			final BaseComponent leftBracketTextComponent = new TextComponent("[");
			final BaseComponent rightBracketTextComponent = new TextComponent("]");
			final TextComponent space = new TextComponent(" ");

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

		return Command.SINGLE_SUCCESS;
	}

	private static int clear(CommandContext<CommandSourceStack> context, ServerPlayer player)
	{
		SpiritwebCapability.get(player).ifPresent(iSpiritweb ->
		{
			CommandSourceStack source = context.getSource();
			iSpiritweb.clearManifestations();
			iSpiritweb.syncToClients(null);
			BaseComponent playerTextObject = TextHelper.getPlayerTextObject(context.getSource().getLevel(), player.getUUID());
			source.sendSuccess(new TranslatableComponent(Constants.Strings.POWER_SET_SUCCESS, playerTextObject), false);
		});

		return Command.SINGLE_SUCCESS;
	}

	private static int reroll(CommandContext<CommandSourceStack> context, ServerPlayer player)
	{
		SpiritwebCapability.get(player).ifPresent(iSpiritweb ->
		{
			CommandSourceStack source = context.getSource();
			iSpiritweb.clearManifestations();
			EntityHelper.giveEntityStartingManifestation(player, (SpiritwebCapability) iSpiritweb);
			//set to none so that it auto updates to the new available ones on sync
			iSpiritweb.setSelectedManifestation(ManifestationRegistry.NONE.get());
			iSpiritweb.syncToClients(null);
			BaseComponent playerTextObject = TextHelper.getPlayerTextObject(context.getSource().getLevel(), player.getUUID());
			source.sendSuccess(new TranslatableComponent(Constants.Strings.POWER_SET_SUCCESS, playerTextObject), false);
		});

		return Command.SINGLE_SUCCESS;
	}

	private static int set(CommandContext<CommandSourceStack> context, ServerPlayer player)
	{
		CommandSourceStack source = context.getSource();
		AManifestation manifestation = context.getArgument("manifestation", AManifestation.class);

		BaseComponent playerText = TextHelper.getPlayerTextObject(source.getLevel(), player.getUUID());

		BaseComponent manifestationText = TextHelper.createTextWithTooltip(manifestation.translation(), manifestation.description());

		if (player == null || manifestation == null)
		{
			source.sendFailure(new TranslatableComponent(Constants.Strings.POWER_SET_FAIL, playerText, manifestationText));
			return 0;
		}
		SpiritwebCapability.get(player).ifPresent((spiritweb) ->
		{
			spiritweb.clearManifestations();
			spiritweb.giveManifestation(manifestation.getManifestationType(), manifestation.getPowerID());
			spiritweb.syncToClients(null);
			source.sendSuccess(new TranslatableComponent(Constants.Strings.POWER_SET_SUCCESS, playerText, manifestationText), false);
		});
		return Command.SINGLE_SUCCESS;
	}

	private static int give(CommandContext<CommandSourceStack> context, ServerPlayer player)
	{
		CommandSourceStack source = context.getSource();
		AManifestation manifestation = context.getArgument("manifestation", AManifestation.class);

		BaseComponent playerText = TextHelper.getPlayerTextObject(source.getLevel(), player.getUUID());

		BaseComponent manifestationText = TextHelper.createTextWithTooltip(manifestation.translation(), manifestation.description());

		if (player == null || manifestation == null)
		{
			source.sendFailure(new TranslatableComponent(Constants.Strings.POWER_SET_FAIL, playerText, manifestationText));
			return 0;
		}
		SpiritwebCapability.get(player).ifPresent((spiritweb) ->
		{
			spiritweb.giveManifestation(manifestation.getManifestationType(), manifestation.getPowerID());
			source.sendSuccess(new TranslatableComponent(Constants.Strings.POWER_SET_SUCCESS, playerText, manifestationText), false);
			spiritweb.syncToClients(null);
		});
		return Command.SINGLE_SUCCESS;
	}

	private static int remove(CommandContext<CommandSourceStack> context, ServerPlayer player)
	{
		CommandSourceStack source = context.getSource();
		AManifestation manifestation = context.getArgument("manifestation", AManifestation.class);

		BaseComponent playerText = TextHelper.getPlayerTextObject(source.getLevel(), player.getUUID());

		BaseComponent manifestationText = TextHelper.createTextWithTooltip(manifestation.translation(), manifestation.description());

		if (player == null || manifestation == null)
		{
			source.sendFailure(new TranslatableComponent(Constants.Strings.POWER_SET_FAIL, playerText, manifestationText));
			return 0;
		}
		SpiritwebCapability.get(player).ifPresent((spiritweb) ->
		{
			spiritweb.removeManifestation(manifestation.getManifestationType(), manifestation.getPowerID());
			spiritweb.syncToClients(null);
			source.sendSuccess(new TranslatableComponent(Constants.Strings.POWER_SET_SUCCESS, playerText, manifestationText), false);
		});
		return Command.SINGLE_SUCCESS;
	}

	public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		return Commands.literal("powers")
				.requires(context -> context.hasPermission(2))
				.then(Commands.literal("check")
						.executes(context -> check(context, context.getSource().getPlayerOrException())))
/*                .then(Commands.literal("clear")
                        .executes(context -> clear(context, context.getSource().asPlayer())))
                  .then(Commands.literal("set")
                        .then(Commands.argument("manifestation", ManifestationsArgumentType.createArgument())
                                .executes(context -> set(context, context.getSource().asPlayer())))
                )*/
				.then(Commands.literal("reroll")
						.executes(context -> reroll(context, context.getSource().getPlayerOrException())))
				.then(Commands.literal("give")
						.then(Commands.argument("manifestation", ManifestationsArgumentType.createArgument())
								.executes(context -> give(context, context.getSource().getPlayerOrException()))))
				.then(Commands.literal("remove")
						.then(Commands.argument("manifestation", ManifestationsArgumentType.createArgument())
								.executes(context -> remove(context, context.getSource().getPlayerOrException()))))
				; // end add
	}

}