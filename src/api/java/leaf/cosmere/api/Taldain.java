/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public class Taldain
{
	public enum Investiture
	{
		MASTERY(0);

		private final int id;

		Investiture(int id)
		{
			this.id = id;
		}

		public static Optional<Investiture> valueOf(int value)
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

		public boolean hasAssociatedManifestation() {
			return true;
		}
	}
}