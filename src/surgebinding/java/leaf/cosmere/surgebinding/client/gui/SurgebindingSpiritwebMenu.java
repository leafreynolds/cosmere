package leaf.cosmere.surgebinding.client.gui;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.client.gui.CosmereScreen;
import leaf.cosmere.client.gui.SpiritwebMenu;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import java.util.List;

public class SurgebindingSpiritwebMenu extends CosmereScreen
{
	final LocalPlayer player;
	public SurgebindingSpiritwebMenu()
	{
		super(Component.literal("Surgebinding"), SpiritwebMenu::selectManiCallback);
		player = Minecraft.getInstance().player;
	}

	@Override
	protected void init()
	{
		super.init();

		int radius = height/16;

		SpiritwebCapability.get(player).ifPresent( (spiritweb -> {
			final List<Manifestation> availableManifestations = spiritweb.getAvailableManifestations();

			int i = 0;
			for (Manifestation mani : availableManifestations)
			{
				if (mani.getManifestationType() == Manifestations.ManifestationTypes.SURGEBINDING)
				{
					addRenderableWidget(new CircleButton((int) (width/2f - radius*1.5 + (radius*3) * i), height/2, radius, spiritweb, mani, manifestationConsumer));
					i++;
				}
			}
		}));
	}

	@Override
	public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
	}
}
