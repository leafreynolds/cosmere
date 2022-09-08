/*
 * File created ~ 9 - 4 - 2022 ~ Leaf
 */

package leaf.cosmere.items.gems;

import leaf.cosmere.constants.Roshar;
import leaf.cosmere.items.IHasColour;

import java.awt.*;

public interface IHasGemstoneType extends IHasColour
{
	Roshar.Gemstone getType();

	default Color getColour()
	{
		return getType().getColor();
	}

	default int getColourValue()
	{
		return getColour().getRGB();
	}
}
