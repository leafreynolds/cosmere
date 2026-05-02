package leaf.cosmere.client.gui;

import leaf.cosmere.api.Manifestations;
import net.minecraft.client.gui.screens.Screen;

import java.util.HashMap;
import java.util.function.Supplier;

public class SpiritwebRegistry
{
	private static volatile SpiritwebRegistry INSTANCE = new SpiritwebRegistry();
	private final HashMap<Manifestations.ManifestationTypes, Supplier<CosmereScreen>> manifestationScreenMap = new HashMap<>();

	public static SpiritwebRegistry getInstance()
	{
		return INSTANCE;
	}

	public void register(Manifestations.ManifestationTypes maniType, Supplier<CosmereScreen> subScreen)
	{
		manifestationScreenMap.put(maniType, subScreen);
	}

	public HashMap<Manifestations.ManifestationTypes, Supplier<CosmereScreen>> getManifestationScreenMap()
	{
		return manifestationScreenMap;
	}

	public void clear()
	{
		manifestationScreenMap.clear();
	}
}
