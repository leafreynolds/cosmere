/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.constants.Roshar;

import java.awt.*;

public interface IHasGemType extends IHasColour
{
	Roshar.Polestone getGemType();

	default Color getColour()
	{
		return getGemType().getColor();
	}

	default int getColourValue()
	{
		return getColour().getRGB();
	}
}
