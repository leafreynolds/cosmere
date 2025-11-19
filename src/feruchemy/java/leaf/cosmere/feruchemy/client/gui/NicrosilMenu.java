/*
 * File updated ~ 10 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.client.gui;

import com.google.common.base.Stopwatch;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import leaf.cosmere.api.IHasManifestations;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.client.gui.ButtonAction;
import leaf.cosmere.client.gui.SpiritwebMenu;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.packets.SetSelectedManifestationMessage;
import leaf.cosmere.common.network.packets.StoreTapManifestationMessage;
import leaf.cosmere.feruchemy.client.gui.guiitems.SpiritwebButtonContainer;
import leaf.cosmere.feruchemy.client.gui.guiitems.SpiritwebPowerButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class NicrosilMenu extends Screen
{
	public static final NicrosilMenu instance = new NicrosilMenu();
	final double TEXT_DISTANCE = 30;
	private final List<String> m_infoText = new ArrayList<>();
	public Manifestation selectedManifestation = null;
	public SidedMenuButton doAction = null;

	protected ArrayList<SpiritwebButtonContainer> ringMenus = new ArrayList<>();
	protected ArrayList<SpiritwebButtonContainer> braceletMenus = new ArrayList<>();
	protected ArrayList<SpiritwebButtonContainer> necklaceMenus = new ArrayList<>();
	protected ArrayList<SpiritwebPowerButton> spiritwebPowerButtons = new ArrayList<>();

	protected ArrayList<PlayerSpiritwebPowerButton> playerSpiritwebPowerButtons = new ArrayList<>();
	protected ArrayList<SidedMenuButton> sidedMenuButtons = new ArrayList<>();

	private SpiritwebCapability spiritweb = null;
	private boolean closed = true;
	private float visibility = 0.0f;
	private Stopwatch lastChange = Stopwatch.createStarted();
	private Manifestations.ManifestationTypes selectedPowerType = Manifestations.ManifestationTypes.ALLOMANCY;
	private TransferredPower heldButton = null;

	protected NicrosilMenu()
	{
		super(Component.literal("Menu"));
		this.minecraft = getMinecraft();
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
	public @NotNull Minecraft getMinecraft()
	{
		return Minecraft.getInstance();
	}

	public void postRender()
	{
		if (this.minecraft == null)
		{
			return;
		}
		if (this.minecraft.screen == NicrosilMenu.instance)
		{
			if (this.closed)
			{
				final Window window = this.minecraft.getWindow();
				init(this.minecraft, window.getGuiScaledWidth(), window.getGuiScaledHeight());
				setScaledResolution(window.getGuiScaledWidth(), window.getGuiScaledHeight());

				this.spiritweb = SpiritwebMenu.instance.getSpiritweb();

				//no need to set if it's already open
				//this.minecraft.setScreen(NicrosilMenu.instance);
				visibility = 0;
				lastChange = Stopwatch.createStarted();

				selectedManifestation = this.spiritweb.getSelectedManifestation();

				setupButtons();
				this.closed = false;
			}

			raiseVisibility();
		}
	}

	@Override
	public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers)
	{
		if (Keybindings.MANIFESTATION_MENU.matches(pKeyCode, pScanCode))
		{
			//closeScreen();
		}
		return super.keyReleased(pKeyCode, pScanCode, pModifiers);
	}

	@Override
	public boolean mouseClicked(final double mouseX, final double mouseY, final int button)
	{

		if (heldButton != null)
		{
			for(SpiritwebPowerButton spiritwebPowerButton : spiritwebPowerButtons)
			{
				if(spiritwebPowerButton.highlight)
				{
					//spiritwebPowerButton.setManifestation(heldButton.powerButton.manifestation);
					//playerSpiritwebPowerButtons.remove(heldButton.powerButton);

					if (spiritweb.getLiving() instanceof Player player)
					{
						LazyOptional<ICuriosItemHandler> curiosItemHandler = CuriosApi.getCuriosInventory(player);
						if (curiosItemHandler.resolve().isPresent())
						{
							ICuriosItemHandler itemHandler = curiosItemHandler.resolve().get();
							if (itemHandler.getEquippedCurios().getStackInSlot(spiritwebPowerButton.getContainer().curioItemSlot).getItem() instanceof IHasManifestations)
							{
								final Attribute attribute = heldButton.powerButton.manifestation.getAttribute();
								AttributeInstance manifestationAttribute = player.getAttribute(attribute);
								Cosmere.packetHandler().sendToServer(new StoreTapManifestationMessage(
										heldButton.powerButton.manifestation,
										manifestationAttribute.getBaseValue(),
										spiritwebPowerButton.getContainer().curioItemSlot,
										true));
								spiritwebPowerButton.setManifestation(heldButton.powerButton.manifestation);
								spiritwebPowerButton.setStrength((int) manifestationAttribute.getBaseValue());
								playerSpiritwebPowerButtons.remove(heldButton.powerButton);
							}
						}
					}
				}
			}
			heldButton = null;
		}
		else
		{
			for(SpiritwebPowerButton spiritwebPowerButton : spiritwebPowerButtons)
			{
				if(spiritwebPowerButton.highlight && spiritwebPowerButton.getManifestation() != null)
				{
					//playerSpiritwebPowerButtons.add(new PlayerSpiritwebPowerButton(spiritwebPowerButton.getManifestation()));
					//spiritwebPowerButton.setManifestation(null);
					if (spiritweb.getLiving() instanceof Player player)
					{
						LazyOptional<ICuriosItemHandler> curiosItemHandler = CuriosApi.getCuriosInventory(player);
						if (curiosItemHandler.resolve().isPresent())
						{
							ICuriosItemHandler itemHandler = curiosItemHandler.resolve().get();
							if (itemHandler.getEquippedCurios().getStackInSlot(spiritwebPowerButton.getContainer().curioItemSlot).getItem() instanceof IHasManifestations)
							{
								Cosmere.packetHandler().sendToServer(new StoreTapManifestationMessage(
										spiritwebPowerButton.getManifestation(),
										spiritwebPowerButton.getStrength(),
										spiritwebPowerButton.getContainer().curioItemSlot,
										false));
								spiritwebPowerButtons.remove(spiritwebPowerButton);
								playerSpiritwebPowerButtons.add(new PlayerSpiritwebPowerButton(spiritwebPowerButton.getManifestation()));
							}
						}
					}


				}
			}

			for (PlayerSpiritwebPowerButton playerSpiritwebPowerButton : playerSpiritwebPowerButtons)
			{
				if (playerSpiritwebPowerButton.highlighted)
				{
					heldButton = new TransferredPower(playerSpiritwebPowerButton, playerSpiritwebPowerButton.strength);
				}
			}

			for (SidedMenuButton sidedMenuButton : sidedMenuButtons)
			{
				if (sidedMenuButton.highlighted)
				{
					if (heldButton == null)
					{
						if (sidedMenuButton.powerType != -1)
						{
							selectedPowerType = Manifestations.ManifestationTypes.valueOf(doAction.powerType).get();
							setupButtons();
						}
						else if (sidedMenuButton.action != null)
						{
							//do other action
						}
					}
					else
					{
						heldButton = null;
					}

					return true;
				}
			}
		}

		return true;
	}

	@Override
	public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta)
	{
		//1 = drop down
		//-1 = pick up
		System.out.println(pDelta);
		return true;
	}

	public void closeScreen()
	{
		this.closed = true;
		this.minecraft.setScreen(null);
	}

	protected void setupButtons()
	{
		playerSpiritwebPowerButtons.clear();
		spiritwebPowerButtons.clear();
		sidedMenuButtons.clear();
		necklaceMenus.clear();
		braceletMenus.clear();
		ringMenus.clear();

		if (spiritweb.getLiving() instanceof Player player)
		{
			LazyOptional<ICuriosItemHandler> curiosItemHandler = CuriosApi.getCuriosInventory(player);
			if (curiosItemHandler.resolve().isPresent())
			{
				ICuriosItemHandler itemHandler = curiosItemHandler.resolve().get();

				for (int i = 0; i < itemHandler.getSlots(); i++)
				{
					if (itemHandler.getEquippedCurios().getStackInSlot(i).getItem() instanceof IHasManifestations item)
					{
						SpiritwebButtonContainer spiritwebContainer = new SpiritwebButtonContainer(0, 0, 1, 1, i);
						Manifestation[] manifestations = item.getManifestations(itemHandler.getEquippedCurios().getStackInSlot(i));
						Integer[] manifestationStrengths = item.getManifestationStrengths(itemHandler.getEquippedCurios().getStackInSlot(i));
						for (int j = 0; j < item.getMaxCapacity(); j++)
						{
							SpiritwebPowerButton spiritwebPowerButton = new SpiritwebPowerButton(0, 0, spiritweb, spiritwebContainer);
							spiritwebPowerButton.setManifestation(manifestations[j]);
							spiritwebPowerButton.setStrength(manifestationStrengths[j]);
							spiritwebPowerButtons.add(spiritwebPowerButton);
							spiritwebContainer.addButton(spiritwebPowerButton);
						}

						switch(item.getMaxCapacity())
						{
							case 3:
								necklaceMenus.add(spiritwebContainer);
								break;
							case 2:
								braceletMenus.add(spiritwebContainer);
								break;
							case 1:
								ringMenus.add(spiritwebContainer);
								break;
							default:
								break;
						}
					}
				}
			}

		}

		final List<Manifestation> availableManifestations = spiritweb.getAvailableManifestations();
		Set<Manifestations.ManifestationTypes> foundPowerTypes = new HashSet<>();

		for (Manifestation manifestation : availableManifestations)
		{
			if (manifestation.getManifestationType() == selectedPowerType)
			{
				if (manifestation.getStrength(spiritweb, true) > 0)
				{
					playerSpiritwebPowerButtons.add(new PlayerSpiritwebPowerButton(manifestation));
				}
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

	@Override
	public void render(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTicks)
	{
		PoseStack matrixStack = guiGraphics.pose();
		if (spiritweb == null)
		{
			return;
		}

		matrixStack.pushPose();

		final int start = (int) (visibility * 98) << 24;
		final int end = (int) (visibility * 128) << 24;

		guiGraphics.fillGradient(0, 0, width, height, start, end);

		//RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		final Tesselator tessellator = Tesselator.getInstance();
		final BufferBuilder buffer = tessellator.getBuilder();

		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		final double mouseVecX = mouseX - width / 2f;
		final double mouseVecY = (mouseY - height / 2f);

		final double middleX = width / 2f;
		final double middleY = height / 2f;

		selectedManifestation = null;
		doAction = null;


		renderSpiritwebMenuContainer(buffer, ringMenus, mouseX, mouseY, middleX, middleY);
		renderSpiritwebMenuContainer(buffer, braceletMenus, mouseX, mouseY, middleX, middleY);
		renderSpiritwebMenuContainer(buffer, necklaceMenus, mouseX, mouseY, middleX, middleY);

		renderSpiritwebButtons(buffer, mouseVecX, mouseVecY, middleX, middleY);
		renderSidedButtons(buffer, mouseVecX, mouseVecY, middleX, middleY);

		tessellator.end();

		matrixStack.pushPose();
		for(SpiritwebPowerButton spiritwebPowerButton : spiritwebPowerButtons)
		{
			spiritwebPowerButton.renderIcon(guiGraphics);
		}
		matrixStack.popPose();

		drawIcons(guiGraphics, buffer, middleX, middleY);
		matrixStack.popPose();
	}

	private void renderSpiritwebMenuContainer(BufferBuilder buffer, ArrayList<SpiritwebButtonContainer> spiritwebContainers, double mouseVecX, double mouseVecY, double middleX, double middleY) {
		for (int i = 0; i < spiritwebContainers.size(); i++)
		{
			double yOffset = spiritwebContainers.get(i).getContainWidth() - 1;
			double height = spiritwebContainers.get(i).getHeight();

			double xOffset = ((spiritwebContainers.size() * spiritwebContainers.get(i).getWidth()) + ((spiritwebContainers.size() - 1) * 20)) / spiritwebContainers.size();

			spiritwebContainers.get(i).renderContainer(buffer, mouseVecX, mouseVecY, middleX + (xOffset * (i - ((spiritwebContainers.size() - 1) / 2f))), middleY + (yOffset * (height + 20)));
		}
	}

	private void drawIcons(@NotNull GuiGraphics guiGraphics, BufferBuilder buffer, double middle_x, double middle_y)
	{
		PoseStack matrixStack = guiGraphics.pose();
		matrixStack.pushPose();
		//RenderSystem.enableTexture();
		RenderSystem.enableBlend();

		//then we switch to icons
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);

		//put the icons on the region buttons
		renderSpiritwebButtonIcons(guiGraphics, middle_x, middle_y);
		//put the icons on the sided buttons
		renderSidedButtonIcons(guiGraphics, middle_x, middle_y);

		matrixStack.popPose();
	}

	private void renderAnyExtraInfoTexts(GuiGraphics guiGraphics, int middle_x, int middle_y)
	{
		int leftSideX = 10;
		final int[] y = {middle_y / 2};

		if (selectedPowerType == Manifestations.ManifestationTypes.SANDMASTERY)
		{
			m_infoText.clear();

			for (ISpiritwebSubmodule spiritwebSubmodule : spiritweb.getSubmodules().values())
			{
				spiritwebSubmodule.collectMenuInfo(m_infoText);
			}

			for (String s : m_infoText)
			{
				if (s.toLowerCase().contains("hydration"))
				{
					guiGraphics.drawString(font, s, leftSideX, y[0], 0xffffffff);
					y[0] += 10;
				}
			}
		}

		if (selectedManifestation == null)
		{
			return;
		}

		y[0] = middle_y / 2;
		int rightSideX = middle_x + 35;

		guiGraphics.drawString(font, I18n.get(selectedManifestation.getTranslationKey()), rightSideX, y[0], 0xffffffff);
		guiGraphics.drawString(font, "Mode: " + spiritweb.getMode(selectedManifestation), rightSideX, y[0] + 10, 0xffffffff);

	}

	private void renderSidedButtonStrings(GuiGraphics guiGraphics, double middle_x, double middle_y)
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
						guiGraphics.drawString(font, text, (int) (middle_x + sideButton.x1 - 8) - font.width(text), (int) (middle_y + sideButton.y1 + 6), 0xffffffff);
						break;
					case EAST:
						guiGraphics.drawString(font, text, (int) (middle_x + sideButton.x2 + 8), (int) (middle_y + sideButton.y1 + 6), 0xffffffff);
						break;
					case UP:
						guiGraphics.drawString(font, text, (int) (middle_x + (sideButton.x1 + sideButton.x2) * 0.5 - font.width(text) * 0.5), (int) (middle_y + sideButton.y1 - 14), 0xffffffff);
						break;
					case DOWN:
						guiGraphics.drawString(font, text, (int) (middle_x + (sideButton.x1 + sideButton.x2) * 0.5 - font.width(text) * 0.5), (int) (middle_y + sideButton.y1 + 24), 0xffffffff);
						break;
				}

			}
		}
	}

	private void renderRadialButtonStrings(GuiGraphics guiGraphics, int middle_x, int middle_y)
	{
		for (final PlayerSpiritwebPowerButton button : playerSpiritwebPowerButtons)
		{
			//but only if that button is highlighted
			if (button.highlighted)
			{
				final double x = button.centerX;
				final double y = button.centerY;

				int fixed_x = (int) x;//(x + TEXT_DISTANCE);
				final int fixed_y = (int) y + 20;//(y + TEXT_DISTANCE);

				final String text = I18n.get(button.manifestation.getTranslationKey());

				fixed_x = x > 0
				          ? fixed_x - (font.width(text) + 10)
				          : fixed_x + 10;

				guiGraphics.drawString(font, text, middle_x + fixed_x, middle_y + fixed_y, 0xffffffff);

				break;
			}
		}
	}

	private void renderSidedButtonIcons(GuiGraphics guiGraphics, double middleX, double middleY)
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

			guiGraphics.blit(new ResourceLocation(button.name, stringBuilder.toString()), (int) (middleX + x - 8), (int) (middleY + y - 8), 16, 16, 0, 0, 18, 18, 18, 18);

		}
	}

	private void renderSpiritwebButtonIcons(GuiGraphics guiGraphics, double middleX, double middleY)
	{


		final StringBuilder stringBuilder = new StringBuilder();
		for (final PlayerSpiritwebPowerButton menuRegion : playerSpiritwebPowerButtons)
		{
			stringBuilder.setLength(0);
			final double x = menuRegion.centerX;
			final double y = menuRegion.centerY;

			final double scalex = 15 * 0.5;
			final double scaley = 15 * 0.5;
			final double x1 = x - scalex;
			final double y1 = y - scaley;

			Manifestation mani = menuRegion.manifestation;
			final Manifestations.ManifestationTypes manifestationType = mani.getManifestationType();
			String manifestationTypeName = manifestationType.getName();
			stringBuilder
					.append("textures/icon/")
					.append(manifestationTypeName)
					.append("/");

			switch (manifestationType)
			{
				case ALLOMANCY:
				case FERUCHEMY:
					if (mani instanceof IHasMetalType metalType)
					{
						stringBuilder.append(metalType.getMetalType().getName());
					}
					break;
				case SURGEBINDING:
					stringBuilder.append(mani.getName());
					break;
				case AON_DOR:
					break;
				case AWAKENING:
					break;
			}

			stringBuilder.append(".png");
			final ResourceLocation textureLocation = new ResourceLocation(mani.getRegistryName().getNamespace(), stringBuilder.toString());
			RenderSystem.setShaderTexture(0, textureLocation);
			guiGraphics.blit(textureLocation,
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

	private void renderSpiritwebButtons(BufferBuilder buffer, double mouseVecX, double mouseVecY, double middle_x, double middle_y)
	{
		if (!playerSpiritwebPowerButtons.isEmpty())
		{
			int buttonAmount = playerSpiritwebPowerButtons.size() - 1;
			int i = buttonAmount;
			while (i >= 0)
			{
				PlayerSpiritwebPowerButton region = playerSpiritwebPowerButtons.get(i);
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


				if (heldButton != null && heldButton.powerButton == region)
				{
					//Left side upper
					x1m1 = mouseVecX - 10;
					y1m1 = mouseVecY - 10;

					//Left side downer
					x2m1 = mouseVecX - 10;
					y2m1 = mouseVecY + 10;

					//Right side upper
					x1m2 = mouseVecX + 10;
					y1m2 = mouseVecY - 10;

					//Right side downer
					x2m2 = mouseVecX + 10;
					y2m2 = mouseVecY + 10;
				}
				else
				{
					final int positionAdjustX = 0;
					final int positionAdjustY = -50;
					//Left side upper
					x1m1 = (25 * (i - (double) buttonAmount / 2)) - 10 + positionAdjustX;
					y1m1 = -10 + positionAdjustY;

					//Left side downer
					x2m1 = (25 * (i - (double) buttonAmount / 2)) - 10 + positionAdjustX;
					y2m1 = 10 + positionAdjustY;

					//Right side Upper
					x1m2 = (25 * (i - (double) buttonAmount / 2)) + 10 + positionAdjustX;
					y1m2 = -10 + positionAdjustY;

					//Right side Downer
					x2m2 = (25 * (i - (double) buttonAmount / 2)) + 10 + positionAdjustX;
					y2m2 = 10 + positionAdjustY;
				}


				//the center of a regular polygon can be found by adding up
				// all corner positions and divide by the total count
				region.centerX = (x1m1 + x2m1 + x1m2 + x2m2) / 4;
				region.centerY = (y1m1 + y2m1 + y1m2 + y2m2) / 4;

				float opacity;
				float brightness = 0f;

				final boolean showHighlight;

				showHighlight = MathHelper.inTriangle(
						x1m1, y1m1,
						x2m2, y2m2,
						x2m1, y2m1,
						mouseVecX, mouseVecY)
						|| MathHelper.inTriangle(
						x1m1, y1m1,
						x1m2, y1m2,
						x2m2, y2m2,
						mouseVecX, mouseVecY);

				if (showHighlight)
				{
					brightness = 0.2f;
					region.highlighted = true;
					selectedManifestation = region.manifestation;
				}
				else
				{
					region.highlighted = false;
				}

				float r = brightness;
				float g = brightness;
				float b = brightness;

				opacity = 0.5f;

				{
					//set the square pos
					buffer.vertex(middle_x + x1m1, middle_y + y1m1, 0).color(r, g, b, opacity).endVertex();
					buffer.vertex(middle_x + x2m1, middle_y + y2m1, 0).color(r, g, b, opacity).endVertex();

					buffer.vertex(middle_x + x2m2, middle_y + y2m2, 0).color(r, g, b, opacity).endVertex();
					buffer.vertex(middle_x + x1m2, middle_y + y1m2, 0).color(r, g, b, opacity).endVertex();
				}

				i--;
			}
		}
	}

	private static class SidedMenuButton
	{

		public final ButtonAction action;
		public final int powerType;
		public double x1, x2;
		public double y1, y2;
		public boolean highlighted;
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

	static class PlayerSpiritwebPowerButton
	{
		public double centerX;
		public double centerY;
		public boolean highlighted;
		public double strength;
		public final Manifestation manifestation;

		public PlayerSpiritwebPowerButton(final Manifestation manifestation)
		{
			this.manifestation = manifestation;
			this.strength = manifestation.getStrength(instance.spiritweb, true);

		}
	}

	static class TransferredPower
	{
		public PlayerSpiritwebPowerButton powerButton;
		public double strength;

		public TransferredPower(PlayerSpiritwebPowerButton powerButton, double initialAmount)
		{
			this.powerButton = powerButton;
			this.strength = initialAmount;
		}

	}


}