/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api.math;

import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

//Unity's vector3 code is a great resource for vector math
//https://github.com/Unity-Technologies/UnityCsReference/blob/master/Runtime/Export/Math/Vector3.cs
public class VectorHelper
{
	public static final float kEpsilon = 0.00001F;

	// Makes this vector have a magnitude of 1.
	public static Vec3i Normalize(Vec3i vec)
	{
		float mag = Magnitude(vec);
		if (mag > kEpsilon)
		{
			vec = divide(vec, mag);
		}
		else
		{
			vec = Vec3i.ZERO;
		}

		return vec;
	}

	//
	public static float Magnitude(Vec3i vector)
	{
		return Mth.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY() + vector.getZ() * vector.getZ());
	}


	public static Vec3i divide(Vec3i a, float d)
	{
		return new Vec3i(a.getX() / d, a.getY() / d, a.getZ() / d);
	}


	// Moves a point /current/ in a straight line towards a /target/ point.
	public static Vec3 moveTowards(Vec3 current, Vec3i target, float maxDistanceDelta)
	{
		// avoid vector ops because current scripting backends are terrible at inlining
		double toVector_x = target.getX() - current.x;
		double toVector_y = target.getY() - current.y;
		double toVector_z = target.getZ() - current.z;

		double sqdist = toVector_x * toVector_x + toVector_y * toVector_y + toVector_z * toVector_z;

		if (sqdist == 0 || (maxDistanceDelta >= 0 && sqdist <= maxDistanceDelta * maxDistanceDelta))
		{
			return new Vec3(target.getX(), target.getY(), target.getZ());
		}
		float dist = (float) Math.sqrt(sqdist);

		return new Vec3(current.x + toVector_x / dist * maxDistanceDelta,
				current.y + toVector_y / dist * maxDistanceDelta,
				current.z + toVector_z / dist * maxDistanceDelta);
	}


	// Moves a point /current/ in a straight line towards a /target/ point.
	public static Vec3 getDirection(Vec3 target, Vec3 current, float multiplicationFactor)
	{
		double toVector_x = target.x() - current.x;
		double toVector_y = target.y() - current.y;
		double toVector_z = target.z() - current.z;

		return new Vec3(
				toVector_x * multiplicationFactor,
				toVector_y * multiplicationFactor,
				toVector_z * multiplicationFactor);
	}


	// Returns a copy of /vector/ with its magnitude clamped to /maxLength/.
	public static Vec3 ClampMagnitude(Vec3 vector, float maxLength)
	{
		double sqrMag = SqrMagnitude(vector);
		if (sqrMag > maxLength * maxLength)
		{
			float mag = (float) Math.sqrt(sqrMag);
			//these intermediate variables force the intermediate result to be
			//of float precision. without this, the intermediate result can be of higher
			//precision, which changes behavior.
			double normalized_x = vector.x / mag;
			double normalized_y = vector.y / mag;
			double normalized_z = vector.z / mag;
			return new Vec3(normalized_x * maxLength,
					normalized_y * maxLength,
					normalized_z * maxLength);
		}
		return vector;
	}


	public static double SqrMagnitude(Vec3 vector)
	{
		return vector.x * vector.x + vector.y * vector.y + vector.z * vector.z;
	}

}
