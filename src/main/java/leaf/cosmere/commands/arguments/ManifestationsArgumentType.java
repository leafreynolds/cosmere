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
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.ManifestationRegistry;
import leaf.cosmere.utils.helpers.CommandHelper;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class ManifestationsArgumentType implements ArgumentType<AManifestation>
{
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
		return SharedSuggestionProvider.suggest(Collections.emptyList(), CommandHelper.addManifestationNamesWithTooltip(builder));
	}

}