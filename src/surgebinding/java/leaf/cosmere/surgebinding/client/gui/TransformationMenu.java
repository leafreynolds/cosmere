package leaf.cosmere.surgebinding.client.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TransformationMenu extends Screen
{

	protected TransformationMenu(Component pTitle)
	{
		super(pTitle);
	}

	@Override
	protected void init()
	{
		super.init();

	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
}
