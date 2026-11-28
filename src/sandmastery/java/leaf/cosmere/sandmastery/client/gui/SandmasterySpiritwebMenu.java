package leaf.cosmere.sandmastery.client.gui;

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

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTick);

		// free ribbons = strength minus every Sandmastery power's allocation, shown above the radial
		SpiritwebCapability.get(player).ifPresent(spiritweb ->
		{
			int total = 0;
			int inUse = 0;
			for (Manifestation mani : spiritweb.getAvailableManifestations())
			{
				if (mani.getManifestationType() == Manifestations.ManifestationTypes.SANDMASTERY)
				{
					total = (int) mani.getStrength(spiritweb, false);
					inUse += spiritweb.getMode(mani);
				}
			}
			int free = Math.max(0, total - inUse);
			int radialTop = height / 2 + height / 16 - height / 3;
			guiGraphics.drawCenteredString(font, Component.translatable("gui.sandmastery.ribbons_free", free), width / 2, radialTop - font.lineHeight - 16, 0xFFFFFF);
		});
	}
}
