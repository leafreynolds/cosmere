/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api;

import java.awt.*;

public interface IHasColour
{
	Color getColour();

	default int getColourValue()
	{
		return getColour().getRGB();
	}
}
