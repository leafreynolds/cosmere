package leaf.cosmere.feruchemy.client.gui.guiitems;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import joptsimple.internal.Strings;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class SpiritwebPowerButton
{
	public double x1 = 0;
	public double y1 = 0;

	//left side downer
	public double x2 = 0;
	public double y2 = 0;

	//right side downer
	public double x3 = 0;
	public double y3 = 0;

	//right side upper
	public double x4 = 0;
	public double y4 = 0;

	public double posX = 0;
	public double posY = 0;

	public boolean highlight;
	public boolean matchesIdentity;

	public String displayName;
	public String iconPath;

	public int red;
	public int green;
	public int blue;

	public int opacity;

	double width;
	double height;

	static final StringBuilder stringBuilder = new StringBuilder();

	ISpiritweb spiritweb;
	Manifestation manifestation;
	Integer strength;
	byte slotIndex;

	SpiritwebButtonContainer container;

	public SpiritwebPowerButton(double posX, double posY, ISpiritweb spiritweb, SpiritwebButtonContainer container, byte slotIndex, boolean matchesIdentity)
	{
		this.spiritweb = spiritweb;
		this.displayName = "";
		this.container = container;
		this.slotIndex = slotIndex;
		this.matchesIdentity = matchesIdentity;

		this.width = 20;
		this.height = 20;

		this.x1 = posX - (width / 2);
		this.y1 = posY - (height / 2);

		//left side downer
		this.x2 = posX - (width / 2);
		this.y2 = posY + (height / 2);

		//right side downer
		this.x3 = posX + (width / 2);
		this.y3 = posY + (height / 2);

		//right side upper
		this.x4 = posX + (width / 2);
		this.y4 = posY - (height / 2);

		this.posX = posX;
		this.posY = posY;

		this.red = 205;
		this.blue = 205;
		this.green = 205;
		this.opacity = 100;
	}

	public void renderIcon(GuiGraphics guiGraphics)
	{
		if (!Strings.isNullOrEmpty(iconPath))
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
		if (manifestation == null)
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

	public byte getSlotIndex()
	{
		return slotIndex;
	}

	public void setPosition(double sPosX, double sPosY)
	{

		//Left side upper
		double x1offset = x1 - posX;
		double y1offset = y1 - posY;

		//left side downer
		double x2offset = x2 - posX;
		double y2offset = y2 - posY;

		//right side downer
		double x3offset = x3 - posX;
		double y3offset = y3 - posY;

		//right side upper
		double x4offset = x4 - posX;
		double y4offset = y4 - posY;

		posX = sPosX;
		posY = sPosY;

		//Left side upper
		x1 = posX + x1offset;
		y1 = posY + y1offset;

		//left side downer
		x2 = posX + x2offset;
		y2 = posY + y2offset;

		//right side downer
		x3 = posX + x3offset;
		y3 = posY + y3offset;

		//right side upper
		x4 = posX + x4offset;
		y4 = posY + y4offset;

	}



	//Sets highlight and returns that, might split into two later.
	public void highlightAction(double mouseX, double mouseY, double middle_x, double middle_y)
	{
		highlight = (MathHelper.inTriangle(
				x1, y1,
				x2, y2,
				x3, y3,
				mouseX, mouseY)
				|| MathHelper.inTriangle(
				x1, y1,
				x4, y4,
				x3, y3,
				mouseX, mouseY));

	}

	;

	public void renderButton(BufferBuilder buffer)
	{
		int r = red;;
		int g = green;;
		int b = blue;

		if (highlight)
		{
			r += 30;
			g += 30;
			b += 30;
		}
		if(!matchesIdentity)
		{
			g -= 100;
			b -= 100;
		}

		buffer.vertex(x1, y1, 0).color(r, g, b, opacity).endVertex();
		buffer.vertex(x2, y2, 0).color(r, g, b, opacity).endVertex();

		buffer.vertex(x3, y3, 0).color(r, g, b, opacity).endVertex();
		buffer.vertex(x4, y4, 0).color(r, g, b, opacity).endVertex();

	}
}
