package leaf.cosmere.feruchemy.client;


import leaf.cosmere.api.Activator;
import leaf.cosmere.api.Metals;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.client.PowerKeyMappings;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyManifestation;
import leaf.cosmere.feruchemy.common.registries.FeruchemyManifestations;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import java.util.HashMap;
import java.util.Map;

import static leaf.cosmere.feruchemy.common.registries.FeruchemyManifestations.FERUCHEMY_POWERS;

@EventBusSubscriber(value = Dist.CLIENT, modid = Feruchemy.MODID)
public class FeruchemyKeybindings
{
	public static final Map<Metals.MetalType, KeyMapping> FERUCHEMY_POWER = new HashMap<>();

	@SubscribeEvent
	public static void register(RegisterKeyMappingsEvent event)
	{
		FeruchemyManifestations.KEY_ACTIVATORS.forEach((metalType, def) ->
		{
			KeyMapping key = PowerKeyMappings.register(event, def);
			FERUCHEMY_POWER.put(metalType, key);
			FeruchemyManifestation manifest = FERUCHEMY_POWERS.get(metalType).getManifestation();
			Activator entry = new Activator(key, manifest);
			entry.setCategory("feruchemy");
			Keybindings.activators.add(entry);
		});
	}
}
