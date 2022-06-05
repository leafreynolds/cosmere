/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.constants;

import leaf.cosmere.client.ClientHelper;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

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

}
