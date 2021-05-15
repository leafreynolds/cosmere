/*
 * File created ~ 12 - 5 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.math;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;

public class Vector2
{
    public static final Vector2 ZERO = new Vector2(0.0F, 0.0F);
    public static final Vector2 ONE = new Vector2(1.0F, 1.0F);

    public float x = 0;
    public float y = 0;

    public Vector2(float xIn, float yIn)
    {
        this.x = xIn;
        this.y = yIn;
    }

    public boolean equals(Vector2f other)
    {
        return this.x == other.x && this.y == other.y;
    }

    public static final float Deg2Rad = 0.01745329f;
    public Vector2 Rotate(float degrees)
    {
        float sin = MathHelper.sin(degrees * Deg2Rad);
        float cos = MathHelper.cos(degrees * Deg2Rad);

        float tx = this.x;
        float ty = this.y;
        this.x = (cos * tx) - (sin * ty);
        this.y = (sin * tx) + (cos * ty);

        return this;
    }
    public Vector2 oldRotate(float delta)
    {
        x = this.x * MathHelper.cos(delta) - this.y * MathHelper.sin(delta);
        y = this.x * MathHelper.sin(delta) + this.y * MathHelper.cos(delta);
        return this;
    }

    public static Vector2 Rotate(Vector2 v, float delta)
    {
        return new Vector2(
                v.x * MathHelper.cos(delta) - v.y * MathHelper.sin(delta),
                v.x * MathHelper.sin(delta) + v.y * MathHelper.cos(delta)
        );
    }
}
