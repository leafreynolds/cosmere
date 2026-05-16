package leaf.cosmere.client.gui;

import leaf.cosmere.api.manifestation.Manifestation;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

// would be an interface if Screen wasn't a class
// might be unnecessary, but could be nice to have in future, I guess
public class CosmereScreen extends Screen
{
	protected final Consumer<Manifestation> manifestationConsumer;
	protected CosmereScreen(Component pTitle, Consumer<Manifestation> manifestationConsumer)
	{
		super(pTitle);
		this.manifestationConsumer = manifestationConsumer;
	}
}
