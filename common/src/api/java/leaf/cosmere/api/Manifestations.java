/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.api;

import leaf.cosmere.api.manifestation.Manifestation;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public class Manifestations
{
	public final static int ALLOMANCY_ID = 1;
	public final static int FERUCHEMY_ID = 2;
	public final static int HEMALURGY_ID = 7;//yes I know. Didn't add this till way later and now I'm too afraid to change it... todo: change it
	public final static int SURGEBINDING_ID = 3;
	public final static int AONDOR_ID = 4;
	public final static int AWAKENING_ID = 5;
	public final static int SANDMASTERY_ID = 6;
	public final static int AVIAR_ID = 8;

	public enum ManifestationTypes
	{
		NONE(0),
		//Allomancy Section
		ALLOMANCY(ALLOMANCY_ID),

		//Feruchemy Section
		FERUCHEMY(FERUCHEMY_ID),

		//Hemalurgy Section
		HEMALURGY(HEMALURGY_ID),

		//Knight Radiant Section
		SURGEBINDING(SURGEBINDING_ID),

		// AonDor
		AON_DOR(AONDOR_ID),

		// AonDor
		AWAKENING(AWAKENING_ID),

		// Taldain's Sand Mastery
		SANDMASTERY(SANDMASTERY_ID),

		// Aviar
		AVIAR(AVIAR_ID);


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


		public String getName()
		{
			return this.name().toLowerCase(Locale.ROOT);
		}

		public Manifestation getManifestation(int powerID)
		{
			switch (this)
			{
				case ALLOMANCY:
				case FERUCHEMY:
					Optional<Metals.MetalType> metalType = Metals.MetalType.valueOf(powerID);
					if (metalType.isPresent())
					{
						return CosmereAPI.manifestationRegistry().getValue(new ResourceLocation(this.getName(), metalType.get().getName()));
					}
					break;
				case SURGEBINDING:
					Optional<Roshar.Surges> value = Roshar.Surges.valueOf(powerID);
					if (value.isPresent())
					{
						return CosmereAPI.manifestationRegistry().getValue(new ResourceLocation(this.getName(), value.get().getName()));
					}
					break;
				case AON_DOR:
					break;
				case AWAKENING:
					break;
				case SANDMASTERY:
					Optional<Taldain.Mastery> masteryStyle = Taldain.Mastery.valueOf(powerID);
					if (masteryStyle.isPresent())
					{
						return CosmereAPI.manifestationRegistry().getValue(new ResourceLocation(this.getName(), masteryStyle.get().getName()));
					}
					break;
			}
			return CosmereAPI.manifestationRegistry().getValue(new ResourceLocation("cosmere", "none"));
		}
	}

}
