/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

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
                builder.suggest(entry.getKey().toString(), new TranslationTextComponent(entry.getValue()).withStyle((style) -> style.withColor(TextFormatting.GREEN)));
            });
        }
        builder.buildFuture();
        return builder;
    }
}
