package leaf.cosmere.client;

import dev.munebase.dynamickeybinds.DynamicKeyRegistryProvider;
import net.minecraft.client.KeyMapping;

/**
 * Client half of the DynamicKeybinds compat. Only call it behind
 * {@link leaf.cosmere.common.compat.dynamickeybinds.PowerKeybinds#ENABLED}.
 */
final class DynamicKeybindsClientCompat
{
	private DynamicKeybindsClientCompat()
	{
	}

	static KeyMapping get(String id)
	{
		return DynamicKeyRegistryProvider.getRegistry().getKeyBindById(id);
	}
}
