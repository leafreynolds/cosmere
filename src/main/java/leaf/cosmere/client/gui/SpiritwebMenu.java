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
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.network.Network;
import leaf.cosmere.network.packets.ChangeManifestationModeMessage;
import leaf.cosmere.network.packets.DeactivateCurrentManifestationsMessage;
import leaf.cosmere.network.packets.SetSelectedManifestationMessage;
import leaf.cosmere.utils.helpers.MathHelper;
import leaf.cosmere.utils.math.Vector2;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static leaf.cosmere.registry.KeybindingRegistry.MANIFESTATION_MENU;

public class SpiritwebMenu extends Screen
{

	private final float TIME_SCALE = 0.01f;
	final double TEXT_DISTANCE = 30;

	public static final SpiritwebMenu instance = new SpiritwebMenu();
	private SpiritwebCapability spiritweb = null;

	private float visibility = 0.0f;
	private boolean canShow = true;
	private Stopwatch lastChange = Stopwatch.createStarted();
	public AManifestation switchToPower = null;
	public ButtonAction doAction = null;
	public boolean actionUsed = false;

	protected ArrayList<RadialButtonRegion> radialButtonRegions = new ArrayList<>();
	protected ArrayList<SidedMenuButton> sidedMenuButtons = new ArrayList<>();

	protected SpiritwebMenu()
	{
		super(new TextComponent("Menu"));
	}

	public boolean raiseVisibility()
	{
		if (!canShow())
		{
			return false;
		}

		visibility = MathHelper.clamp01(visibility + lastChange.elapsed(TimeUnit.MILLISECONDS) * TIME_SCALE);
		lastChange = Stopwatch.createStarted();
		return true;
	}

	public void decreaseVisibility()
	{
		setCanShow(true);
		visibility = MathHelper.clamp01(visibility - lastChange.elapsed(TimeUnit.MILLISECONDS) * TIME_SCALE);
		lastChange = Stopwatch.createStarted();
	}

	public boolean canShow()
	{
		return canShow;
	}

	public void setCanShow(final boolean canShow)
	{
		this.canShow = canShow;
	}

	public boolean isVisible()
	{
		return visibility > 0.001;
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
		this.spiritweb = spiritweb;
		final boolean wasVisible = isVisible();

		if (!MANIFESTATION_MENU.isUnbound() && MANIFESTATION_MENU.isDown())
		{
			actionUsed = false;
			if (raiseVisibility())
			{
				getMinecraft().mouseHandler.releaseMouse();
			}
		}
		else
		{
			if (!actionUsed)
			{
				if (switchToPower != null)
				{
					//todo open menu sound
					//ClientSide.instance.playRadialMenu();

					Network.sendToServer(new SetSelectedManifestationMessage(switchToPower));
				}

				if (doAction != null)
				{
					//todo open menu sound
					//ClientSide.instance.playRadialMenu();
					switch (doAction)
					{
/*                               case ROLL_X:
                            PacketRotateVoxelBlob pri = new PacketRotateVoxelBlob(Direction.Axis.X, Rotation.CLOCKWISE_90);
                            ChiselsAndBits.getNetworkChannel().sendToServer(pri);
                            break;*/

						case INACTIVE:
							//deactivate current
							if (spiritweb.canTickSelectedManifestation())
							{
								Network.sendToServer(new DeactivateCurrentManifestationsMessage());
							}
							break;
						case ACTIVE:
							//activate current
							if (!spiritweb.canTickSelectedManifestation())
							{
								//Network.sendToServer(new ToggleManifestationMessage());
							}
							break;
						case MODE_INCREASE:
							//change mode to positive
							Network.sendToServer(new ChangeManifestationModeMessage(spiritweb.manifestation(), 1));
							break;
						case MODE_DECREASE:
							//change mode to negative.
							Network.sendToServer(new ChangeManifestationModeMessage(spiritweb.manifestation(), -1));
							break;
					}
				}
			}

			actionUsed = true;
			decreaseVisibility();
		}

		if (isVisible())
		{
			final Window window = event.getWindow();
			init(Minecraft.getInstance(), window.getGuiScaledWidth(), window.getGuiScaledHeight());
			setScaledResolution(window.getGuiScaledWidth(), window.getGuiScaledHeight());

			if (!wasVisible)
			{
				getMinecraft().screen = SpiritwebMenu.instance;
				getMinecraft().mouseHandler.releaseMouse();
			}

			if (getMinecraft().mouseHandler.isMouseGrabbed())
			{
				KeyMapping.releaseAll();
			}
		}
		else
		{
			if (wasVisible)
			{
				getMinecraft().mouseHandler.grabMouse();
			}
		}
	}

	@Override
	public boolean mouseClicked(final double mouseX, final double mouseY, final int button)
	{
		this.visibility = 0f;
		canShow = false;
		this.minecraft.setScreen(null);

		if (this.minecraft.screen == null)
		{
			this.minecraft.setWindowActive(true);
			this.minecraft.getSoundManager().resume();
			this.minecraft.mouseHandler.grabMouse();
		}
		return true;
	}

