package leaf.cosmere.api.MenuHelpers;

import com.mojang.blaze3d.vertex.BufferBuilder;
import leaf.cosmere.api.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;

public class MenuContainer
{
	protected double centerX;
	protected double centerY;

	//Refers to how many items can fit in a row and column
	protected int containWidth;
	protected int containHeight;

	protected double width;
	protected double height;

	public int red;
	public int green;
	public int blue;
	public int opacity;

	//Left side upper
	protected double x1;
	protected double y1;

	//left side downer
	protected double x2;
	protected double y2;

	//right side downer
	protected double x3;
	protected double y3;

	//right side upper
	protected double x4;
	protected double y4;

	//width * height
	protected int size;

	public boolean highlight;

	public ArrayList<MenuButton> menuButtons =  new ArrayList<>();

	public MenuContainer(double x, double y, int containWidth, int containHeight)
	{
		this.centerX = x;
		this.centerY = y;

		red = 125;
		green = 125;
		blue = 125;
		opacity = 125;

		updateDimensions(containWidth, containHeight);
	}

	public void setPosition(double x, double y)
	{
		this.centerX = x;
		this.centerY = y;

		updateDimensions(containWidth, containHeight);

	}

	public void arrangeButtons() {
		int index = 0;

		double edgeX = centerX - (width/2);
		double edgeY = centerY - (height/2);

		for (MenuButton button: menuButtons) {

			int column = index % containWidth;
			int row = index / containWidth;

			button.setPosition(edgeX + 15 + (25*column), edgeY + 15 + (25*row));

			index++;

		}
	}

	public void updateDimensions(int containWidth, int containHeight)
	{
		this.containWidth = containWidth;
		this.containHeight = containHeight;


		//width needed is spacing + contents,
		//contents = nPossibleButtons * buttonSize
		//spacing = (nPossibleButtons + 1) * spacingSize
		//same for height
		this.width = (containWidth + 1) * 5 + containWidth * 20;
		this.height = (containHeight + 1) * 5 + containHeight * 20;

		size = containHeight * containWidth;


		//left side upper
		x1 = centerX - (width / 2);
		y1 = centerY - (height / 2);

		//left side downer
		x2 = centerX - (width / 2);
		y2 = centerY + (height / 2);

		//right side downer
		x3 = centerX + (width / 2);
		y3 = centerY + (height / 2);

		//right side upper
		x4 = centerX + (width / 2);
		y4 = centerY - (height / 2);

		arrangeButtons();
	}

	public void clearButtons()
	{
		menuButtons.clear();
	}

	public void addButton(MenuButton button)
	{
		if (menuButtons.size() < size)
		{
			menuButtons.add(button);
			arrangeButtons();
		}
	}

	public void removeButton(MenuButton button)
	{
		menuButtons.remove(button);
	}

	public void highlightAction(double mouseX, double mouseY)
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

	public void highlightButtons(double mouseX, double mouseY)
	{
		for (MenuButton button : menuButtons)
		{
			button.highlightAction(mouseX, mouseY);
		}
	}

	public void renderContainer(BufferBuilder buffer)
	{

		for (MenuButton button : menuButtons)
		{
			button.renderButton(buffer);
		}

		int lerpositive;

		if (highlight)
		{
			lerpositive = 30;
		}
		else
		{
			lerpositive = 0;
		}

		buffer.vertex(x1, y1, 0).color(red + lerpositive, green + lerpositive, blue + lerpositive, opacity).endVertex();
		buffer.vertex(x2, y2, 0).color(red + lerpositive, green + lerpositive, blue + lerpositive, opacity).endVertex();

		buffer.vertex(x3, y3, 0).color(red + lerpositive, green + lerpositive, blue + lerpositive, opacity).endVertex();
		buffer.vertex(x4, y4, 0).color(red + lerpositive, green + lerpositive, blue + lerpositive, opacity).endVertex();




	}





}
