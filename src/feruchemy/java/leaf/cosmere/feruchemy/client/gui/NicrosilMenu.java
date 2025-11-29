/*
 * File updated ~ 10 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.client.gui;

import com.google.common.base.Stopwatch;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import leaf.cosmere.common.charge.IHoldsPowers;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Manifestations.ManifestationTypes;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.client.gui.ButtonAction;
import leaf.cosmere.client.gui.ISyncSpiritweb;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.packets.StoreTapPowerMessage;
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
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class NicrosilMenu extends Screen implements ISyncSpiritweb
{
	public static final NicrosilMenu instance = new NicrosilMenu();

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
		if (getMinecraft().screen == NicrosilMenu.instance)
		{
			if (this.closed)
			{
				final Window window = getMinecraft().getWindow();
				init(getMinecraft(), window.getGuiScaledWidth(), window.getGuiScaledHeight());
				setScaledResolution(window.getGuiScaledWidth(), window.getGuiScaledHeight());

				this.spiritweb = spiritweb;

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
			closeScreen();
		}
		return super.keyReleased(pKeyCode, pScanCode, pModifiers);
	}

	@Override
	public boolean mouseClicked(final double mouseX, final double mouseY, final int button)
	{
		if (heldButton != null)
		{
			for (SpiritwebPowerButton spiritwebPowerButton : spiritwebPowerButtons)
			{
				if (spiritwebPowerButton.highlight)
				{
					if (spiritwebPowerButton.getAttribute() == null || spiritwebPowerButton.getAttribute() == heldButton.attribute)
					{
						if (spiritweb.getLiving() instanceof Player player)
						{
							LazyOptional<ICuriosItemHandler> curiosItemHandler = CuriosApi.getCuriosInventory(player);
							if (curiosItemHandler.resolve().isPresent())
							{
								ICuriosItemHandler itemHandler = curiosItemHandler.resolve().get();
								ItemStack itemStack = itemHandler.getEquippedCurios().getStackInSlot(spiritwebPowerButton.getContainer().curioItemSlot);
								if (itemStack.getItem() instanceof IHoldsPowers item)
								{
									final Attribute attribute = heldButton.attribute;
									AttributeInstance attributeInstance = player.getAttribute(attribute);

									if(item.trySetAttunedPlayer(itemStack, player))
									{
										Cosmere.packetHandler().sendToServer(new StoreTapPowerMessage(
												heldButton.attribute,
												attributeInstance.getBaseValue(),
												spiritwebPowerButton.getContainer().curioItemSlot,
												true,
												spiritwebPowerButton.getSlotIndex()));

										applyLocalStore(heldButton, spiritwebPowerButton);
									}
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
				if (spiritwebPowerButton.highlight && spiritwebPowerButton.getAttribute() != null)
				{
					if (spiritweb.getLiving() instanceof Player player)
					{
						LazyOptional<ICuriosItemHandler> curiosItemHandler = CuriosApi.getCuriosInventory(player);
						if (curiosItemHandler.resolve().isPresent())
						{
							ICuriosItemHandler itemHandler = curiosItemHandler.resolve().get();
							ItemStack itemStack = itemHandler.getEquippedCurios().getStackInSlot(spiritwebPowerButton.getContainer().curioItemSlot);
							if (itemStack.getItem() instanceof IHoldsPowers item)
							{
								if(item.getPlayerIsAttuned(itemStack, player))
								{
									Cosmere.packetHandler().sendToServer(new StoreTapPowerMessage(
											spiritwebPowerButton.getAttribute(),
											spiritwebPowerButton.getStrength(),
											spiritwebPowerButton.getContainer().curioItemSlot,
											false,
											spiritwebPowerButton.getSlotIndex()));
									applyLocalTap(spiritwebPowerButton);
								}
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
					heldButton = new TransferredPower(playerSpiritwebPowerButton.attribute, playerSpiritwebPowerButton.strength);
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
							playerSpiritwebPowerButtons.clear();
							sidedMenuButtons.clear();
							setupManifestationButtons(getAvailableManifestations(), null, 0);
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
	}

	public void closeScreen()
	{
		this.closed = true;
		getMinecraft().setScreen(null);
	}

	private void applyLocalStore(TransferredPower held, SpiritwebPowerButton targetSlot)
	{
		playerSpiritwebPowerButtons.removeIf(btn -> btn.attribute == held.attribute);


		if (targetSlot.getAttribute() == null)
		{
			targetSlot.setAttribute(held.attribute);
			targetSlot.setStrength((int) held.strength);
		}
		else if (targetSlot.getAttribute() == held.attribute)
		{
			int totalStrength = (int) held.strength + targetSlot.getStrength();

			if ((held.attribute instanceof RangedAttribute rangedAttribute))
			{
				if (totalStrength < rangedAttribute.getMinValue())
				{
					totalStrength = (int) rangedAttribute.getMinValue();
				}
				else if (totalStrength > rangedAttribute.getMaxValue())
				{
					totalStrength = (int) rangedAttribute.getMaxValue();
				}
			}
			targetSlot.setStrength(totalStrength);
		}

		if (playerSpiritwebPowerButtons.isEmpty())
		{
			final List<Manifestation> availableManifestations = getAvailableManifestations();
			if (held.manifestation.getManifestationType() == ManifestationTypes.SANDMASTERY)
			{
				availableManifestations.removeIf(manifestation -> manifestation.getManifestationType() == ManifestationTypes.SANDMASTERY);
			}
			else
			{
				availableManifestations.remove(held.manifestation);
			}
			playerSpiritwebPowerButtons.clear();
			sidedMenuButtons.clear();
			if (!availableManifestations.isEmpty())
			{
				selectedPowerType = availableManifestations.get(0).getManifestationType();
			}
			availableManifestations.sort(Comparator.comparingInt(Manifestation::getPowerID));
			setupManifestationButtons(availableManifestations, held.manifestation, held.strength);
		}
	}

	private void applyLocalTap(SpiritwebPowerButton fromSlot)
	{
		Manifestation mani = fromSlot.getManifestation();
		double strength = fromSlot.getStrength();
		final List<Manifestation> availableManifestations = getAvailableManifestations();

		double totalStrength = strength;
		if (availableManifestations.contains(mani))
		{
			totalStrength += mani.getStrength(spiritweb, true);
		}

		if ((mani.getAttribute() instanceof RangedAttribute attribute))
		{
			if (totalStrength < attribute.getMinValue())
			{
				totalStrength = (int) attribute.getMinValue();
			}
			else if (totalStrength > attribute.getMaxValue())
			{
				totalStrength = (int) attribute.getMaxValue();
			}
		}

		fromSlot.setManifestation(null);
		fromSlot.setStrength(0);

		playerSpiritwebPowerButtons.clear();
		sidedMenuButtons.clear();
		availableManifestations.removeIf(manifestation -> manifestation == mani);
		availableManifestations.add(mani);
		selectedPowerType = mani.getManifestationType();
		availableManifestations.sort(Comparator.comparingInt(Manifestation::getPowerID));
		setupManifestationButtons(availableManifestations, mani, totalStrength);
	}

	protected void setupButtons()
	{
		spiritwebPowerButtons.clear();
		necklaceMenus.clear();
		braceletMenus.clear();
		ringMenus.clear();
		playerSpiritwebPowerButtons.clear();
		sidedMenuButtons.clear();

		final List<Manifestation> availableManifestations = getAvailableManifestations();
		setupManifestationButtons(availableManifestations, null, 0);

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
					ItemStack itemStack = itemHandler.getEquippedCurios().getStackInSlot(i);
					if (itemStack.getItem() instanceof IHasManifestations item)
					{
						final double middleX = width / 2f;
						final double middleY = height / 2f;
						SpiritwebButtonContainer spiritwebContainer = new SpiritwebButtonContainer(middleX, middleY, 1, 1, i, (Item) item);
						Manifestation[] manifestations = item.getManifestations(itemHandler.getEquippedCurios().getStackInSlot(i));
						Integer[] manifestationStrengths = item.getManifestationStrengths(itemHandler.getEquippedCurios().getStackInSlot(i));
						for (int j = 0; j < item.getMaxCapacity(); j++)
						{
							SpiritwebPowerButton spiritwebPowerButton = new SpiritwebPowerButton(middleX, middleY, spiritweb, spiritwebContainer, (byte) j, item.getPlayerIsAttuned(itemStack, player));
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

	private void setupManifestationButtons(List<Manifestation> manifestations, Manifestation mani, double strength)
	{
		Set<ManifestationTypes> foundPowerTypes = EnumSet.noneOf(ManifestationTypes.class);

		boolean addedSandmastery = false;

		for (Manifestation manifestation : manifestations)
		{
			if (manifestation.getStrength(spiritweb, true) == 0 && strength == 0)
			{
				continue;
			}
			ManifestationTypes type = manifestation.getManifestationType();
			foundPowerTypes.add(type);

			if (type != selectedPowerType)
			{
				continue;
			}

			if (type == ManifestationTypes.SANDMASTERY)
			{
				if (addedSandmastery)
				{
					continue;
				}
				addedSandmastery = true;
			}

			if (mani != null && manifestation == mani)
			{
				playerSpiritwebPowerButtons.add(new PlayerSpiritwebPowerButton(manifestation, strength));
			}
			else
			{
				playerSpiritwebPowerButtons.add(new PlayerSpiritwebPowerButton(manifestation, manifestation.getStrength(spiritweb, true)));
			}
		}

		int index = 0;
		int totalTypes = foundPowerTypes.size();
		for (ManifestationTypes type : foundPowerTypes)
		{
			sidedMenuButtons.add(
					new SidedMenuButton(
							type.getName(),
							type.getID(),
							index++,
							totalTypes,
							width,
							Direction.UP)
			);
		}
	}

	@Override
	public void render(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTicks)
	{
		PoseStack matrixStack = guiGraphics.pose();
		if (spiritweb == null || width <= 0 || height <= 0)
		{
			return;
		}

		matrixStack.pushPose();

		final int start = (int) (visibility * 98) << 24;
		final int end = (int) (visibility * 128) << 24;

		guiGraphics.fillGradient(0, 0, width, height, start, end);

		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		final Tesselator tessellator = Tesselator.getInstance();
		final BufferBuilder buffer = tessellator.getBuilder();

		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		final double mouseVecX = mouseX - width / 2f;
		final double mouseVecY = (mouseY - height / 2f);

		final double middleX = width / 2f;
		final double middleY = height / 2f;

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

		renderSpiritwebPowerButtonStrings(guiGraphics);
		renderPlayerSpiritwebButtonStrings(guiGraphics, middleX, middleY);
		renderSidedButtonStrings(guiGraphics);

		matrixStack.popPose();
	}

	private void drawIcons(@NotNull GuiGraphics guiGraphics, BufferBuilder buffer, double middle_x, double middle_y)
	{
		PoseStack matrixStack = guiGraphics.pose();
		matrixStack.pushPose();
		RenderSystem.enableBlend();

		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);

		renderPlayerSpiritwebButtonIcons(guiGraphics, middle_x, middle_y);
		renderSidedButtonIcons(guiGraphics, middle_x, middle_y);

		matrixStack.popPose();
	}

	private void renderSpiritwebPowerButtonStrings(GuiGraphics guiGraphics)
	{
		for (final SpiritwebPowerButton button : spiritwebPowerButtons)
		{
			if (!button.highlight || button.getManifestation() == null)
			{
				continue;
			}

			final Manifestation mani = button.getManifestation();
			String text = "+" + button.getStrength() + " ";
			if (mani.getManifestationType() == ManifestationTypes.SANDMASTERY)
			{
				text += I18n.get("manifestation.sandmastery.ribbons");
			}
			else
			{
				text += I18n.get(mani.getTranslationKey());
			}

			int textCenterX = (int) button.posX;
			int textY = (int) (button.posY + 20); // 20px above the button

			int textX = textCenterX - font.width(text) / 2;

			int bgLeft = textX - 2;
			int bgTop = textY - 2;
			int bgRight = textX + font.width(text) + 2;
			int bgBottom = textY + font.lineHeight + 2;

			guiGraphics.fill(bgLeft, bgTop, bgRight, bgBottom, 0x80000000);
			guiGraphics.drawString(font, text, textX, textY, 0xFFFFFFFF);

			break;
		}
	}

	private void renderPlayerSpiritwebButtonStrings(GuiGraphics guiGraphics, double middleX, double middleY)
	{
		if (playerSpiritwebPowerButtons.isEmpty())
		{
			return;
		}

		for (final PlayerSpiritwebPowerButton btn : playerSpiritwebPowerButtons)
		{
			if (!btn.highlighted || btn.manifestation == null)
			{
				continue;
			}

			String text = "+" + (int) btn.strength + " ";
			if (btn.manifestation.getManifestationType() == ManifestationTypes.SANDMASTERY)
			{
				text += I18n.get("manifestation.sandmastery.ribbons");
			}
			else
			{
				text += I18n.get(btn.manifestation.getTranslationKey());
			}

			int textCenterX = (int) (middleX + btn.centerX);
			int textY = (int) (middleY + btn.centerY - 24); // a bit above the button

			int textX = textCenterX - font.width(text) / 2;

			int bgLeft = textX - 2;
			int bgTop = textY - 2;
			int bgRight = textX + font.width(text) + 2;
			int bgBottom = textY + font.lineHeight + 2;

			guiGraphics.fill(bgLeft, bgTop, bgRight, bgBottom, 0x80000000);
			guiGraphics.drawString(font, text, textX, textY, 0xFFFFFFFF);

			break;
		}
	}

	private void renderSidedButtonStrings(GuiGraphics guiGraphics)
	{
		if (sidedMenuButtons.isEmpty())
		{
			return;
		}

		for (SidedMenuButton button : sidedMenuButtons)
		{
			if (!button.highlighted)
			{
				continue;
			}

			final String text = I18n.get(ManifestationTypes.valueOf(button.powerType).get().getName());

			double centerX = (button.x1 + button.x2) / 2.0;
			double topY = button.y1;

			int textY = (int) (topY - font.lineHeight - 4);
			int textX = (int) (centerX - font.width(text) / 2);

			int bgLeft = textX - 2;
			int bgTop = textY - 2;
			int bgRight = textX + font.width(text) + 2;
			int bgBottom = textY + font.lineHeight + 2;

			guiGraphics.fill(bgLeft, bgTop, bgRight, bgBottom, 0x80000000);
			guiGraphics.drawString(font, text, textX, textY, 0xFFFFFFFF);

			break;
		}
	}

	private void renderSpiritwebButtonContainerStrings(GuiGraphics guiGraphics, List<SpiritwebButtonContainer> spiritwebButtonContainers, double middle_x, double middle_y)
	{
		if (spiritwebButtonContainers != null && !spiritwebButtonContainers.isEmpty())
		{
			SpiritwebButtonContainer spiritwebButtonContainer = spiritwebButtonContainers.get(0);
			int y_offset = spiritwebButtonContainer.getContainWidth() - 1;
			final String text = I18n.get(spiritwebButtonContainer.item.getDescriptionId());
			int xCoord = (int) (middle_x - font.width(text) * 0.5);
			int yCoord = (int) ((middle_y - font.lineHeight - spiritwebButtonContainer.getHeight() / 2) + (60 * y_offset));

			guiGraphics.drawString(font, text, xCoord, yCoord, 0xffffffff);
		}
	}

	private void renderSidedButtonIcons(GuiGraphics guiGraphics, double middleX, double middleY)
	{
		Minecraft mc = getMinecraft();
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
			try
			{
				mc.getResourceManager().getResourceOrThrow(tex);
				guiGraphics.blit(tex, (int) (x - 8), (int) (y - 8),
						16, 16, 0, 0, 18, 18, 18, 18);
			}
			catch (Exception ignored)
			{
				// No icon? Just don't draw it.
			}

		}
	}

	private void renderPlayerSpiritwebButtonIcons(GuiGraphics guiGraphics, double middleX, double middleY)
	{
		Minecraft mc = getMinecraft();
		final StringBuilder stringBuilder = new StringBuilder();
		for (final PlayerSpiritwebPowerButton menuRegion : playerSpiritwebPowerButtons)
		{
			// Skip uninitialized entries just in case
			if (menuRegion.centerX == 0 && menuRegion.centerY == 0)
			{
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
			try
			{
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
			}
			catch (Exception ignored)
			{
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
		final double spacing = 25.0;
		final double half = (size - 1) / 2.0;

		final double centerY = middle_y - 90;

		for (int i = 0; i < size; i++)
		{
			final SidedMenuButton button = sidedMenuButtons.get(i);

			double centerX = middle_x + spacing * (i - half);

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

			spiritwebContainers.get(i).renderContainer(buffer, mouseVecX, mouseVecY, middleX + (xOffset * (i - ((spiritwebContainers.size() - 1) / 2f))), middleY + (yOffset * (height + 30)));
		}
	}

	private List<Manifestation> getAvailableManifestations()
	{
		final List<Manifestation> availableManifestations = spiritweb.getAvailableManifestations();
		availableManifestations.removeIf((manifestation ->
				manifestation.getManifestationType() == ManifestationTypes.ALLOMANCY &&
						manifestation.getPowerID() == Metals.MetalType.ATIUM.getID())
		);

		availableManifestations.removeIf((manifestation ->
				manifestation.getManifestationType() == ManifestationTypes.FERUCHEMY &&
						(manifestation.getPowerID() == Metals.MetalType.NICROSIL.getID() ||
								manifestation.getPowerID() == Metals.MetalType.ATIUM.getID()))
		);

		return availableManifestations;
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
		public final Attribute attribute;
		public double centerX;
		public double centerY;
		public boolean highlighted;
		public double strength;

		public PlayerSpiritwebPowerButton(final Attribute attribute, double strength)
		{
			this.attribute = attribute;
			this.strength = strength;
		}
	}

	static class TransferredPower
	{
		public Attribute attribute;
		public double strength;

		public TransferredPower(Attribute attribute, double strength)
		{
			this.attribute = attribute;
			this.strength = strength;
		}
	}

	public SpiritwebCapability getSpiritweb()
	{
		return spiritweb;
	}


}