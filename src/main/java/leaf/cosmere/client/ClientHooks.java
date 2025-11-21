package leaf.cosmere.client;

import leaf.cosmere.client.gui.SpiritwebMenu;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.client.Minecraft;

public class ClientHooks
{
	public static void onSpiritwebUpdated(SpiritwebCapability cap) {
		Minecraft mc = Minecraft.getInstance();
		if (mc == null) return;

		if (mc.screen instanceof SpiritwebMenu spiritMenu) {
			spiritMenu.onSpiritwebUpdated(cap);
		}
	}
}
