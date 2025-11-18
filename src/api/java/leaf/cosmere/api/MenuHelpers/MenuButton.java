package leaf.cosmere.api.MenuHelpers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import joptsimple.internal.Strings;
import leaf.cosmere.api.math.MathHelper;
import net.minecraft.resources.ResourceLocation;

public class MenuButton
{

	//behold, the anatomy of a *BOX*:
	//  1-4
	//  | |
	//  2-3

	//not set in stone but fine to stick to something

	//Left side upper
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

	public String displayName;
	public String iconPath;

	public int red;
	public int green;
	public int blue;

	public int opacity;

	public MenuButton()
	{
		red = 125;
		green = 125;
		blue = 125;
		opacity = 125;
		highlight = false;

	}

	public void setPosition(double sPosX, double sPosY) {

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

	public void setColour(int red, int green, int blue)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public void setOpacity(int opacity)
	{
		this.opacity = opacity;
	}

	//Float versions for previous^^
	public void setColour(float red, float green, float blue)
	{
		setColour((int) red * 255, (int) green * 255, (int) blue * 255);
	}

	public void setOpacity(float opacity)
	{
		setOpacity ((int) opacity * 255);
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

	};

	public void renderButton(BufferBuilder buffer)
	{
		int lerpositive = 0;

		if (highlight)
		{
			lerpositive = 50;
		}

		buffer.vertex(x1, y1, 0).color(red + lerpositive, green + lerpositive, blue + lerpositive, opacity).endVertex();
		buffer.vertex(x2, y2, 0).color(red + lerpositive, green + lerpositive, blue + lerpositive, opacity).endVertex();

		buffer.vertex(x3, y3, 0).color(red + lerpositive, green + lerpositive, blue + lerpositive, opacity).endVertex();
		buffer.vertex(x4, y4, 0).color(red + lerpositive, green + lerpositive, blue + lerpositive, opacity).endVertex();

	}

}
