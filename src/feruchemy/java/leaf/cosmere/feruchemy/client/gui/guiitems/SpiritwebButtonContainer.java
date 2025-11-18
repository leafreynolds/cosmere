package leaf.cosmere.feruchemy.client.gui.guiitems;

import leaf.cosmere.api.MenuHelpers.MenuButton;
import leaf.cosmere.api.MenuHelpers.MenuContainer;

public class SpiritwebButtonContainer extends MenuContainer
{
	public Integer curioItemSlot;

	public SpiritwebButtonContainer(double x, double y, int containWidth, int containHeight, int curioItemSlot)
	{
		super(x, y, containWidth, containHeight);
		this.red = 0;
		this.green = 0;
		this.blue = 0; //255
		this.curioItemSlot = curioItemSlot;
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
			updateDimensions(containWidth + 1, containHeight, centerX, centerY);
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
				updateDimensions(containWidth - 1, containHeight, centerX, centerY);
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
		updateDimensions(1, 1, centerX, centerY);
		menuButtons.clear();
	}
}
