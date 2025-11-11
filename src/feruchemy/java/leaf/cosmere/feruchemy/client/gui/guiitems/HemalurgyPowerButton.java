package leaf.cosmere.feruchemy.client.gui.guiitems;

import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.MenuHelpers.SquareMenuButton;
import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import net.minecraft.world.item.ItemStack;

public class HemalurgyPowerButton extends SquareMenuButton
{
	static final StringBuilder stringBuilder = new StringBuilder();

	Manifestation manifestation;
	ItemStack stack;
	double strength;


	public HemalurgyPowerButton(double posY, double posX, Manifestation manifestation, ItemStack itemStack)
	{
		super(posY, posX);
		this.manifestation = manifestation;
		this.stack = itemStack;
		this.strength = 0;


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

		//type.getName() + Constants.RegNameStubs.SPIKE

		stringBuilder.append(".png");
		this.iconPath = stringBuilder.toString();


	}


}
