/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public class TextHelper
{
	public static BaseComponent getPlayerTextObject(ServerLevel world, UUID id)
	{
		return getPlayerTextObject(world.getServer(), id);
	}

	public static BaseComponent getPlayerTextObject(MinecraftServer server, UUID id)
	{
		String playerName = PlayerHelper.getPlayerName(id, server);
		return createTextComponentWithTip(playerName, id.toString());
	}

	public static BaseComponent createTextComponentWithTip(String text, String tooltipText)
	{
		//Always surround tool tip items with brackets
		BaseComponent textComponent = new TextComponent("[" + text + "]");
		textComponent.withStyle(style ->
		{
			return style.withColor(ChatFormatting.GREEN)//color tool tip items green
					.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(tooltipText))).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, tooltipText));
		});
		return textComponent;
	}

	public static BaseComponent createTextWithTooltip(TranslatableComponent translation, TranslatableComponent description)
	{
		//Always surround tool tip items with brackets
		translation.withStyle(style ->
		{
			return style.withColor(ChatFormatting.GREEN)//color tool tip items green
					.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, description)).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, translation.getKey()));
		});
		return translation;
	}

	public static BaseComponent createTranslatedText(String s, Object... a)
	{
		return new TranslatableComponent(s, a);
	}

	public static BaseComponent createText(Object s)
	{
		return new TextComponent(s.toString());
	}
}
