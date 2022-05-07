/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

import static leaf.cosmere.registry.ManifestationRegistry.getManifestations;

public class CommandHelper
{
	public static SuggestionsBuilder addManifestationNamesWithTooltip(SuggestionsBuilder builder)
	{
		Map<ResourceLocation, String> map = getManifestations();
		if (!map.isEmpty())
		{
			map.entrySet().forEach(entry ->
			{
				builder.suggest(entry.getKey().toString(), new TranslatableComponent(entry.getValue()).withStyle((style) -> style.withColor(ChatFormatting.GREEN)));
			});
		}
		builder.buildFuture();
		return builder;
	}
}
