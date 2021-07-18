/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringHelper
{

    public static String fixCapitalisation(String text)
    {
        String original = text.trim().replace("    ", "").replace("_", " ").replace("/", ".");
        String output = Arrays.stream(original.split("\\s+")).map(t -> t.substring(0, 1).toUpperCase() + t.substring(1)).collect(Collectors.joining(" "));
        return output;
    }
}
