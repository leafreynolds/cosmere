package leaf.cosmere.common.commands.subcommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.commands.arguments.ManifestationsArgumentType;
import leaf.cosmere.common.config.CosmereConfigs;
import leaf.cosmere.common.eventHandlers.EntityEventHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static leaf.cosmere.common.commands.subcommands.ManifestationCommand.ReportPowersFoundOnPlayer;

public class ChooseMetalbornPowersCommand extends ModCommand
{
	private static final HashMap<UUID, CommandQueueItem> commandConfirmationQueue = new HashMap<>();

	public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		return Commands.literal("choose_metalborn_powers")
				.requires(CommandSourceStack::isPlayer)
				.then(Commands.argument("allomanticPower", ManifestationsArgumentType.createArgument())
						.requires(CommandSourceStack::isPlayer)
						.executes(ChooseMetalbornPowersCommand::addMetalbornPowers)
						.then(Commands.argument("feruchemicalPower", ManifestationsArgumentType.createArgument())
								.requires(CommandSourceStack::isPlayer)
								.executes(ChooseMetalbornPowersCommand::addMetalbornPowers)))
				.then(Commands.literal("confirm")
						.requires(CommandSourceStack::isPlayer)
						.executes(ChooseMetalbornPowersCommand::confirmMetalbornPowers))
				.then(Commands.literal("random")
						.requires(CommandSourceStack::isPlayer)
						.executes(ChooseMetalbornPowersCommand::random));
	}

	private static int addMetalbornPowers(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		CommandSourceStack source = context.getSource();
		ServerPlayer player = source.getPlayerOrException();
		AtomicBoolean isInitialized = new AtomicBoolean(false);

		SpiritwebCapability.get(player).ifPresent(spiritweb ->
		{
			SpiritwebCapability spiritwebCap = (SpiritwebCapability) spiritweb;
			isInitialized.set(spiritwebCap.hasBeenInitialized());
		});

		if (CosmereConfigs.SERVER_CONFIG.ALLOW_METALBORN_CHOICE.get() && !isInitialized.get())
		{
			// cosmere:none is also an acceptable input
			Manifestation allomanticPower = context.getArgument("allomanticPower", Manifestation.class);
			Manifestation feruchemicalPower = context.getArgument("feruchemicalPower", Manifestation.class);

			boolean allomanticIsValid = allomanticPower.getManifestationType().equals(Manifestations.ManifestationTypes.ALLOMANCY) || allomanticPower.getManifestationType().equals(Manifestations.ManifestationTypes.NONE);
			boolean feruchemicalIsValid = feruchemicalPower.getManifestationType().equals(Manifestations.ManifestationTypes.FERUCHEMY) || feruchemicalPower.getManifestationType().equals(Manifestations.ManifestationTypes.NONE);
			boolean isCompoundingPair = allomanticPower.getName().equals(feruchemicalPower.getName())
					|| (allomanticPower.getName().equals(Metals.MetalType.ELECTRUM.getName()) && feruchemicalPower.getName().equals(Metals.MetalType.ATIUM.getName()))
					|| (allomanticPower.getName().equals(Metals.MetalType.ATIUM.getName()) && feruchemicalPower.getName().equals(Metals.MetalType.ELECTRUM.getName()));

			if (allomanticIsValid && feruchemicalIsValid && !isCompoundingPair)
			{
				CommandQueueItem queueItem = new CommandQueueItem(allomanticPower, feruchemicalPower, false);
				commandConfirmationQueue.put(player.getUUID(), queueItem);

				source.sendSystemMessage(Component.literal("You have chosen §aallomantic " + allomanticPower.getName() + " §fand §aferuchemical " + feruchemicalPower.getName()));  // todo translatable string

				sendConfirmationMessage(source);
			}
			else
			{
				if (!allomanticIsValid)
				{
					source.sendFailure(Component.literal("Invalid argument; argument 1 is not allomantic or none"));
				}
				if (!feruchemicalIsValid)
				{
					source.sendFailure(Component.literal("Invalid argument; argument 2 is not feruchemical or none"));
				}
				if (isCompoundingPair)
				{
					source.sendFailure(Component.literal("Cannot choose compounding pair"));
				}
			}
		}
		else
		{
			if (!CosmereConfigs.SERVER_CONFIG.ALLOW_METALBORN_CHOICE.get())
			{
				source.sendFailure(Component.literal("Server config does not allow non-random metalborn choice"));   // todo translatable string
			}
			else if (isInitialized.get())
			{
				source.sendFailure(Component.literal("Powers already chosen; cannot choose again"));   // todo translatable string
			}
		}

		return SINGLE_SUCCESS;
	}

	private static int confirmMetalbornPowers(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		CommandSourceStack source = context.getSource();
		ServerPlayer player = source.getPlayerOrException();

		if (commandConfirmationQueue.containsKey(player.getUUID()))
		{
			CommandQueueItem queueItem = commandConfirmationQueue.get(player.getUUID());

			SpiritwebCapability.get(player).ifPresent(spiritweb ->
			{
				SpiritwebCapability spiritwebCap = (SpiritwebCapability) spiritweb;

				if (!queueItem.randomPowers)
				{
					// always give natural level manifestation; do nothing if cosmere:none is selected
					if (!queueItem.allomanticPower.getManifestationType().equals(Manifestations.ManifestationTypes.NONE))
					{
						spiritweb.giveManifestation(queueItem.allomanticPower, 9);
						source.sendSuccess(Component.literal("Successfully added allomantic " + queueItem.allomanticPower.getName() + " to " + player.getName().getString()), false);   // todo localisation string

						// give player metal vial according to what they chose
						spiritwebCap.getSubmodule(Manifestations.ManifestationTypes.ALLOMANCY).GiveStartingItem(player, queueItem.allomanticPower);
					}
					if (!queueItem.feruchemicalPower.getManifestationType().equals(Manifestations.ManifestationTypes.NONE))
					{
						spiritweb.giveManifestation(queueItem.feruchemicalPower, 9);
						source.sendSuccess(Component.literal("Successfully added feruchemical " + queueItem.feruchemicalPower.getName() + " to " + player.getName().getString()), false);   // todo localisation string

						// give player metal vial according to what they chose
						spiritwebCap.getSubmodule(Manifestations.ManifestationTypes.FERUCHEMY).GiveStartingItem(player, queueItem.feruchemicalPower);
					}

					// adds non-metalborn powers
					EntityEventHandler.addOtherPowers(spiritwebCap);
				}
				else
				{
					EntityEventHandler.giveEntityStartingManifestation(player, spiritwebCap);
					spiritwebCap.setHasBeenInitialized();
				}

				spiritwebCap.setHasBeenInitialized();

				ReportPowersFoundOnPlayer(context, player);
				spiritweb.syncToClients(null);
			});

			commandConfirmationQueue.remove(player.getUUID());
		}
		else
		{
			context.getSource().sendFailure(Component.literal("No powers selected."));
		}

		return SINGLE_SUCCESS;
	}

	private static int random(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		CommandSourceStack source = context.getSource();

		if (source.getEntity() instanceof ServerPlayer player)
		{
			CommandQueueItem queueItem = new CommandQueueItem(null, null, true);
			commandConfirmationQueue.put(player.getUUID(), queueItem);

			source.sendSystemMessage(Component.literal("You have chosen to receive §arandom powers"));
			sendConfirmationMessage(source);
		}

		return SINGLE_SUCCESS;
	}

	private static void sendConfirmationMessage(CommandSourceStack source)
	{
		// todo translatable string
		String command = "/cosmere choose_metalborn_powers confirm";
		MutableComponent confirmComponent = Component.literal("§6Click ");
		confirmComponent.append(TextHelper.createTextWithTooltip(
				Component.literal("§a§nhere§r ").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))),
				Component.literal("Confirm")));
		confirmComponent.append(Component.literal("§6to confirm power choice"));

		source.sendSuccess(confirmComponent, false);
	}

	@Override
	public int run(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException
	{
		return SINGLE_SUCCESS;
	}

	private record CommandQueueItem(Manifestation allomanticPower, Manifestation feruchemicalPower, boolean randomPowers) {}
}
