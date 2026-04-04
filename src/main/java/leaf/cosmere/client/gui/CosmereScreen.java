package leaf.cosmere.client.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

// would be an interface if Screen wasn't a class
// might be unnecessary, but could be nice to have in future, I guess
public class CosmereScreen extends Screen
{
	protected CosmereScreen(Component pTitle)
	{
		super(pTitle);
	}
}
