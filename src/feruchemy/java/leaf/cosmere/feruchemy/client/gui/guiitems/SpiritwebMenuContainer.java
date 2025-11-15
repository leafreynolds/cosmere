package leaf.cosmere.feruchemy.client.gui.guiitems;

import leaf.cosmere.api.MenuHelpers.MenuButton;
import leaf.cosmere.api.MenuHelpers.MenuContainer;

public class SpiritwebMenuContainer extends MenuContainer
{
	public SpiritwebMenuContainer(double x, double y, int containWidth, int containHeight)
	{
		super(x, y, containWidth, containHeight);
		this.red = 125;
		this.green = 200;
		this.blue = 220; //255
	}


	@Override
	public void addButton(MenuButton button)
	{

		if (menuButtons.size() < size)
		{
			menuButtons.add(button);
		}
		else
		{
			menuButtons.add(button);
			updateDimensions(containWidth + 1, containHeight);
		}

	}

	@Override
	public void removeButton(MenuButton button)
	{
		if (menuButtons.contains(button))
		{
			if (size != 1) //Shrink the container according to amount of powers, but don't shrink if it's already at one
			{
				menuButtons.remove(button);
				updateDimensions(containWidth - 1, containHeight);
			}
			else
			{
				menuButtons.remove(button);
			}
		}
	}

	@Override
	public void clearButtons()
	{
		updateDimensions(1, 1);
		menuButtons.clear();
	}
}
