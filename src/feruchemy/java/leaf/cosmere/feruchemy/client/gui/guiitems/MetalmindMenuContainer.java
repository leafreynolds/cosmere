package leaf.cosmere.feruchemy.client.gui.guiitems;

import leaf.cosmere.api.MenuHelpers.MenuContainer;
import net.minecraft.world.item.ItemStack;

public class MetalmindMenuContainer extends MenuContainer
{
	ItemStack metalmind;

	public MetalmindMenuContainer(double x, double y, int containWidth, int containHeight, ItemStack stack)
	{
		super(x, y, containWidth, containHeight);
		this.green = 200;
		metalmind = stack;



	}
}
