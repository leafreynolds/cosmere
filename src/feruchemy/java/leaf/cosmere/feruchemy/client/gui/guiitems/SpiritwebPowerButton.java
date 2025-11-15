package leaf.cosmere.feruchemy.client.gui.guiitems;

import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.MenuHelpers.SquareMenuButton;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;

public class SpiritwebPowerButton extends SquareMenuButton
{
	static final StringBuilder stringBuilder = new StringBuilder();

	Manifestation manifestation;
	ISpiritweb spiritweb;
	double strength;


	public SpiritwebPowerButton(double posY, double posX, Manifestation manifestation, ISpiritweb spiritweb)
	{
		super(posY, posX);
		this.manifestation = manifestation;
		this.spiritweb = spiritweb;
		this.strength = manifestation.getStrength(spiritweb, true);
		this.displayName = "";

		//Create icon path
		stringBuilder.setLength(0);
		final Manifestations.ManifestationTypes manifestationType = manifestation.getManifestationType();
		String manifestationTypeName = manifestationType.getName();
		stringBuilder
				.append("textures/icon/")
				.append(manifestationTypeName)
				.append("/");

		switch (manifestationType)
		{
			case ALLOMANCY:
			case FERUCHEMY:
				if (manifestation instanceof IHasMetalType metalType)
				{
					stringBuilder.append(metalType.getMetalType().getName());
				}
				break;
			case SURGEBINDING:
				stringBuilder.append(manifestation.getName());
				break;
			case AON_DOR:
			case AWAKENING:
				break;
		}

		stringBuilder.append(".png");
		this.iconPath = stringBuilder.toString();





	}


}
