/*
 * File created ~ 25 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.helpers;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;

//Unity's vector3 code is a great resource for vector math
//https://github.com/Unity-Technologies/UnityCsReference/blob/master/Runtime/Export/Math/Vector3.cs
public class VectorHelper
{
    public static final float kEpsilon = 0.00001F;

    // Makes this vector have a magnitude of 1.
    public static Vector3i Normalize(Vector3i vec)
    {
        float mag = Magnitude(vec);
        if (mag > kEpsilon)
        {
            vec = divide(vec, mag);
        }
        else
        {
            vec = Vector3i.NULL_VECTOR;
        }

        return vec;
    }

    //
    public static float Magnitude(Vector3i vector)
    {
        return MathHelper.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY() + vector.getZ() * vector.getZ());
    }


    public static Vector3i divide(Vector3i a, float d)
    {
        return new Vector3i(a.getX() / d, a.getY() / d, a.getZ() / d);
    }


    // Moves a point /current/ in a straight line towards a /target/ point.
    public static Vector3d moveTowards(Vector3d current, Vector3i target, float maxDistanceDelta)
    {
        // avoid vector ops because current scripting backends are terrible at inlining
        double toVector_x = target.getX() - current.x;
        double toVector_y = target.getY() - current.y;
        double toVector_z = target.getZ() - current.z;

        double sqdist = toVector_x * toVector_x + toVector_y * toVector_y + toVector_z * toVector_z;

        if (sqdist == 0 || (maxDistanceDelta >= 0 && sqdist <= maxDistanceDelta * maxDistanceDelta))
        {
            return new Vector3d(target.getX(), target.getY(), target.getZ());
        }
        float dist = (float) Math.sqrt(sqdist);

        return new Vector3d(current.x + toVector_x / dist * maxDistanceDelta,
                current.y + toVector_y / dist * maxDistanceDelta,
                current.z + toVector_z / dist * maxDistanceDelta);
    }


    // Moves a point /current/ in a straight line towards a /target/ point.
    public static Vector3d getDirection(Vector3d target, Vector3d current, float multiplicationFactor)
    {
        double toVector_x = target.getX() - current.x;
        double toVector_y = target.getY() - current.y;
        double toVector_z = target.getZ() - current.z;

        return new Vector3d(
                toVector_x * multiplicationFactor,
                toVector_y * multiplicationFactor,
                toVector_z * multiplicationFactor);
    }

}