	private static class SidedMenuButton
	{

		public double x1, x2;
		public double y1, y2;
		public boolean highlighted;

		public final ButtonAction action;
		public TextureAtlasSprite icon;
		public int color;
		public String name;
		public Direction textSide;

		public SidedMenuButton(
				final String name,
				final ButtonAction action,
				final double x,
				final double y,
				final TextureAtlasSprite ico,
				final Direction textSide)
		{
			this.name = name;
			this.action = action;
			x1 = x;
			x2 = x + 18;
			y1 = y;
			y2 = y + 18;
			icon = ico;
			color = 0xffffff;
			this.textSide = textSide;
		}

		public SidedMenuButton(
				final String name,
				final ButtonAction action,
				final double x,
				final double y,
				final int col,
				final Direction textSide)
		{
			this.name = name;
			this.action = action;
			x1 = x;
			x2 = x + 18;
			y1 = y;
			y2 = y + 18;
			color = col;
			this.textSide = textSide;
		}

	}

	static class RadialButtonRegion
	{

		public final AManifestation manifestation;
		public double centerX;
		public double centerY;
		public boolean highlighted;

		public RadialButtonRegion(final AManifestation manifestation)
		{
			this.manifestation = manifestation;
		}

	}

	protected void SetupButtons()
	{
		sidedMenuButtons.add(spiritweb.canTickSelectedManifestation()
		                     ? new SidedMenuButton("gui.cosmere.other.inactive", ButtonAction.INACTIVE, TEXT_DISTANCE + 20, -50, ClientHelper.off, Direction.WEST)
		                     : new SidedMenuButton("gui.cosmere.other.active", ButtonAction.ACTIVE, TEXT_DISTANCE + 20, -50, ClientHelper.on, Direction.WEST));

		sidedMenuButtons.add(new SidedMenuButton("gui.cosmere.mode.increase", ButtonAction.MODE_INCREASE, TEXT_DISTANCE * 2, -10, ClientHelper.arrowUp, Direction.EAST));
		sidedMenuButtons.add(new SidedMenuButton("gui.cosmere.mode.decrease", ButtonAction.MODE_DECREASE, TEXT_DISTANCE * 2, 10, ClientHelper.arrowDown, Direction.EAST));


		for (AManifestation manifestation : spiritweb.getAvailableManifestations())
		{
			radialButtonRegions.add(new RadialButtonRegion(manifestation));
		}
	}

	@Override
	public void render(final PoseStack matrixStack, final int mouseX, final int mouseY, final float partialTicks)
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
		//RenderSystem.disableAlphaTest();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		//RenderSystem.shadeModel(GL11.GL_SMOOTH);
		final Tesselator tessellator = Tesselator.getInstance();
		final BufferBuilder buffer = tessellator.getBuilder();

		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		final double mouseVecX = mouseX - width / 2f;
		final double mouseVecY = (mouseY - height / 2f);

		final double middle_x = width / 2f;
		final double middle_y = height / 2f;

		radialButtonRegions.clear();
		sidedMenuButtons.clear();

		SetupButtons();

		switchToPower = null;
		doAction = null;

		//render the button backgrounds
		renderRadialButtons(buffer, mouseVecX, mouseVecY, middle_x, middle_y);
		renderSidedButtons(buffer, mouseVecX, mouseVecY, middle_x, middle_y);

		//draw out what we've asked for
		tessellator.end();

		//then we switch to icons
		RenderSystem.enableTexture();
		RenderSystem.setShaderColor(1, 1, 1, 1.0f);
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.bindTexture(Minecraft.getInstance().getTextureManager().getTexture(InventoryMenu.BLOCK_ATLAS).getId());
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

		//put the icons on the region buttons
		renderRadialButtonIcons(buffer, middle_x, middle_y);
		//put the icons on the sided buttons
		renderSidedButtonIcons(buffer, middle_x, middle_y);

		tessellator.end();

		// draw radial button strings
		renderRadialButtonStrings(matrixStack, (int) middle_x, (int) middle_y);
		//draw sided button strings
		renderSidedButtonStrings(matrixStack, middle_x, middle_y);
		//do extra text info stuff
		renderAnyExtraInfoTexts(matrixStack, (int) middle_y);

