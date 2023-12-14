/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api;

import java.awt.*;

public interface IHasMetalType extends IHasColour
{
	Metals.MetalType getMetalType();

	default Color getColour()
	{
		return getMetalType().getColor();
	}

}
