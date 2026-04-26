package leaf.cosmere.client.gui;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;

public interface ISyncSpiritweb
{
	void onSpiritwebUpdated(SpiritwebCapability cap);

	SpiritwebCapability getSpiritweb();
}