		matrixStack.popPose();
	}

	private void renderAnyExtraInfoTexts(PoseStack matrixStack, int middle_y)
	{
		int x = 10;
		final int[] y = {middle_y / 2};

		spiritweb.METALS_INGESTED.forEach((key, value) ->
		{
			if (value > 0)
			{
				//todo localisation check
				final String text = key.getName() + ": " + value;
				font.drawShadow(matrixStack, text, x, y[0], 0xffffffff);
				y[0] += 10;
			}
		});
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
		for (final RadialButtonRegion button : radialButtonRegions)
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

	private void renderSidedButtonIcons(BufferBuilder buffer, double middle_x, double middle_y)
	{
		for (final SidedMenuButton button : sidedMenuButtons)
		{
			final float f = switchToPower == null ? 1.0f : 0.5f;
			final float a = 1.0f;

			final double u1 = 0;
			final double u2 = 16;
			final double v1 = 0;
			final double v2 = 16;

			final TextureAtlasSprite sprite = button.icon == null ? ClientHelper.blank : button.icon;

			final double btnx1 = button.x1 + 1;
			final double btnx2 = button.x2 - 1;
			final double btny1 = button.y1 + 1;
			final double btny2 = button.y2 - 1;

			final float red = f * ((button.color >> 16 & 0xff) / 255.0f);
			final float green = f * ((button.color >> 8 & 0xff) / 255.0f);
			final float blue = f * ((button.color & 0xff) / 255.0f);

			buffer.vertex(middle_x + btnx1, middle_y + btny1, 0).uv(sprite.getU(u1), sprite.getV(v1)).color(red, green, blue, a).endVertex();
			buffer.vertex(middle_x + btnx1, middle_y + btny2, 0).uv(sprite.getU(u1), sprite.getV(v2)).color(red, green, blue, a).endVertex();
			buffer.vertex(middle_x + btnx2, middle_y + btny2, 0).uv(sprite.getU(u2), sprite.getV(v2)).color(red, green, blue, a).endVertex();
			buffer.vertex(middle_x + btnx2, middle_y + btny1, 0).uv(sprite.getU(u2), sprite.getV(v1)).color(red, green, blue, a).endVertex();
		}
	}

	private void renderRadialButtonIcons(BufferBuilder buffer, double middle_x, double middle_y)
	{
		for (final RadialButtonRegion mnuRgn : radialButtonRegions)
		{
			final double x = mnuRgn.centerX;
			final double y = mnuRgn.centerY;

			final SpriteIconPositioning sip = ClientHelper.instance.getIconForManifestation(mnuRgn.manifestation);

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

			buffer.vertex(middle_x + x1, middle_y + y1, 0).uv(sprite.getU(u1), sprite.getV(v1)).color(f, f, f, a).endVertex();
			buffer.vertex(middle_x + x1, middle_y + y2, 0).uv(sprite.getU(u1), sprite.getV(v2)).color(f, f, f, a).endVertex();
			buffer.vertex(middle_x + x2, middle_y + y2, 0).uv(sprite.getU(u2), sprite.getV(v2)).color(f, f, f, a).endVertex();
			buffer.vertex(middle_x + x2, middle_y + y1, 0).uv(sprite.getU(u2), sprite.getV(v1)).color(f, f, f, a).endVertex();
		}
	}

	private void renderSidedButtons(BufferBuilder buffer, double mouseVecX, double mouseVecY, double middle_x, double middle_y)
	{
		for (final SidedMenuButton button : sidedMenuButtons)
		{
			final float a = 0.5f;
			float f = 0f;

			if (button.x1 <= mouseVecX && button.x2 >= mouseVecX && button.y1 <= mouseVecY && button.y2 >= mouseVecY)
			{
				f = 1;
				button.highlighted = true;
				doAction = button.action;
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
		if (!radialButtonRegions.isEmpty())
		{

			final float ring_inner_edge = -10;
			final float ring_outer_edge = -50;

			// todo test if I can get down to one button only
			final int totalButtons = radialButtonRegions.size();

			Vector2 innerEdge;
			Vector2 outerEdge;

			boolean smallMode = radialButtonRegions.size() == 2;

			innerEdge = new Vector2(0, ring_inner_edge);
			outerEdge = new Vector2(0, ring_outer_edge);


			for (final RadialButtonRegion region : radialButtonRegions)
			{
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

					//4
					x2m2 = outerEdge.x;
					y2m2 = outerEdge.y;
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
					//right side outer point
					x2m2 = outerEdge.x;
					y2m2 = outerEdge.y;

				}

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
					f = 1;
					region.highlighted = true;
					switchToPower = region.manifestation;
				}


				if (smallMode)
				{
					buffer.vertex(middle_x + x2m1, middle_y + y2m1, 0).color(f, f, f, a).endVertex();
					buffer.vertex(middle_x + x1m1, middle_y + y1m1, 0).color(f, f, f, a).endVertex();
					buffer.vertex(middle_x + x2m2, middle_y + y2m2, 0).color(f, f, f, a).endVertex();
					buffer.vertex(middle_x + x1m2, middle_y + y1m2, 0).color(f, f, f, a).endVertex();
				}
				else
				{
					//set the square pos
					buffer.vertex(middle_x + x1m1, middle_y + y1m1, 0).color(f, f, f, a).endVertex();
					buffer.vertex(middle_x + x2m1, middle_y + y2m1, 0).color(f, f, f, a).endVertex();
					buffer.vertex(middle_x + x2m2, middle_y + y2m2, 0).color(f, f, f, a).endVertex();
					buffer.vertex(middle_x + x1m2, middle_y + y1m2, 0).color(f, f, f, a).endVertex();
				}

			}
		}
	}


}