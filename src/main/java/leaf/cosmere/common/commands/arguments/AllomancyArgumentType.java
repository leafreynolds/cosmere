package leaf.cosmere.common.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import leaf.cosmere.api.Constants;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class AllomancyArgumentType implements ArgumentType<Manifestation>
{
	private static final Collection<String> EXAMPLES = Stream.of(
			"allomancy:" + Metals.MetalType.STEEL.getName()
	).toList();

	public static final DynamicCommandExceptionType INVALID_MANIFESTATION_EXCEPTION =
			new DynamicCommandExceptionType((manifestation) ->
					Component.translatable(Constants.Strings.POWER_INVALID, manifestation));

	public static SuggestionsBuilder addAllomancyNamesWithTooltip(SuggestionsBuilder builder)
	{
		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			if (manifestation.getManifestationType().equals(Manifestations.ManifestationTypes.ALLOMANCY) || manifestation.getName().equals("none"))
			{
				builder.suggest(manifestation.getRegistryName().toString(), Component.translatable(manifestation.getTranslationKey()).withStyle((style) -> style.withColor(ChatFormatting.GREEN)));
			}
		}

		builder.buildFuture();
		return builder;
	}

	public static AllomancyArgumentType createArgument()
	{
		return new AllomancyArgumentType();
	}

	@Override
	public Manifestation parse(StringReader reader) throws CommandSyntaxException
	{
		ResourceLocation location = ResourceLocation.read(reader);
		Manifestation manifestation = CosmereAPI.manifestationRegistry().getValue(location);
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
		       ? SharedSuggestionProvider.suggest(Collections.emptyList(), addAllomancyNamesWithTooltip(builder))
		       : Suggestions.empty();
	}

	@Override
	public Collection<String> getExamples()
	{
		return EXAMPLES;
	}
}
