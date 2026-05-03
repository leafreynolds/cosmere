/*
 * File updated ~ 10 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.client.gui;

import com.google.common.base.Stopwatch;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.network.packets.SetSelectedManifestationMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SpiritwebMenu extends Screen
{
	private float visibility = 0.0f;
	private Stopwatch lastChange = Stopwatch.createStarted();
	private final ISpiritweb spiritweb;
	private final SpiritwebRegistry registry;
	private CosmereScreen selectedManifestationScreen = null;
	public static Manifestations.ManifestationTypes selectedManifestationType = Manifestations.ManifestationTypes.NONE;
	private static Manifestation selectedManifestation = null;

	public SpiritwebMenu(Component pTitle, ISpiritweb spiritweb)
	{
		super(pTitle);
		this.spiritweb = spiritweb;
		this.registry = SpiritwebRegistry.getInstance();
	}

	public void raiseVisibility()
	{
		final float TIME_SCALE = 0.01f;
		visibility = MathHelper.clamp01(visibility + lastChange.elapsed(TimeUnit.MILLISECONDS) * TIME_SCALE);
		lastChange = Stopwatch.createStarted();
	}

	private void CloseScreen()
	{
		prepareClose();
		this.minecraft.setScreen(null);
	}

	public void prepareClose()
	{
		if (selectedManifestationScreen != null && selectedManifestation != null)
		{
			Cosmere.packetHandler().sendToServer(new SetSelectedManifestationMessage(selectedManifestation));
			selectedManifestation = null;
		}
	}

	@Override
	protected void init()
	{
		if (spiritweb.getSelectedManifestation() != null)
		{
			selectedManifestationType = spiritweb.getSelectedManifestation().getManifestationType();
		}
		AtomicInteger added = new AtomicInteger(0);
		int count = registry.getManifestationScreenMap().size();
		int buttonWidth = 32;
		int offset = 5;
		int totalBlockWidth = (count-1) * (buttonWidth+offset) + buttonWidth;
		int startX = (this.width/2) - (totalBlockWidth/2);
		for (int i = 0; i < Manifestations.ManifestationTypes.AVIAR.getID(); i++)
		{
			Manifestations.ManifestationTypes.valueOf(i).ifPresent( (maniType) ->
			{
				if (registry.getManifestationScreenMap().get(maniType) != null)
				{
					int x = startX + (added.get() * (buttonWidth+offset));
					addRenderableWidget(new TabButton(x, (pButton ->
					{
						if (maniType != selectedManifestationType)
						{
							selectedManifestationType = maniType;
							selectedManifestationScreen = registry.getManifestationScreenMap().get(maniType).get();
							selectedManifestationScreen.init(Minecraft.getInstance(), this.width, this.height);
						}
					}), maniType,
							() -> selectedManifestationType));

					added.set(added.get()+1);

					if (selectedManifestationType == maniType)
					{
						selectedManifestationScreen = registry.getManifestationScreenMap().get(maniType).get();
					}
					else if (added.get() == 1 && selectedManifestationScreen != null)
					{
						selectedManifestationType = maniType;
						selectedManifestationScreen = registry.getManifestationScreenMap().get(maniType).get();
					}
				}
			});
		}
		if (selectedManifestationScreen != null)
			selectedManifestationScreen.init(Minecraft.getInstance(), this.width, this.height);
	}

	@Override
	public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
		raiseVisibility();
		final int start = (int) (visibility * 98) << 24;
		final int end = (int) (visibility * 128) << 24;

		pGuiGraphics.fillGradient(0, 0, width, height, start, end);

		renderInfoBlock(pGuiGraphics);

		selectedManifestation = null;
		if (selectedManifestationScreen != null)
		{
			selectedManifestationScreen.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
		}
	}

	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton)
	{
		if (selectedManifestationScreen != null)
		{
			selectedManifestationScreen.mouseClicked(pMouseX, pMouseY, pButton);
		}
		return super.mouseClicked(pMouseX, pMouseY, pButton);
	}

	@Override
	public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers)
	{
		if (Keybindings.MANIFESTATION_MENU.matches(pKeyCode, pScanCode))
		{
			CloseScreen();
		}

		return super.keyReleased(pKeyCode, pScanCode, pModifiers);
	}

	@Override
	public boolean isPauseScreen()
	{
		// no pause >:(
		return false;
	}

	private void renderInfoBlock(GuiGraphics pGuiGraphics)
	{
		if (selectedManifestation != null && selectedManifestation.getInfoBlock() != null)
			selectedManifestation.getInfoBlock().render(pGuiGraphics, 0, 0, 0f);
	}

	public static void selectManiCallback(Manifestation manifestation)
	{
		selectedManifestation = manifestation;
		selectedManifestationType = manifestation.getManifestationType();
	}
}
