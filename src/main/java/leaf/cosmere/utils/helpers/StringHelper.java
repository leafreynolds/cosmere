/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class StringHelper
{

	public static String fixCapitalisation(String text)
	{
		String original = text.trim().replace("    ", "").replace("_", " ").replace("/", ".");
		String output = Arrays.stream(original.split("\\s+")).map(t -> t.substring(0, 1).toUpperCase() + t.substring(1)).collect(Collectors.joining(" "));
		return output;
	}

	//Basically the opposite, make string suitable for path
	public static String fixPath(String text)
	{
		String output = text.trim().toLowerCase(Locale.ROOT).replace("    ", "").replace(" ", "_").replace("/", ".");
		return output;
	}
}
