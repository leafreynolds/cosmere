package leaf.cosmere.client;

import com.mojang.blaze3d.platform.InputConstants;
import leaf.cosmere.common.compat.dynamickeybinds.DynamicKeybindsCompat;
import leaf.cosmere.common.compat.dynamickeybinds.PowerKeybinds;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Client side of {@link PowerKeybinds}. Read power keys through {@link #isDown}/{@link #consumeClick}
 * so they resolve to the DynamicKeybinds mapping when that mod is installed.
 */
public final class PowerKeyMappings
{
	private static final Map<KeyMapping, String> DYNAMIC = new HashMap<>();

	private PowerKeyMappings()
	{
	}

	public static KeyMapping register(RegisterKeyMappingsEvent event, PowerKeybinds.Def def)
	{
		KeyMapping keyMapping = new KeyMapping(def.name(), def.defaultKey(), def.category());
		if (!PowerKeybinds.ENABLED)
		{
			event.register(keyMapping);
			return keyMapping;
		}
		// unregistered placeholder; must never fire. The real mapping is granted by the server.
		keyMapping.setKey(InputConstants.UNKNOWN);
		DYNAMIC.put(keyMapping, def.id());
		return keyMapping;
	}

	/** The live mapping for this key, or null if it is dynamic and not granted yet. */
	public static @Nullable KeyMapping get(KeyMapping keyMapping)
	{
		String id = DYNAMIC.get(keyMapping);
		return id == null ? keyMapping : DynamicKeybindsCompat.clientGet(id);
	}

	public static boolean isDown(KeyMapping keyMapping)
	{
		KeyMapping key = get(keyMapping);
		return key != null && key.isDown();
	}

	public static boolean consumeClick(KeyMapping keyMapping)
	{
		KeyMapping key = get(keyMapping);
		return key != null && key.consumeClick();
	}
}
