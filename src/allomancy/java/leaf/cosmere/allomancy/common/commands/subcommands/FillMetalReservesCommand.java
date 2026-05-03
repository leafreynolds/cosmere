/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.allomancy.common.commands.subcommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import leaf.cosmere.allomancy.common.capabilities.AllomancySpiritwebSubmodule;
import leaf.cosmere.allomancy.common.config.AllomancyConfigs;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.commands.subcommands.ModCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class FillMetalReservesCommand extends ModCommand
{
	public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		return Commands.literal("fill_metal_reserves")
				.requires(context -> context.hasPermission(2))
				.executes(FillMetalReservesCommand::fillReserves)
				.then(Commands.argument("target", EntityArgument.players())
						.requires(context -> context.hasPermission(2))
						.executes(FillMetalReservesCommand::fillReserves));
	}

	private static int fillReserves(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		Collection<ServerPlayer> players = getPlayers(context, 3);

		for (ServerPlayer player : players)
		{
			SpiritwebCapability.get(player).ifPresent((spiritweb) ->
			{
				AllomancySpiritwebSubmodule asm = AllomancySpiritwebSubmodule.getSubmodule(spiritweb);

				for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
				{
					//I know it's not feruchemy, but it's the same list of metals
					if (metalType.hasFeruchemicalEffect())
					{
						final int metalTypeID = metalType.getID();
						final Manifestation manifestation = Manifestations.ManifestationTypes.ALLOMANCY.getManifestation(metalTypeID);
						if (spiritweb.hasManifestation(manifestation))
						{
							int amount = metalType.getAllomancyBurnTimeSeconds() * AllomancyConfigs.SERVER.MAX_INGESTIBLE_METAL.get();
							asm.adjustIngestedMetal(metalType, amount, true);
						}
					}
				}
				spiritweb.syncToClients(null);
			});
		}

		return SINGLE_SUCCESS;
	}

	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		return SINGLE_SUCCESS;
	}

}
