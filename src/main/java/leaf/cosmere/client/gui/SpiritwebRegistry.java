package leaf.cosmere.client.gui;

import leaf.cosmere.api.Manifestations;
import net.minecraft.client.gui.screens.Screen;

import java.util.HashMap;
import java.util.function.Supplier;

public class SpiritwebRegistry
{
	private static SpiritwebRegistry INSTANCE;
	private final HashMap<Manifestations.ManifestationTypes, Supplier<Screen>> manifestationScreenMap = new HashMap<>();

	public static SpiritwebRegistry getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new SpiritwebRegistry();
		}
		return INSTANCE;
	}

	public void register(Manifestations.ManifestationTypes maniType, Supplier<Screen> subScreen)
	{
		manifestationScreenMap.put(maniType, subScreen);
	}

	public HashMap<Manifestations.ManifestationTypes, Supplier<Screen>> getManifestationScreenMap()
	{
		return manifestationScreenMap;
	}
}
