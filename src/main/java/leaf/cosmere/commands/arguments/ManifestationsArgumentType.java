/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ManifestationsArgumentType implements ArgumentType<AManifestation>
{
	private static final Collection<String> EXAMPLES = Stream.of(
					"cosmere:" + Metals.MetalType.STEEL.getAllomancyRegistryName(),
					"cosmere:" + Metals.MetalType.IRON.getFeruchemyRegistryName())
			.toList();

	@Override
	public Collection<String> getExamples()
	{
		return EXAMPLES;
	}

	public static final DynamicCommandExceptionType INVALID_MANIFESTATION_EXCEPTION =
			new DynamicCommandExceptionType((manifestation) ->
					new TranslatableComponent(Constants.Strings.POWER_INVALID, manifestation));

	public static ManifestationsArgumentType createArgument()
	{
		return new ManifestationsArgumentType();
	}

	@Override
	public AManifestation parse(StringReader reader) throws CommandSyntaxException
	{
		ResourceLocation location = ResourceLocation.read(reader);
		AManifestation manifestation = ManifestationRegistry.MANIFESTATION_REGISTRY.get().getValue(location);
		if (manifestation != null)
		{
			return manifestation;
		}
		throw INVALID_MANIFESTATION_EXCEPTION.create(location);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		return context.getSource() instanceof SharedSuggestionProvider
		       ? SharedSuggestionProvider.suggest(Collections.emptyList(), addManifestationNamesWithTooltip(builder))
		       : Suggestions.empty();
	}

	public static SuggestionsBuilder addManifestationNamesWithTooltip(SuggestionsBuilder builder)
	{
		Map<ResourceLocation, String> map = ManifestationRegistry.getManifestations();
		if (!map.isEmpty())
		{
			map.forEach((key, value) -> builder.suggest(key.toString(), new TranslatableComponent(value).withStyle((style) -> style.withColor(ChatFormatting.GREEN))));
		}
		builder.buildFuture();
		return builder;
	}
}