/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.constants.Metals;

import java.awt.*;

public interface IHasMetalType extends IHasColour
{
	Metals.MetalType getMetalType();

	default Color getColour()
	{
		return getMetalType().getColor();
	}

	default int getColourValue()
	{
		return getColour().getRGB();
	}
}
