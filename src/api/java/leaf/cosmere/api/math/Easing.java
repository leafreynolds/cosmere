/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api.math;

public class Easing
{

	public static float easeOutQuad(float x)
	{
		return 1 - (1 - x) * (1 - x);
	}
}
