/*
 * File created ~ 25 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.helpers;

import net.minecraft.util.math.MathHelper;
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
}
