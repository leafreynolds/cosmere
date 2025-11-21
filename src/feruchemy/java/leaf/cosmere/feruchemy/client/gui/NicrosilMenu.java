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
import leaf.cosmere.api.Manifestations.ManifestationTypes;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.client.gui.ButtonAction;
import leaf.cosmere.client.gui.ISyncSpiritweb;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
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
import net.minecraft.world.item.Item;
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

public class NicrosilMenu extends Screen implements ISyncSpiritweb
{
	public static final NicrosilMenu instance = new NicrosilMenu();
	static final double TEXT_DISTANCE = 30;
	public SidedMenuButton doAction = null;
	boolean syncLock = false;

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
	private ManifestationTypes selectedPowerType = ManifestationTypes.ALLOMANCY;
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

	public void postRender(SpiritwebCapability spiritweb)
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

				this.spiritweb = spiritweb;

				//no need to set if it's already open
				//this.minecraft.setScreen(NicrosilMenu.instance);
				visibility = 0;
				lastChange = Stopwatch.createStarted();

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
		if(syncLock) return false;

		if (heldButton != null)
		{
			for (SpiritwebPowerButton spiritwebPowerButton : spiritwebPowerButtons)
			{
				if (spiritwebPowerButton.highlight)
				{
					if(spiritwebPowerButton.getManifestation() == null)
					{
						if (spiritweb.getLiving() instanceof Player player)
						{
							LazyOptional<ICuriosItemHandler> curiosItemHandler = CuriosApi.getCuriosInventory(player);
							if (curiosItemHandler.resolve().isPresent())
							{
								ICuriosItemHandler itemHandler = curiosItemHandler.resolve().get();
								if (itemHandler.getEquippedCurios()
										.getStackInSlot(spiritwebPowerButton.getContainer().curioItemSlot)
										.getItem() instanceof IHasManifestations)
								{
									final Attribute attribute = heldButton.manifestation.getAttribute();
									AttributeInstance manifestationAttribute = player.getAttribute(attribute);

									// 1) send to server (real change)
									Cosmere.packetHandler().sendToServer(new StoreTapManifestationMessage(
											heldButton.manifestation,
											manifestationAttribute.getBaseValue(),
											spiritwebPowerButton.getContainer().curioItemSlot,
											true));

									// 2) *local* instant UI update (prediction)
									applyLocalStore(heldButton, spiritwebPowerButton);

									// 3) clear drag state
									heldButton = null;
									return true;
								}
							}
						}
					}
				}
			}
			heldButton = null;
		}
		else
		{
			for (SpiritwebPowerButton spiritwebPowerButton : spiritwebPowerButtons)
			{
				if (spiritwebPowerButton.highlight && spiritwebPowerButton.getManifestation() != null)
				{
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
								applyLocalTap(spiritwebPowerButton);
								return true;
							}
						}
					}
				}
			}

			for (PlayerSpiritwebPowerButton playerSpiritwebPowerButton : playerSpiritwebPowerButtons)
			{
				if (playerSpiritwebPowerButton.highlighted)
				{
					heldButton = new TransferredPower(playerSpiritwebPowerButton.manifestation, playerSpiritwebPowerButton.strength);
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
							selectedPowerType = ManifestationTypes.valueOf(sidedMenuButton.powerType).get();
							setupButtons();
							break;
						}
					}
				}
			}

		}
		return true;
	}

	@Override
	public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta)
	{
		return true;
	}

	public void onSpiritwebUpdated(SpiritwebCapability cap)
	{
		this.spiritweb = cap;
		this.syncLock = false;
	}

	public void closeScreen()
	{
		this.closed = true;
		this.minecraft.setScreen(null);
	}

	private void applyLocalStore(TransferredPower held, SpiritwebPowerButton targetSlot)
	{
		syncLock = true;
		playerSpiritwebPowerButtons.removeIf(btn -> btn.manifestation == held.manifestation);


		if (targetSlot.getManifestation() == null)
		{
			targetSlot.setManifestation(held.manifestation);
			targetSlot.setStrength((int) held.strength);
		}

		/*
		SpiritwebButtonContainer container = targetSlot.getContainer();
		for (SpiritwebPowerButton btn : spiritwebPowerButtons)
		{
			if (btn.getContainer() == container && btn.getManifestation() == null)
			{
				btn.setManifestation(held.manifestation);
				btn.setStrength((int) held.strength);
				return;
			}
		}
		*/

		if(playerSpiritwebPowerButtons.isEmpty())
		{
			final List<Manifestation> availableManifestations = spiritweb.getAvailableManifestations();
			availableManifestations.remove(held.manifestation);
			playerSpiritwebPowerButtons.clear();
			sidedMenuButtons.clear();
			selectedPowerType = held.manifestation.getManifestationType();
			setupManifestationButtons(availableManifestations, held.strength);
		}
	}

	private void applyLocalTap(SpiritwebPowerButton fromSlot)
	{
		syncLock = true;
		Manifestation mani = fromSlot.getManifestation();
		double strength = fromSlot.getStrength();

		fromSlot.setManifestation(null);
		fromSlot.setStrength(0);

		for (PlayerSpiritwebPowerButton btn : playerSpiritwebPowerButtons)
		{
			if (btn.manifestation == mani)
			{
				btn.strength = strength;
				return;
			}
		}
		playerSpiritwebPowerButtons.add(new PlayerSpiritwebPowerButton(mani, strength));

		final List<Manifestation> availableManifestations = spiritweb.getAvailableManifestations();
		for(Manifestation availableManifestation : availableManifestations)
		{
			if(availableManifestation.getManifestationType() == mani.getManifestationType()) return;
		}

		playerSpiritwebPowerButtons.clear();
		sidedMenuButtons.clear();
		availableManifestations.add(mani);
		selectedPowerType = mani.getManifestationType();
		setupManifestationButtons(availableManifestations, strength);
	}

	protected void setupButtons()
	{
		spiritwebPowerButtons.clear();
		necklaceMenus.clear();
		braceletMenus.clear();
		ringMenus.clear();
		playerSpiritwebPowerButtons.clear();
		sidedMenuButtons.clear();

		final List<Manifestation> availableManifestations = spiritweb.getAvailableManifestations();
		setupManifestationButtons(availableManifestations, 0);

		setupSpiritwebButtons();
	}

	private void setupSpiritwebButtons()
	{
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
						final double middleX = width / 2f;
						final double middleY = height / 2f;
						SpiritwebButtonContainer spiritwebContainer = new SpiritwebButtonContainer(middleX, middleY, 1, 1, i, (Item) item);
						Manifestation[] manifestations = item.getManifestations(itemHandler.getEquippedCurios().getStackInSlot(i));
						Integer[] manifestationStrengths = item.getManifestationStrengths(itemHandler.getEquippedCurios().getStackInSlot(i));
						for (int j = 0; j < item.getMaxCapacity(); j++)
						{
							SpiritwebPowerButton spiritwebPowerButton = new SpiritwebPowerButton(middleX, middleY, spiritweb, spiritwebContainer);
							spiritwebPowerButton.setManifestation(manifestations[j]);
							spiritwebPowerButton.setStrength(manifestationStrengths[j]);
							spiritwebPowerButtons.add(spiritwebPowerButton);
							spiritwebContainer.addButton(spiritwebPowerButton);
						}

						switch (item.getMaxCapacity())
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
	}

	private void setupManifestationButtons(List<Manifestation> manifestations, double strength)
	{
		Set<ManifestationTypes> foundPowerTypes = new HashSet<>();

		for (Manifestation manifestation : manifestations)
		{
			if (manifestation.getManifestationType() == selectedPowerType)
			{
				playerSpiritwebPowerButtons.add(new PlayerSpiritwebPowerButton(manifestation, strength));
			}
			foundPowerTypes.add(manifestation.getManifestationType());
		}

		int index = 0;
		for (ManifestationTypes foundPowerType : foundPowerTypes)
		{
			sidedMenuButtons.add(
					new SidedMenuButton(
							foundPowerType.getName(),
							foundPowerType.getID(),
							index++,
							foundPowerTypes.size(),
							width,
							Direction.UP)
			);
		}
	}

	@Override
	public void render(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTicks)
	{
		if(syncLock) return;
		PoseStack matrixStack = guiGraphics.pose();
		if (spiritweb == null || width <= 0 || height <= 0)
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

		doAction = null;

		renderSpiritwebMenuContainer(buffer, ringMenus, mouseX, mouseY, middleX, middleY);
		renderSpiritwebMenuContainer(buffer, braceletMenus, mouseX, mouseY, middleX, middleY);
		renderSpiritwebMenuContainer(buffer, necklaceMenus, mouseX, mouseY, middleX, middleY);

		renderPlayerSpiritwebButtons(buffer, mouseVecX, mouseVecY, middleX, middleY);
		renderSidedButtons(buffer, mouseX, mouseY, middleX, middleY);

		tessellator.end();

		matrixStack.pushPose();
		for (SpiritwebPowerButton spiritwebPowerButton : spiritwebPowerButtons)
		{
			spiritwebPowerButton.renderIcon(guiGraphics);
		}
		matrixStack.popPose();

		drawIcons(guiGraphics, buffer, middleX, middleY);

		renderSpiritwebButtonContainerStrings(guiGraphics, ringMenus, middleX, middleY);
		renderSpiritwebButtonContainerStrings(guiGraphics, braceletMenus, middleX, middleY);
		renderSpiritwebButtonContainerStrings(guiGraphics, necklaceMenus, middleX, middleY);

		matrixStack.popPose();
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
		renderPlayerSpiritwebButtonIcons(guiGraphics, middle_x, middle_y);
		//put the icons on the sided buttons
		renderSidedButtonIcons(guiGraphics, middle_x, middle_y);

		matrixStack.popPose();
	}

	private void renderSpiritwebButtonContainerStrings(GuiGraphics guiGraphics, List<SpiritwebButtonContainer> spiritwebButtonContainers, double middle_x, double middle_y)
	{
		if (spiritwebButtonContainers != null && !spiritwebButtonContainers.isEmpty())
		{
			SpiritwebButtonContainer spiritwebButtonContainer = spiritwebButtonContainers.get(0);
			int y_offset = spiritwebButtonContainer.getContainWidth() - 1;
			final String text = I18n.get(spiritwebButtonContainer.item.getDescriptionId());
			int xCoord = (int) (middle_x - font.width(text) * 0.5);
			int yCoord = (int) ((middle_y - font.lineHeight - spiritwebButtonContainer.getHeight() / 2) + (50 * y_offset));

			guiGraphics.drawString(font, text, xCoord, yCoord, 0xffffffff);
		}
	}

	private void renderSidedButtonIcons(GuiGraphics guiGraphics, double middleX, double middleY)
	{
		Minecraft mc = Minecraft.getInstance();
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

			ResourceLocation tex = new ResourceLocation(button.name, stringBuilder.toString());
			try {
				mc.getResourceManager().getResourceOrThrow(tex);
				guiGraphics.blit(tex, (int) (x - 8), (int) (y - 8),
						16, 16, 0, 0, 18, 18, 18, 18);
			} catch (Exception ignored) {
				// No icon? Just don't draw it.
			}

		}
	}

	private void renderPlayerSpiritwebButtonIcons(GuiGraphics guiGraphics, double middleX, double middleY)
	{
		Minecraft mc = Minecraft.getInstance();
		final StringBuilder stringBuilder = new StringBuilder();
		for (final PlayerSpiritwebPowerButton menuRegion : playerSpiritwebPowerButtons)
		{
			// Skip uninitialized entries just in case
			if (menuRegion.centerX == 0 && menuRegion.centerY == 0) {
				continue;
			}

			stringBuilder.setLength(0);
			final double x = menuRegion.centerX;
			final double y = menuRegion.centerY;

			final double scalex = 15 * 0.5;
			final double scaley = 15 * 0.5;
			final double x1 = x - scalex;
			final double y1 = y - scaley;

			Manifestation mani = menuRegion.manifestation;
			final ManifestationTypes manifestationType = mani.getManifestationType();
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
			try {
				mc.getResourceManager().getResourceOrThrow(textureLocation);
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
			} catch (Exception ignored) {
				// Missing icon – skip drawing instead of showing a white/missing-texture square
			}

		}
	}

	private void renderSidedButtons(BufferBuilder buffer, double mouseX, double mouseY, double middle_x, double middle_y)
	{
		if (sidedMenuButtons.isEmpty())
		{
			return;
		}

		final int size = sidedMenuButtons.size();
		final double spacing = 25.0;   // match PlayerSpiritwebPowerButtons
		final double half = (size - 1) / 2.0;

		// Put them at the same vertical center as the player buttons, or adjust slightly
		final double centerY = middle_y - 80; // PlayerSpiritwebPowerButtons bar center

		for (int i = 0; i < size; i++)
		{
			final SidedMenuButton button = sidedMenuButtons.get(i);

			// Center of this button
			double centerX = middle_x + spacing * (i - half);

			// Button size 18x18 → half-size 9
			double halfSize = 9.0;
			button.x1 = centerX - halfSize;
			button.x2 = centerX + halfSize;
			button.y1 = centerY - halfSize;
			button.y2 = centerY + halfSize;

			final float a = 0.5f;
			float f;

			button.highlighted =
					MathHelper.inTriangle(
							button.x1, button.y1,
							button.x2, button.y2,
							button.x1 + 18, button.y1,
							mouseX, mouseY)
							|| MathHelper.inTriangle(
							button.x1, button.y1,
							button.x2, button.y2,
							button.x1, button.y1 + 18,
							mouseX, mouseY);

			if (button.highlighted)
			{
				f = 1;
				doAction = button;
			}
			else
			{
				f = selectedPowerType.getID() == button.powerType ? 1 : 0;
			}

			buffer.vertex(button.x1, button.y1, 0).color(f, f, f, a).endVertex();
			buffer.vertex(button.x1, button.y2, 0).color(f, f, f, a).endVertex();
			buffer.vertex(button.x2, button.y2, 0).color(f, f, f, a).endVertex();
			buffer.vertex(button.x2, button.y1, 0).color(f, f, f, a).endVertex();
		}
	}



	private void renderPlayerSpiritwebButtons(BufferBuilder buffer, double mouseVecX, double mouseVecY, double middle_x, double middle_y)
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


				if (heldButton != null && heldButton.manifestation == region.manifestation)
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

	private void renderSpiritwebMenuContainer(BufferBuilder buffer, ArrayList<SpiritwebButtonContainer> spiritwebContainers, double mouseVecX, double mouseVecY, double middleX, double middleY)
	{
		for (int i = 0; i < spiritwebContainers.size(); i++)
		{
			double yOffset = spiritwebContainers.get(i).getContainWidth() - 1;
			double height = spiritwebContainers.get(i).getHeight();

			double xOffset = ((spiritwebContainers.size() * spiritwebContainers.get(i).getWidth()) + ((spiritwebContainers.size() - 1) * 20)) / spiritwebContainers.size();

			spiritwebContainers.get(i).renderContainer(buffer, mouseVecX, mouseVecY, middleX + (xOffset * (i - ((spiritwebContainers.size() - 1) / 2f))), middleY + (yOffset * (height + 20)));
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

		public final int index;
		public final int size;

		public SidedMenuButton(
				final String name,
				final int powerType,
				int index,
				final int size,
				final int width,
				final Direction textSide)
		{
			this.name = name;
			this.action = null;
			this.powerType = powerType;

			this.index = index;
			this.size = size;

			this.color = 0xffffff;
			this.textSide = textSide;
		}
	}

	static class PlayerSpiritwebPowerButton
	{
		public final Manifestation manifestation;
		public double centerX;
		public double centerY;
		public boolean highlighted;
		public double strength;

		public PlayerSpiritwebPowerButton(final Manifestation manifestation, double strength)
		{
			this.manifestation = manifestation;
			this.strength = manifestation.getStrength(instance.spiritweb, true);
			if(this.strength == 0) this.strength = strength;

		}
	}

	static class TransferredPower {
		public Manifestation manifestation;
		public double strength;

		public TransferredPower(Manifestation manifestation, double strength) {
			this.manifestation = manifestation;
			this.strength = strength;
		}
	}

	public SpiritwebCapability getSpiritweb()
	{
		return spiritweb;
	}


}