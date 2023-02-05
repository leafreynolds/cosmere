/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public class Taldain
{
	public enum Mastery
	{
		ELEVATE(0),
//		PLATFORM(1),
//		STAIR(2),
		LAUNCH(3),
		PROJECTILE(4),
//		WALL(5),
//		SHIELD(6),
//		BUILDING(7),
//		DECOY(8),
		CUSHION(9);

		private final int id;

		Mastery(int id) { this.id = id; }

		public static Optional<Mastery> valueOf(int value)
		{
			return Arrays.stream(values())
					.filter(investiture -> investiture.id == value)
					.findFirst();
		}

		public int getID()
		{
			return id;
		}

		public String getName()
		{
			return name().toLowerCase(Locale.ROOT);
		}
	}
}