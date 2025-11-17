/*
 * File updated ~ 10 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.client.gui;

import com.google.common.base.Stopwatch;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import leaf.cosmere.api.*;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.api.math.Vector2;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.packets.ChangeManifestationModeMessage;
import leaf.cosmere.common.network.packets.SetSelectedManifestationMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookPage;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.RecipeBook;
import net.minecraft.world.inventory.RecipeBookMenu;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SpiritwebMenu extends Screen
{
	private float visibility = 0.0f;
	private Stopwatch lastChange = Stopwatch.createStarted();
	private ISpiritweb spiritweb;

	public SpiritwebMenu(Component pTitle, ISpiritweb spiritweb)
	{
		super(pTitle);
		this.spiritweb = spiritweb;
	}

	public void raiseVisibility()
	{
		final float TIME_SCALE = 0.01f;
		visibility = MathHelper.clamp01(visibility + lastChange.elapsed(TimeUnit.MILLISECONDS) * TIME_SCALE);
		lastChange = Stopwatch.createStarted();
	}

	private void CloseScreen()
	{
		this.minecraft.setScreen(null);
	}

	@Override
	public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
		raiseVisibility();
		final int start = (int) (visibility * 98) << 24;
		final int end = (int) (visibility * 128) << 24;

		pGuiGraphics.fillGradient(0, 0, width, height, start, end);
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
}