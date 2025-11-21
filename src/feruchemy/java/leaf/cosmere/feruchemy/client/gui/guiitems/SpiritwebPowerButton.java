package leaf.cosmere.feruchemy.client.gui.guiitems;

import com.mojang.blaze3d.systems.RenderSystem;
import joptsimple.internal.Strings;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.MenuHelpers.SquareMenuButton;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class SpiritwebPowerButton extends SquareMenuButton
{
	static final StringBuilder stringBuilder = new StringBuilder();

	ISpiritweb spiritweb;
	Manifestation manifestation;
	Integer strength;

	SpiritwebButtonContainer container;

	public SpiritwebPowerButton(double posX, double posY, ISpiritweb spiritweb, SpiritwebButtonContainer container)
	{
		super(posX, posY);
		this.spiritweb = spiritweb;
		this.displayName = "";
		this.container = container;
	}

	public void renderIcon(GuiGraphics guiGraphics){
		if(!Strings.isNullOrEmpty(iconPath))
		{
			final ResourceLocation textureLocation = new ResourceLocation(manifestation.getRegistryName().getNamespace(), iconPath);
			RenderSystem.setShaderTexture(0, textureLocation);
			guiGraphics.blit(textureLocation,
					(int) (posX - 7.5),
					(int) (posY - 7.5),
					16,
					16,
					0,
					0,
					18,
					18,
					18,
					18);
		}
	}

	public SpiritwebButtonContainer getContainer()
	{
		return container;
	}

	public ISpiritweb getSpiritweb()
	{
		return spiritweb;
	}

	public void setSpiritweb(ISpiritweb spiritweb)
	{
		this.spiritweb = spiritweb;
	}

	public Manifestation getManifestation()
	{
		return manifestation;
	}

	public void setManifestation(Manifestation manifestation)
	{
		if(manifestation == null)
		{
			// Remove manifestation from button
			this.manifestation = null;
			this.iconPath = null;
		}
		else
		{
			this.manifestation = manifestation;

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

	public Integer getStrength()
	{
		return strength;
	}

	public void setStrength(Integer strength)
	{
		this.strength = strength;
	}


}
