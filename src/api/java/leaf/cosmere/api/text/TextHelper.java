/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api.text;

import leaf.cosmere.api.helpers.PlayerHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public class TextHelper
{
	public static MutableComponent getPlayerTextObject(ServerLevel world, UUID id)
	{
		return getPlayerTextObject(world.getServer(), id);
	}

	public static MutableComponent getPlayerTextObject(MinecraftServer server, UUID id)
	{
		String playerName = PlayerHelper.getPlayerName(id, server);
		return createTextComponentWithTip(playerName, id.toString());
	}

	public static MutableComponent createTextComponentWithTip(String text, String tooltipText)
	{
		//Always surround tool tip items with brackets
		MutableComponent textComponent = Component.literal("[" + text + "]");
		textComponent.withStyle(style -> style.applyFormat(ChatFormatting.GREEN)//color tool tip items green
				.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(tooltipText)))
				.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, tooltipText)));
		return textComponent;
	}

	public static MutableComponent createTextWithTooltip(MutableComponent translation, MutableComponent description)
	{
		//Always surround tool tip items with brackets
		translation.withStyle(style ->
		{
			return style.withColor(ChatFormatting.GREEN)//color tool tip items green
					.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, description));
		});
		return translation;
	}

	public static MutableComponent createTranslatedText(String s, Object... a)
	{
		return Component.translatable(s, a);
	}

	public static MutableComponent createText(Object s)
	{
		return Component.literal(s.toString());
	}
}
