/*
 * File updated ~ 18 - 8 - 2023 ~ Leaf
 */

package leaf.cosmere.api.math;

import net.minecraft.util.Mth;

import java.util.Random;

public class MathHelper
{
	public final static Random RANDOM = new Random();

	//inclusive
	public static int randomInt(int min, int max)
	{
		return RANDOM.nextInt((max - min) + 1) + min;
	}

	// 1 out of how many.
	public static boolean chance(int chance)
	{
		return MathHelper.randomInt(1, chance) % chance == 0;
	}

	public static boolean randomBool()
	{
		return RANDOM.nextBoolean();
	}

	public static float InverseLerp(float a, float b, float value)
	{
		if (a != b)
		{
			return clamp01((value - a) / (b - a));
		}
		else
		{
			return 0.0f;
		}
	}

	public static float clamp01(final float f)
	{
		return Mth.clamp(f, 0.0f, 1.0f);
	}


	public static boolean inTriangle(
			final double x1,
			final double y1,
			final double x2,
			final double y2,
			final double x3,
			final double y3,
			final double x,
			final double y)
	{
		final double ab = (x1 - x) * (y2 - y) - (x2 - x) * (y1 - y);
		final double bc = (x2 - x) * (y3 - y) - (x3 - x) * (y2 - y);
		final double ca = (x3 - x) * (y1 - y) - (x1 - x) * (y3 - y);
		return sign(ab) == sign(bc) && sign(bc) == sign(ca);
	}

	public static int sign(final double n)
	{
		return n > 0 ? 1 : -1;
	}
}
