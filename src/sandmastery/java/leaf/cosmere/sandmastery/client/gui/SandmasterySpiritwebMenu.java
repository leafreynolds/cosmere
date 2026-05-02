package leaf.cosmere.sandmastery.client.gui;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.client.gui.CosmereScreen;
import leaf.cosmere.client.gui.SpiritwebMenu;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import java.util.List;

public class SandmasterySpiritwebMenu extends CosmereScreen
{
	final LocalPlayer player;
	public SandmasterySpiritwebMenu()
	{
		super(Component.literal("Sandmastery"), SpiritwebMenu::selectManiCallback);
		player = Minecraft.getInstance().player;
	}

	@Override
	protected void init()
	{
		super.init();

		int centerX = width/2;
		int centerY = height/2 + height/16;

		SpiritwebCapability.get(player).ifPresent( (spiritweb -> {
			final List<Manifestation> availableManifestations = spiritweb.getAvailableManifestations();

			int i = 0;
			for (Manifestation mani : availableManifestations)
			{
				if (mani.getManifestationType() == Manifestations.ManifestationTypes.SANDMASTERY)
				{
					addRenderableWidget(new RadialButton(centerX, centerY, height/3, i, mani, manifestationConsumer));
					i++;
				}
			}
		}));
	}
}
