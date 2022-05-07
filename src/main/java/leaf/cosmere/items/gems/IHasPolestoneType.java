/*
 * File created ~ 9 - 4 - 2022 ~ Leaf
 */

package leaf.cosmere.items.gems;

import leaf.cosmere.constants.Roshar;
import leaf.cosmere.items.IHasColour;

import java.awt.*;

public interface IHasPolestoneType extends IHasColour
{
	Roshar.Polestone getPolestoneType();

	default Color getColour()
	{
		return getPolestoneType().getColor();
	}

	default int getColourValue()
	{
		return getColour().getRGB();
	}
}
