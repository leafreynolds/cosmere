/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to the Chisels and Bits team for their example of rendering a dynamic menu based on given elements!
 * https://github.com/ChiselsAndBits/Chisels-and-Bits
 *
 * At the moment it's just showing all manifestation powers that a user has,
 * but eventually will be subclassed to show menus by power type instead.
 *
 */

package leaf.cosmere.client.gui;

import com.google.common.base.Stopwatch;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.client.ClientHelper;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.allomancy.AllomancyBase;
import leaf.cosmere.manifestation.feruchemy.FeruchemyBase;
import leaf.cosmere.network.Network;
import leaf.cosmere.network.packets.ChangeManifestationModeMessage;
import leaf.cosmere.network.packets.SetSelectedManifestationMessage;
import leaf.cosmere.registry.KeybindingRegistry;
import leaf.cosmere.utils.helpers.MathHelper;
import leaf.cosmere.utils.helpers.ResourceLocationHelper;
import leaf.cosmere.utils.math.Vector2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SpiritwebMenu extends Screen
{
	final double TEXT_DISTANCE = 30;

	public static final SpiritwebMenu instance = new SpiritwebMenu();
	private SpiritwebCapability spiritweb = null;

	private float visibility = 0.0f;
	private Stopwatch lastChange = Stopwatch.createStarted();
	public AManifestation selectedManifestation = null;
	public SidedMenuButton doAction = null;
	private Manifestations.ManifestationTypes selectedPowerType = Manifestations.ManifestationTypes.ALLOMANCY;

	protected ArrayList<RadialMenuButton> radialMenuButtons = new ArrayList<>();
	protected ArrayList<SidedMenuButton> sidedMenuButtons = new ArrayList<>();

	protected SpiritwebMenu()
	{
		super(new TextComponent("Menu"));
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}

	public void raiseVisibility()
	{
		final float TIME_SCALE = 0.01f;
		visibility = MathHelper.clamp01(visibility + lastChange.elapsed(TimeUnit.MILLISECONDS) * TIME_SCALE);
		lastChange = Stopwatch.createStarted();
	}

	public void setScaledResolution(final int scaledWidth, final int scaledHeight)
	{
		width = scaledWidth;
		height = scaledHeight;
	}

	@Override
	public Minecraft getMinecraft()
	{
		return Minecraft.getInstance();
	}


	public void postRender(RenderGameOverlayEvent.Post event, SpiritwebCapability spiritweb)
	{
		if (KeybindingRegistry.MANIFESTATION_MENU.consumeClick())
		{
			final Window window = event.getWindow();
			init(Minecraft.getInstance(), window.getGuiScaledWidth(), window.getGuiScaledHeight());
			setScaledResolution(window.getGuiScaledWidth(), window.getGuiScaledHeight());
			this.spiritweb = spiritweb;
			getMinecraft().screen = SpiritwebMenu.instance;
			getMinecraft().mouseHandler.releaseMouse();
			visibility = 0;
			lastChange = Stopwatch.createStarted();

			selectedManifestation = spiritweb.manifestation();

			SetupButtons();
		}
		if (KeybindingRegistry.MANIFESTATION_MENU.isDown())
		{
			raiseVisibility();
		}
	}

	@Override
	public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers)
	{
		if (KeybindingRegistry.MANIFESTATION_MENU.matches(pKeyCode, pScanCode))
		{
			for (RadialMenuButton radialMenuButton : radialMenuButtons)
			{
				if (radialMenuButton.highlighted)
				{
					Network.sendToServer(new SetSelectedManifestationMessage(radialMenuButton.manifestation));
					break;
				}
			}

			CloseScreen();
		}
		return super.keyReleased(pKeyCode, pScanCode, pModifiers);
	}

	@Override
	public boolean mouseClicked(final double mouseX, final double mouseY, final int button)
	{
		for (RadialMenuButton radialMenuButton : radialMenuButtons)
		{
			if (radialMenuButton.highlighted)
			{
				if (button == 0)
				{
					Network.sendToServer(new ChangeManifestationModeMessage(radialMenuButton.manifestation, 1));
				}
				else
				{
					Network.sendToServer(new ChangeManifestationModeMessage(radialMenuButton.manifestation, -1));
				}
				return true;
			}
		}
		for (SidedMenuButton sidedMenuButton : sidedMenuButtons)
		{
			if (sidedMenuButton.highlighted)
			{
				if (sidedMenuButton.powerType != -1)
				{
					selectedPowerType = Manifestations.ManifestationTypes.valueOf(doAction.powerType).get();
					SetupButtons();
				}
				else if (sidedMenuButton.action != null)
				{
					//do other action
				}

				return true;
			}
		}
		CloseScreen();
		return true;
	}

	private void CloseScreen()
	{
		this.minecraft.setScreen(null);
		if (this.minecraft.screen == null)
		{
			this.minecraft.setWindowActive(true);
			this.minecraft.getSoundManager().resume();
			this.minecraft.mouseHandler.grabMouse();
		}
	}

	private static class SidedMenuButton
	{

		public double x1, x2;
		public double y1, y2;
		public boolean highlighted;

		public final ButtonAction action;
		public final int powerType;
		public int color;
		public String name;
		public Direction textSide;

		public SidedMenuButton(
				final String name,
				final ButtonAction action,
				final double x,
				final double y,
				final Direction textSide)
		{
			this.name = name;
			this.action = action;
			this.powerType = -1;
			x1 = x;
			x2 = x + 18;
			y1 = y;
			y2 = y + 18;
			color = 0xffffff;
			this.textSide = textSide;
		}

		public SidedMenuButton(
				final String name,
				final int powerType,
				final double x,
				final double y,
				final Direction textSide)
		{
			this.name = name;
			this.action = null;
			this.powerType = powerType;
			x1 = x;
			x2 = x + 18;
			y1 = y;
			y2 = y + 18;
			color = 0xffffff;
			this.textSide = textSide;
		}
	}

	static class RadialMenuButton
	{

		public final AManifestation manifestation;
		public double centerX;
		public double centerY;
		public boolean highlighted;

		public RadialMenuButton(final AManifestation manifestation)
		{
			this.manifestation = manifestation;
		}

	}

	protected void SetupButtons()
	{
		radialMenuButtons.clear();
		sidedMenuButtons.clear();

		final List<AManifestation> availableManifestations = spiritweb.getAvailableManifestations();

		if (availableManifestations.size() <= 16)
		{
			for (AManifestation manifestation : availableManifestations)
			{
				radialMenuButtons.add(new RadialMenuButton(manifestation));
			}
		}
		else
		{
			Set<Manifestations.ManifestationTypes> foundPowerTypes = new HashSet<>();

			for (AManifestation manifestation : availableManifestations)
			{
				if (manifestation.getManifestationType() == selectedPowerType)
				{
					radialMenuButtons.add(new RadialMenuButton(manifestation));
				}
				foundPowerTypes.add(manifestation.getManifestationType());
			}

			for (Manifestations.ManifestationTypes foundPowerType : foundPowerTypes)
			{
				final int index = foundPowerType.getID() - 1;
				final double v = TEXT_DISTANCE * index;
				sidedMenuButtons.add(
						new SidedMenuButton(
								foundPowerType.getName(),
								foundPowerType.getID(),
								v - ((TEXT_DISTANCE * foundPowerTypes.size()) / 2) + 5,
								-90,
								Direction.UP)
				);

			}
		}
	}

	@Override
	public void render(final @NotNull PoseStack matrixStack, final int mouseX, final int mouseY, final float partialTicks)
	{
		if (spiritweb == null)
		{
			return;
		}

		matrixStack.pushPose();

		final int start = (int) (visibility * 98) << 24;
		final int end = (int) (visibility * 128) << 24;

		fillGradient(matrixStack, 0, 0, width, height, start, end);

		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		final Tesselator tessellator = Tesselator.getInstance();
		final BufferBuilder buffer = tessellator.getBuilder();

		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		final double mouseVecX = mouseX - width / 2f;
		final double mouseVecY = (mouseY - height / 2f);

		final double middle_x = width / 2f;
		final double middle_y = height / 2f;

		selectedManifestation = null;
		doAction = null;

		//render the button backgrounds
		renderRadialButtons(buffer, mouseVecX, mouseVecY, middle_x, middle_y);
		renderSidedButtons(buffer, mouseVecX, mouseVecY, middle_x, middle_y);

		//draw out what we've asked for
		tessellator.end();

		drawIcons(matrixStack, buffer, middle_x, middle_y);


		// draw radial button strings
		renderRadialButtonStrings(matrixStack, (int) middle_x, (int) middle_y);
		//draw sided button strings
		renderSidedButtonStrings(matrixStack, middle_x, middle_y);
		//do extra text info stuff
		renderAnyExtraInfoTexts(matrixStack, (int) middle_x, (int) middle_y);

		matrixStack.popPose();
	}

	private void drawIcons(@NotNull PoseStack matrixStack, BufferBuilder buffer, double middle_x, double middle_y)
	{
		matrixStack.pushPose();
		RenderSystem.enableTexture();
		RenderSystem.enableBlend();

		//then we switch to icons
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		//RenderSystem.bindTexture(Minecraft.getInstance().getTextureManager().getTexture(InventoryMenu.BLOCK_ATLAS).getId());
		//buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

		//put the icons on the region buttons
		renderRadialButtonIcons(matrixStack, middle_x, middle_y);
		//put the icons on the sided buttons
		renderSidedButtonIcons(matrixStack, middle_x, middle_y);

		matrixStack.popPose();
	}

	private void renderAnyExtraInfoTexts(PoseStack matrixStack, int middle_x, int middle_y)
	{
		int leftSideX = 10;
		final int[] y = {(int) middle_y / 2};

		spiritweb.METALS_INGESTED.forEach((key, value) ->
		{
			if (value > 0)
			{
				//todo localisation check
				final String text = key.getName() + ": " + value;
				font.drawShadow(matrixStack, text, leftSideX, y[0], 0xffffffff);
				y[0] += 10;
			}
		});

		if (selectedManifestation == null)
		{
			return;
		}

		y[0] = (int) middle_y / 2;
		int rightSideX = middle_x + 35;

		font.drawShadow(matrixStack, I18n.get(selectedManifestation.translation().getKey()), rightSideX, y[0], 0xffffffff);
		//todo mode translation
		font.drawShadow(matrixStack, "Mode: " + spiritweb.getMode(selectedManifestation), rightSideX, y[0] + 10, 0xffffffff);

	}

	private void renderSidedButtonStrings(PoseStack matrixStack, double middle_x, double middle_y)
	{
		for (final SidedMenuButton sideButton : sidedMenuButtons)
		{
			//but only if that sided button is highlighted
			if (sideButton.highlighted)
			{
				final String text = I18n.get(sideButton.name);

				switch (sideButton.textSide)
				{
					case WEST:
						font.drawShadow(matrixStack, text, (int) (middle_x + sideButton.x1 - 8) - font.width(text), (int) (middle_y + sideButton.y1 + 6), 0xffffffff);
						break;
					case EAST:
						font.drawShadow(matrixStack, text, (int) (middle_x + sideButton.x2 + 8), (int) (middle_y + sideButton.y1 + 6), 0xffffffff);
						break;
					case UP:
						font.drawShadow(matrixStack, text, (int) (middle_x + (sideButton.x1 + sideButton.x2) * 0.5 - font.width(text) * 0.5), (int) (middle_y + sideButton.y1 - 14), 0xffffffff);
						break;
					case DOWN:
						font.drawShadow(matrixStack, text, (int) (middle_x + (sideButton.x1 + sideButton.x2) * 0.5 - font.width(text) * 0.5), (int) (middle_y + sideButton.y1 + 24), 0xffffffff);
						break;
				}

			}
		}
	}

	private void renderRadialButtonStrings(PoseStack matrixStack, int middle_x, int middle_y)
	{
		for (final RadialMenuButton button : radialMenuButtons)
		{
			//but only if that button is highlighted
			if (button.highlighted)
			{
				final double x = button.centerX;
				final double y = button.centerY;

				int fixed_x = (int) x;//(x + TEXT_DISTANCE);
				final int fixed_y = (int) y;//(y + TEXT_DISTANCE);

				//todo localisation check
				final String text = I18n.get(button.manifestation.translation().getKey());

				fixed_x = (int) (x < 0
				                 ? fixed_x - (font.width(text) + TEXT_DISTANCE)
				                 : fixed_x + TEXT_DISTANCE);

				font.drawShadow(matrixStack, text, middle_x + fixed_x, middle_y + fixed_y, 0xffffffff);

				//no need to keep searching, we only keep one highlighted at a time.
				break;
			}
		}
	}

	private void renderSidedButtonIcons(PoseStack matrixStack, double middleX, double middleY)
	{
		final StringBuilder stringBuilder = new StringBuilder();
		for (final SidedMenuButton button : sidedMenuButtons)
		{
			stringBuilder.setLength(0);
			final double x = (button.x1 + button.x2) / 2 + 0.01;
			final double y = (button.y1 + button.y2) / 2 + 0.01;

			stringBuilder
					.append("textures/icon/")
					.append(button.name)
					.append(".png");

			RenderSystem.setShaderTexture(0, ResourceLocationHelper.prefix(stringBuilder.toString()));
			blit(matrixStack, (int) (middleX + x - 8), (int) (middleY + y - 8), 16, 16, 0, 0, 18, 18, 18, 18);

		}
	}

	private void renderRadialButtonIcons(PoseStack matrixStack, double middleX, double middleY)
	{
		final StringBuilder stringBuilder = new StringBuilder();
		for (final RadialMenuButton menuRegion : radialMenuButtons)
		{
			stringBuilder.setLength(0);
			final double x = menuRegion.centerX;
			final double y = menuRegion.centerY;

			final SpriteIconPositioning sip = ClientHelper.instance.getIconForManifestation(menuRegion.manifestation);

			final double scalex = 15 * sip.width * 0.5;
			final double scaley = 15 * sip.height * 0.5;
			final double x1 = x - scalex;
			final double x2 = x + scalex;
			final double y1 = y - scaley;
			final double y2 = y + scaley;

			final TextureAtlasSprite sprite = sip.sprite;

			final float f = 1.0f;
			final float a = 1.0f;

			final double u1 = sip.left * 16.0;
			final double u2 = (sip.left + sip.width) * 16.0;
			final double v1 = sip.top * 16.0;
			final double v2 = (sip.top + sip.height) * 16.0;


			AManifestation mani = menuRegion.manifestation;
			final Manifestations.ManifestationTypes manifestationType = mani.getManifestationType();
			stringBuilder
					.append("textures/icon/")
					.append(manifestationType.getName())
					.append("/");

			switch (manifestationType)
			{
				case ALLOMANCY:
					AllomancyBase allomancyBase = (AllomancyBase) mani;
					stringBuilder.append(allomancyBase.getMetalType().getName());
					break;
				case FERUCHEMY:
					FeruchemyBase feruchemyBase = (FeruchemyBase) mani;
					stringBuilder.append(feruchemyBase.getMetalType().getName());
					break;
				case SURGEBINDING:
					break;
				case AON_DOR:
					break;
				case AWAKENING:
					break;
			}

			stringBuilder.append(".png");
			final ResourceLocation textureLocation = ResourceLocationHelper.prefix(stringBuilder.toString());
			RenderSystem.setShaderTexture(0, textureLocation);
			blit(matrixStack,
					(int) (middleX + x1),
					(int) (middleY + y1),
					16,
					16,
					0,
					0,
					18,
					18,
					18,
					18);

		}
	}

	private void renderSidedButtons(BufferBuilder buffer, double mouseVecX, double mouseVecY, double middle_x, double middle_y)
	{
		for (final SidedMenuButton button : sidedMenuButtons)
		{
			final float a = 0.5f;
			float f;

			if (button.x1 <= mouseVecX && button.x2 >= mouseVecX && button.y1 <= mouseVecY && button.y2 >= mouseVecY)
			{
				f = 1;
				button.highlighted = true;
				doAction = button;
			}
			else
			{
				button.highlighted = false;

				//highlight button, but don't draw string unless mouse over
				f = selectedPowerType.getID() == button.powerType
				    ? 1
				    : 0;
			}

			//set first triangle
			buffer.vertex(middle_x + button.x1, middle_y + button.y1, 0).color(f, f, f, a).endVertex();
			buffer.vertex(middle_x + button.x1, middle_y + button.y2, 0).color(f, f, f, a).endVertex();
			//set second triangle
			buffer.vertex(middle_x + button.x2, middle_y + button.y2, 0).color(f, f, f, a).endVertex();
			buffer.vertex(middle_x + button.x2, middle_y + button.y1, 0).color(f, f, f, a).endVertex();
		}
	}

	private void renderRadialButtons(BufferBuilder buffer, double mouseVecX, double mouseVecY, double middle_x, double middle_y)
	{
		if (!radialMenuButtons.isEmpty())
		{
			final float ring_inner_edge = -20;
			final float ring_outer_edge = -60;

			// todo test if I can get down to one button only
			final int totalButtons = radialMenuButtons.size();

			Vector2 innerEdge;
			Vector2 outerEdge;

			boolean smallMode = radialMenuButtons.size() == 2;

			innerEdge = new Vector2(0, ring_inner_edge);
			outerEdge = new Vector2(0, ring_outer_edge);

			int i = radialMenuButtons.size() - 1;
			while (i >= 0)
			{
				RadialMenuButton region = radialMenuButtons.get(i);
				//left side inner point
				double x1m1;
				double y1m1;

				//left side outer point
				double x2m1;
				double y2m1;

				//right side inner point
				double x1m2;
				double y1m2;
				//right side outer point
				double x2m2;
				double y2m2;

				if (smallMode)
				{
					//1
					x1m1 = outerEdge.x;
					y1m1 = outerEdge.y;
					outerEdge.Rotate(60);

					//2
					x2m1 = outerEdge.x;
					y2m1 = outerEdge.y;
					outerEdge.Rotate(60);

					//3
					x1m2 = outerEdge.x;
					y1m2 = outerEdge.y;
					outerEdge.Rotate(60);

				}
				else
				{

					//left side inner point
					x1m1 = innerEdge.x;
					y1m1 = innerEdge.y;

					//left side outer point
					x2m1 = outerEdge.x;
					y2m1 = outerEdge.y;

					//rotate vectors around the point
					innerEdge.Rotate(-(360f / totalButtons));
					outerEdge.Rotate(-(360f / totalButtons));

					//right side inner point
					x1m2 = innerEdge.x;
					y1m2 = innerEdge.y;

				}
				//4 or right side outer point
				x2m2 = outerEdge.x;
				y2m2 = outerEdge.y;

				//the center of a regular polygon can be found by adding up
				// all corner positions and divide by the total count
				region.centerX = (x1m1 + x2m1 + x1m2 + x2m2) / 4;
				region.centerY = (y1m1 + y2m1 + y1m2 + y2m2) / 4;


				final float a = 0.5f;
				float f = 0f;

				final boolean showHighlight =
						MathHelper.inTriangle(
								x1m1, y1m1,
								x2m2, y2m2,
								x2m1, y2m1,
								mouseVecX, mouseVecY)
								|| MathHelper.inTriangle(
								x1m1, y1m1,
								x1m2, y1m2,
								x2m2, y2m2,
								mouseVecX, mouseVecY);

				//if mouse is within the region, as defined by the two triangles
				//if (begin_rad <= mouseAngle && mouseAngle <= end_rad && showHighlight)
				if (showHighlight)
				{
					f = 0.1f;
					region.highlighted = true;
					selectedManifestation = region.manifestation;
				}
				else
				{
					region.highlighted = false;
				}

				float lerpPositive = 0;
				float lerpNegative = 0;

				if (region.manifestation != null)
				{
					int mode = region.manifestation.getMode(spiritweb);
					int modeMin = region.manifestation.modeMin(spiritweb);
					int modeMax = region.manifestation.modeMax(spiritweb);

					if (mode > 0)
					{
						lerpPositive = MathHelper.InverseLerp(0, modeMax, mode) - 0.1f;
					}
					else if (mode < 0)
					{
						lerpNegative = MathHelper.InverseLerp(0, Math.abs(modeMin), Math.abs(mode)) - 0.1f;
					}
				}

				float r = lerpPositive + f;
				float g = f;
				float b = lerpNegative + f;

				if (smallMode)
				{
					buffer.vertex(middle_x + x2m1, middle_y + y2m1, 0).color(r, g, b, a).endVertex();
					buffer.vertex(middle_x + x1m1, middle_y + y1m1, 0).color(r, g, b, a).endVertex();
				}
				else
				{
					//set the square pos
					buffer.vertex(middle_x + x1m1, middle_y + y1m1, 0).color(r, g, b, a).endVertex();
					buffer.vertex(middle_x + x2m1, middle_y + y2m1, 0).color(r, g, b, a).endVertex();
				}

				buffer.vertex(middle_x + x2m2, middle_y + y2m2, 0).color(r, g, b, a).endVertex();
				buffer.vertex(middle_x + x1m2, middle_y + y1m2, 0).color(r, g, b, a).endVertex();

				i--;
			}
		}
	}


}