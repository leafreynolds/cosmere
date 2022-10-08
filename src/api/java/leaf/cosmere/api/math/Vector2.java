/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api.math;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

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

	public boolean equals(Vec2 other)
	{
		return this.x == other.x && this.y == other.y;
	}

	public static final float Deg2Rad = 0.01745329f;

	public Vector2 Rotate(float degrees)
	{
		float sin = Mth.sin(degrees * Deg2Rad);
		float cos = Mth.cos(degrees * Deg2Rad);

		float tx = this.x;
		float ty = this.y;
		this.x = (cos * tx) - (sin * ty);
		this.y = (sin * tx) + (cos * ty);

		return this;
	}

	public Vector2 oldRotate(float delta)
	{
		x = this.x * Mth.cos(delta) - this.y * Mth.sin(delta);
		y = this.x * Mth.sin(delta) + this.y * Mth.cos(delta);
		return this;
	}

	public static Vector2 Rotate(Vector2 v, float delta)
	{
		return new Vector2(
				v.x * Mth.cos(delta) - v.y * Mth.sin(delta),
				v.x * Mth.sin(delta) + v.y * Mth.cos(delta)
		);
	}
}
