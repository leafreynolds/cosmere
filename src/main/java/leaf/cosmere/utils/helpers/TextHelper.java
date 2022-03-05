/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

public class TextHelper
{
    public static TextComponent getPlayerTextObject(ServerWorld world, UUID id)
    {
        return getPlayerTextObject(world.getServer(), id);
    }

    public static TextComponent getPlayerTextObject(MinecraftServer server, UUID id)
    {
        String playerName = PlayerHelper.getPlayerName(id, server);
        return createTextComponentWithTip(playerName, id.toString());
    }

    public static TextComponent createTextComponentWithTip(String text, String tooltipText)
    {
        //Always surround tool tip items with brackets
        TextComponent textComponent = new StringTextComponent("[" + text + "]");
        textComponent.withStyle(style ->
        {
            return style.withColor(TextFormatting.GREEN)//color tool tip items green
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent(tooltipText))).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, tooltipText));
        });
        return textComponent;
    }

    public static TextComponent createTextWithTooltip(TranslationTextComponent translation, TranslationTextComponent description)
    {
        //Always surround tool tip items with brackets
        translation.withStyle(style ->
        {
            return style.withColor(TextFormatting.GREEN)//color tool tip items green
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, description)).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, translation.getKey()));
        });
        return translation;
    }

    public static TextComponent createTranslatedText(String s, Object... a)
    {
        return new TranslationTextComponent(s, a);
    }

    public static TextComponent createText(Object s)
    {
        return new StringTextComponent(s.toString());
    }
}
