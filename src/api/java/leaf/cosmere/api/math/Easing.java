/*
 * File updated ~ 5 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.api.math;

public class Easing
{

	public static float easeInQuad(float x)
	{
		return x * x;
	}

	public static float easeOutQuad(float x)
	{
		return 1 - (1 - x) * (1 - x);
	}

	public static float easeInCubic(float x)
	{
		return x * x * x;
	}

	public static float easeOutCubic(float x)
	{
		return (float) (1 - Math.pow(1 - x, 3));
	}
}
