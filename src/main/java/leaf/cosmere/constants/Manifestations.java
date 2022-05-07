/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.constants;

import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.ManifestationRegistry;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public class Manifestations
{
	public final static int ALLOMANCY_ID = 1;
	public final static int FERUCHEMY_ID = 2;
	public final static int RADIANT_ID = 3;
	public final static int ELANTRIAN_ID = 4;
	public final static int AWAKENER_ID = 5;

	public enum ManifestationTypes
	{
		NONE(0),
		//Allomancy Section
		ALLOMANCY(ALLOMANCY_ID),

		//Feruchemy Section
		FERUCHEMY(FERUCHEMY_ID),

		//Knight Radiant Section
		RADIANT(RADIANT_ID),

		// AonDor
		ELANTRIAN(ELANTRIAN_ID),

		// AonDor
		AWAKENER(AWAKENER_ID);


		ManifestationTypes(int id)
		{
			this.id = id;
		}

		final int id;

		public int getID()
		{
			return id;
		}


		public static Optional<ManifestationTypes> valueOf(int value)
		{
			return Arrays.stream(values())
					.filter(powerTypes -> powerTypes.id == value)
					.findFirst();
		}

		public AManifestation getManifestation(int powerID)
		{
			Optional<Metals.MetalType> metalType = Metals.MetalType.valueOf(powerID);
			switch (this)
			{
				case ALLOMANCY:
					if (metalType.isPresent())
					{
						return ManifestationRegistry.ALLOMANCY_POWERS.get(metalType.get()).get();
					}
					break;
				case FERUCHEMY:
					if (metalType.isPresent())
					{
						return ManifestationRegistry.FERUCHEMY_POWERS.get(metalType.get()).get();
					}
					break;
				case RADIANT:
					break;
				case ELANTRIAN:
					break;
				case AWAKENER:
					break;
			}
			return ManifestationRegistry.NONE.get();
		}

		public String getName()
		{
			return this.name().toLowerCase(Locale.ROOT);
		}
	}


	public static final String NONE = "none";

	public static class AonDor
	{
		public static final String ELANTRIAN = "elantrian";
	}

	public static class Biochroma
	{
		public static final String AWAKENING = "awakening";
	}

	public static class Hemalurgy
	{
		public static final String NONE = "none";
	}

	public static class Surgebinding
	{
		public static final String WINDRUNNER = "windrunner";
		public static final String SKYBREAKER = "skybreaker";
		public static final String DUSTBRINGER = "dustbringer";
		public static final String EDGEDANCER = "edgedancer";
		public static final String TRUTHWATCHER = "truthwatcher";
		public static final String LIGHTWEAVER = "lightweaver";
		public static final String ELSECALLER = "elsecaller";
		public static final String WILLSHAPER = "willshaper";
		public static final String STONEWARD = "stoneward";
		public static final String BONDSMITH = "bondsmith";
	}
}
