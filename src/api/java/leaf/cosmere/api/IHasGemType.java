/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api;

import java.awt.*;

public interface IHasGemType extends IHasColour
{
	Roshar.Gemstone getGemType();

	default Color getColour()
	{
		return getGemType().getColor();
	}

}
