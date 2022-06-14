/*
 * File created ~ 14 - 5 - 2022 ~ Leaf
 */

package leaf.cosmere.datagen.patchouli;

import java.util.Locale;

public class PatchouliTextFormat
{
	private static final StringBuilder s_stringBuilder = new StringBuilder();

	public static String Item(String input)
	{
		return format(input, "$(item)");
	}

	public static String Thing(String input)
	{
		return format(input, "$(thing)");
	}

	public static String Obfuscate(String input)
	{
		return format(input, "$(obf)");
	}

	public static String Italics(String input)
	{
		return format(input, "$(italics)");
	}

	public static String Bold(String input)
	{
		return format(input, "$(bold)");
	}

	public static String Strikethrough(String input)
	{
		return format(input, "$(strike)");
	}

	public static String LinkEntry(String input, String entryToLinkTo)
	{
		//example
		//$(l:cosmere:allomancy/allomantic_aluminum)aluminum$()
		return format(input, "$(l:%s)".formatted(entryToLinkTo.toLowerCase(Locale.ROOT)));
	}


	private static String format(String input, String formatCode)
	{
		s_stringBuilder.append(formatCode);
		s_stringBuilder.append(input);
		if (!input.contains("$()"))
		{
			//Only need to clear once
			s_stringBuilder.append("$()");
		}

		final String s = s_stringBuilder.toString();
		s_stringBuilder.setLength(0);
		return s;
	}


}
