/*
 * File updated ~ 15 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.common.commands.subcommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import leaf.cosmere.api.Constants;
import leaf.cosmere.api.cosmereEffect.AttributeModifierInfo;
import leaf.cosmere.api.cosmereEffect.CosmereEffectInstance;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CosmereEffectCommand extends ModCommand
{
	public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		return Commands.literal("effects")
				.requires(context -> context.hasPermission(2))
				.then(Commands.literal("check")
						.executes(CosmereEffectCommand::checkEffects)
						.then(Commands.argument("target", EntityArgument.players())
								.requires(context -> context.hasPermission(2))
								.executes(CosmereEffectCommand::checkEffects)))
				.then(Commands.literal("clear")
						.requires(context -> context.hasPermission(2))
						.executes(CosmereEffectCommand::clear)
						.then(Commands.argument("target", EntityArgument.players())
								.executes(CosmereEffectCommand::clear)));
	}

	private static int checkEffects(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{

		Collection<ServerPlayer> players = getPlayers(context, 3);

		for (ServerPlayer player : players)
		{
			reportEffectsFoundOnPlayer(context, player);
		}

		return SINGLE_SUCCESS;
	}

	private static void reportEffectsFoundOnPlayer(CommandContext<CommandSourceStack> context, ServerPlayer player)
	{
		SpiritwebCapability.get(player).ifPresent(spiritweb ->
		{
			CommandSourceStack source = context.getSource();

			MutableComponent found = Component.translatable(Constants.Strings.EFFECTS_FOUND, TextHelper.getPlayerTextObject(player.serverLevel(), player.getUUID()));

			final MutableComponent leftBracketTextComponent = Component.literal("[");
			final MutableComponent rightBracketTextComponent = Component.literal("]");
			final MutableComponent space = Component.literal(" ");
			StringBuilder stringBuilder = new StringBuilder();

			//figure out which manifestations a player has
			for (Map.Entry<UUID, CosmereEffectInstance> entry : spiritweb.getEffects())
			{
				found.append(leftBracketTextComponent);
				final MutableComponent baseText = (MutableComponent) entry.getValue().getTextComponent();

				stringBuilder.append("Ticks Remaining: ").append(entry.getValue().getDuration()).append("\n");
				stringBuilder.append("Attribute Multiplier: x").append(entry.getValue().getStrength()).append("\n");

				for (Map.Entry<Attribute, AttributeModifierInfo> attributeEntry : entry.getValue().getEffect().getAttributeModifiers().entrySet())
				{
					final AttributeModifierInfo attributeModifierInfo = attributeEntry.getValue();

					final String attributeName = attributeModifierInfo.getAttribute().getDescriptionId();
					final double amount = attributeModifierInfo.getAmount();
					final AttributeModifier.Operation operation = attributeModifierInfo.getOperation();

					stringBuilder.append(attributeName).append(": ").append(amount).append(" : Operation: ").append(operation)
							.append("\n");
				}

				for (Map.Entry<Attribute, AttributeModifierInfo> attributeEntry : entry.getValue().getDynamicModifiers().entrySet())
				{
					final AttributeModifierInfo attributeModifierInfo = attributeEntry.getValue();

					final String attributeName = attributeModifierInfo.getAttribute().getDescriptionId();
					final double amount = attributeModifierInfo.getAmount();
					final AttributeModifier.Operation operation = attributeModifierInfo.getOperation();

					stringBuilder.append(attributeName).append(": ").append(amount).append(" : Operation: ").append(operation)
							.append("\n");
				}


				final String s = stringBuilder.toString();
				stringBuilder.setLength(0);

				final MutableComponent tooltip = Component.literal(s);

				found.append(TextHelper.createTextWithTooltip(
						baseText,
						tooltip));
				found.append(rightBracketTextComponent);
				found.append(space);
			}
			source.sendSuccess(() -> found, true);
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

				final Set<Map.Entry<UUID, CosmereEffectInstance>> playerEffects = iSpiritweb.getEffects();
				for (Map.Entry<UUID, CosmereEffectInstance> entry : playerEffects)
				{
					iSpiritweb.onEffectRemoved(entry.getValue());
				}
				playerEffects.clear();

				iSpiritweb.syncToClients(null);
				MutableComponent playerTextObject = TextHelper.getPlayerTextObject(context.getSource().getLevel(), player.getUUID());
				source.sendSuccess(() -> Component.translatable(Constants.Strings.EFFECTS_CLEAR, playerTextObject), false);
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