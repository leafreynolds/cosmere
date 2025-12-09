// java
/*
 * File updated ~ 10 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.client.gui;

import com.google.common.base.Stopwatch;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import leaf.cosmere.api.Manifestations.ManifestationTypes;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.client.gui.ISyncSpiritweb;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.charge.IHoldsPowers;
import leaf.cosmere.common.network.packets.StoreTapPowerMessage;
import leaf.cosmere.common.util.CosmereAttributeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class NicrosilMenu extends Screen implements ISyncSpiritweb {
    public static final NicrosilMenu instance = new NicrosilMenu();

    protected ArrayList<PowerButtonContainer> ringMenus = new ArrayList<>();
    protected ArrayList<PowerButtonContainer> braceletMenus = new ArrayList<>();
    protected ArrayList<PowerButtonContainer> necklaceMenus = new ArrayList<>();

    protected ArrayList<PowerButton> spiritwebPowerButtons = new ArrayList<>();
    protected ArrayList<PowerButton> playerSpiritwebPowerButtons = new ArrayList<>();
    protected ArrayList<PowerButton> sidedMenuButtons = new ArrayList<>();

    private SpiritwebCapability spiritweb = null;
    private boolean closed = true;
    private float visibility = 0.0f;
    private Stopwatch lastChange = Stopwatch.createStarted();
    private ManifestationTypes selectedPowerType = ManifestationTypes.ALLOMANCY;
    private PowerButton heldButton = null;

    protected NicrosilMenu() {
        super(Component.literal("Menu"));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void raiseVisibility() {
        final float TIME_SCALE = 0.01f;
        visibility = MathHelper.clamp01(visibility + lastChange.elapsed(TimeUnit.MILLISECONDS) * TIME_SCALE);
        lastChange = Stopwatch.createStarted();
    }

    public void setScaledResolution(final int scaledWidth, final int scaledHeight) {
        width = scaledWidth;
        height = scaledHeight;
    }

    @Override
    public @NotNull Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    public void postRender(SpiritwebCapability spiritweb) {
        if (getMinecraft().screen == NicrosilMenu.instance) {
            if (this.closed) {
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
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        if (Keybindings.MANIFESTATION_MENU.matches(pKeyCode, pScanCode)) {
            closeScreen();
        }
        return super.keyReleased(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (heldButton != null) {
            for (PowerButton spiritwebPowerButton : spiritwebPowerButtons) {
                if (spiritwebPowerButton.highlighted) {
                    if (spiritwebPowerButton.getAttribute() == null ||
                            spiritwebPowerButton.getAttribute() == heldButton.attribute) {
                        if (spiritweb.getLiving() instanceof Player player) {
                            LazyOptional<ICuriosItemHandler> curiosItemHandler = CuriosApi.getCuriosInventory(player);
                            if (curiosItemHandler.resolve().isPresent()) {
                                ICuriosItemHandler itemHandler = curiosItemHandler.resolve().get();
                                ItemStack itemStack = itemHandler.getEquippedCurios()
                                        .getStackInSlot(spiritwebPowerButton.getContainer().curioItemSlot);
                                if (itemStack.getItem() instanceof IHoldsPowers item) {
                                    final Attribute attribute = heldButton.attribute;
                                    AttributeInstance attributeInstance = player.getAttribute(attribute);

                                    if (item.trySetAttunedPlayer(itemStack, player)) {
                                        Cosmere.packetHandler().sendToServer(new StoreTapPowerMessage(
                                                attribute,
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
        } else {
            for (PowerButton spiritwebPowerButton : spiritwebPowerButtons) {
                if (spiritwebPowerButton.highlighted && spiritwebPowerButton.getAttribute() != null) {
                    if (spiritweb.getLiving() instanceof Player player) {
                        LazyOptional<ICuriosItemHandler> curiosItemHandler = CuriosApi.getCuriosInventory(player);
                        if (curiosItemHandler.resolve().isPresent()) {
                            ICuriosItemHandler itemHandler = curiosItemHandler.resolve().get();
                            ItemStack itemStack = itemHandler.getEquippedCurios()
                                    .getStackInSlot(spiritwebPowerButton.getContainer().curioItemSlot);
                            if (itemStack.getItem() instanceof IHoldsPowers item) {
                                if (item.getPlayerIsAttuned(itemStack, player)) {
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

            for (PowerButton playerSpiritwebPowerButton : playerSpiritwebPowerButtons) {
                if (playerSpiritwebPowerButton.highlighted) {
                    heldButton = playerSpiritwebPowerButton;
                }
            }

            for (PowerButton sidedMenuButton : sidedMenuButtons) {
                if (sidedMenuButton.highlighted) {
                    if (heldButton == null) {
                        if (sidedMenuButton.manifestationType != ManifestationTypes.NONE) {
                            selectedPowerType = sidedMenuButton.manifestationType;
                            playerSpiritwebPowerButtons.clear();
                            sidedMenuButtons.clear();
                            setupAttributeButtons(getAvailableAttributes(), null, 0);
                            break;
                        }
                    }
                }
            }

        }
        return true;
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        return true;
    }

    public void onSpiritwebUpdated(SpiritwebCapability cap) {
        this.spiritweb = cap;
    }

    public void closeScreen() {
        this.closed = true;
        this.heldButton = null;
        getMinecraft().setScreen(null);
    }

    private void applyLocalStore(PowerButton held, PowerButton targetSlot) {
        playerSpiritwebPowerButtons.removeIf(btn -> btn.getAttribute() == held.attribute);


        if (targetSlot.getAttribute() == null) {
            targetSlot.setAttribute(held.attribute);
            targetSlot.setStrength(held.strength);
        } else if (targetSlot.getAttribute() == held.attribute) {
            int totalStrength = (int) held.strength + (int) targetSlot.getStrength();

            if ((held.attribute instanceof RangedAttribute rangedAttribute)) {
                if (totalStrength < rangedAttribute.getMinValue()) {
                    totalStrength = (int) rangedAttribute.getMinValue();
                } else if (totalStrength > rangedAttribute.getMaxValue()) {
                    totalStrength = (int) rangedAttribute.getMaxValue();
                }
            }
            targetSlot.setStrength(totalStrength);
        }

        if (playerSpiritwebPowerButtons.isEmpty()) {
            final List<AttributeInstance> availableAttributes = getAvailableAttributes();
            availableAttributes.removeIf(attributeInstance -> attributeInstance.getAttribute() == held.attribute);
            playerSpiritwebPowerButtons.clear();
            sidedMenuButtons.clear();
            availableAttributes.sort(Comparator.comparingInt(
                    (availableAttribute -> CosmereAttributeUtils.getManifestationType(availableAttribute.getAttribute())
                            .getID())));
            if (!availableAttributes.isEmpty()) {
                selectedPowerType =
                        CosmereAttributeUtils.getManifestationType(availableAttributes.get(0).getAttribute());
            }
            setupAttributeButtons(availableAttributes, held.attribute, held.strength);
        }
    }

    private void applyLocalTap(PowerButton fromSlot) {
        Attribute attribute = fromSlot.getAttribute();
        Integer strength = fromSlot.getStrength();
        final List<AttributeInstance> availableAttributes = getAvailableAttributes();

        Integer totalStrength = strength;
        if (availableAttributes.stream().anyMatch(attributeInstance -> attributeInstance.getAttribute() == attribute)) {
            totalStrength += (int) spiritweb.getLiving().getAttribute(attribute).getBaseValue();
        }

        if ((attribute instanceof RangedAttribute rangedAttribute)) {
            if (totalStrength < rangedAttribute.getMinValue()) {
                totalStrength = (int) rangedAttribute.getMinValue();
            } else if (totalStrength > rangedAttribute.getMaxValue()) {
                totalStrength = (int) rangedAttribute.getMaxValue();
            }
        }

        fromSlot.setAttribute(null);
        fromSlot.setStrength(0);

        playerSpiritwebPowerButtons.clear();
        sidedMenuButtons.clear();
        availableAttributes.removeIf(att -> att.getAttribute() == attribute);
        availableAttributes.add(spiritweb.getLiving().getAttribute(attribute));
        spiritweb.getLiving().getAttribute(attribute).setBaseValue(strength);
        selectedPowerType = CosmereAttributeUtils.getManifestationType(attribute);
        availableAttributes.sort(Comparator.comparingInt(
                (availableAttribute -> CosmereAttributeUtils.getAttributePowerId(availableAttribute.getAttribute()))));
        setupAttributeButtons(availableAttributes, attribute, totalStrength);
    }

    protected void setupButtons() {
        spiritwebPowerButtons.clear();
        necklaceMenus.clear();
        braceletMenus.clear();
        ringMenus.clear();
        playerSpiritwebPowerButtons.clear();
        sidedMenuButtons.clear();

        final List<AttributeInstance> availableAttributes = getAvailableAttributes();
        setupAttributeButtons(availableAttributes, null, 0);

        setupSpiritwebButtons();
    }

    private void setupSpiritwebButtons() {
        if (spiritweb.getLiving() instanceof Player player) {
            LazyOptional<ICuriosItemHandler> curiosItemHandler = CuriosApi.getCuriosInventory(player);
            if (curiosItemHandler.resolve().isPresent()) {
                ICuriosItemHandler itemHandler = curiosItemHandler.resolve().get();

                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    ItemStack itemStack = itemHandler.getEquippedCurios().getStackInSlot(i);
                    if (itemStack.getItem() instanceof IHoldsPowers item) {
                        final double middleX = width / 2f;
                        final double middleY = height / 2f;
                        PowerButtonContainer spiritwebContainer =
                                new PowerButtonContainer(middleX, middleY, item.getMaxCapacity(), 1, i, (Item) item);
                        Attribute[] attributes = item.getAttributes(itemHandler.getEquippedCurios().getStackInSlot(i));
                        Integer[] manifestationStrengths =
                                item.getAttributeStrengths(itemHandler.getEquippedCurios().getStackInSlot(i));
                        for (int j = 0; j < item.getMaxCapacity(); j++) {
                            PowerButton spiritwebPowerButton = new PowerButton(middleX, middleY,
                                    CosmereAttributeUtils.getManifestationType(attributes[j]), spiritwebContainer,
                                    (byte) j, item.getPlayerIsAttuned(itemStack, player));
                            spiritwebPowerButton.setAttribute(attributes[j]);
                            spiritwebPowerButton.setStrength(manifestationStrengths[j]);
                            spiritwebPowerButtons.add(spiritwebPowerButton);
                            spiritwebContainer.addButton(spiritwebPowerButton);
                        }

                        switch (item.getMaxCapacity()) {
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

    private void setupAttributeButtons(List<AttributeInstance> spiritwebPowers, Attribute newPower, Integer strength) {
        Set<ManifestationTypes> foundPowerTypes = EnumSet.noneOf(ManifestationTypes.class);


        for (AttributeInstance spiritwebPower : spiritwebPowers) {
            if (spiritwebPower.getBaseValue() == 0 && strength == 0) {
                continue;
            }

            ManifestationTypes powerType = CosmereAttributeUtils.getManifestationType(spiritwebPower.getAttribute());
            foundPowerTypes.add(powerType);

            if (powerType != selectedPowerType) {
                continue;
            }

            if (spiritwebPower.getAttribute() == newPower) {
                PowerButton p = PowerButton.createPlayerButton(newPower, strength);
                playerSpiritwebPowerButtons.add(p);
            } else {
                PowerButton p = PowerButton.createPlayerButton(spiritwebPower.getAttribute(),
                        (int) spiritwebPower.getBaseValue());
                playerSpiritwebPowerButtons.add(p);
            }
        }

        for (ManifestationTypes type : foundPowerTypes) {
            PowerButton btn = PowerButton.createSideMenuButton(type);
            sidedMenuButtons.add(btn);
        }
    }

    @Override
    public void render(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTicks) {
        PoseStack matrixStack = guiGraphics.pose();
        if (spiritweb == null || width <= 0 || height <= 0) {
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

        final double middleX = width / 2f;
        final double middleY = height / 2f;

        renderSpiritwebMenuContainer(buffer, ringMenus, mouseX, mouseY, middleX, middleY);
        renderSpiritwebMenuContainer(buffer, braceletMenus, mouseX, mouseY, middleX, middleY);
        renderSpiritwebMenuContainer(buffer, necklaceMenus, mouseX, mouseY, middleX, middleY);

        renderPlayerSpiritwebButtons(buffer, mouseX, mouseY, middleX, middleY);
        renderSidedButtons(buffer, mouseX, mouseY, middleX, middleY);
        renderHeldButton(buffer, mouseX, mouseY);

        tessellator.end();

        matrixStack.pushPose();
        drawIcons(guiGraphics, buffer, middleX, middleY);

        renderSpiritwebButtonContainerStrings(guiGraphics, ringMenus, middleX, middleY);
        renderSpiritwebButtonContainerStrings(guiGraphics, braceletMenus, middleX, middleY);
        renderSpiritwebButtonContainerStrings(guiGraphics, necklaceMenus, middleX, middleY);

        renderSpiritwebPowerButtonStrings(guiGraphics);
        renderPlayerSpiritwebButtonStrings(guiGraphics);
        renderSidedButtonStrings(guiGraphics);

        matrixStack.popPose();
    }

    private void drawIcons(@NotNull GuiGraphics guiGraphics, BufferBuilder buffer, double middle_x, double middle_y) {
        PoseStack matrixStack = guiGraphics.pose();
        matrixStack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        renderSpiritwebPowerButtonIcons(guiGraphics);
        renderPlayerSpiritwebButtonIcons(guiGraphics);
        renderSidedButtonIcons(guiGraphics, middle_x, middle_y);

        matrixStack.popPose();
    }

    private void renderSpiritwebPowerButtonStrings(GuiGraphics guiGraphics) {
        for (final PowerButton button : spiritwebPowerButtons) {
            if (!button.highlighted || button.getAttribute() == null) {
                continue;
            }

            final Attribute attr = button.getAttribute();
            String text = "+" + button.getStrength() + " ";
            text += I18n.get(attr.getDescriptionId());

            int textCenterX = (int) button.posX;
            int textY = (int) (button.posY + 20);

            int textX = textCenterX - font.width(text) / 2;

            int bgLeft = textX - 2;
            int bgTop = textY - 2;
            int bgRight = textX + font.width(text) + 2;
            int bgBottom = textY + font.lineHeight + 1;

            guiGraphics.fill(bgLeft, bgTop, bgRight, bgBottom, 0x80000000);
            guiGraphics.drawString(font, text, textX, textY, 0xFFFFFFFF);

            break;
        }
    }

    private void renderPlayerSpiritwebButtonStrings(GuiGraphics guiGraphics) {
        if (playerSpiritwebPowerButtons.isEmpty()) {
            return;
        }

        for (PowerButton btn : playerSpiritwebPowerButtons) {
            if (!btn.highlighted || btn.getAttribute() == null) {
                continue;
            }

            String text = "+" + btn.getStrength() + " " + I18n.get(btn.getAttribute().getDescriptionId());

            int textCenterX = (int) btn.centerX;
            int textY = (int) (btn.centerY - PLAYER_BTN_HALF_SIZE - PLAYER_BTN_LABEL_GAP - font.lineHeight);

            int textX = textCenterX - font.width(text) / 2;

            int bgLeft = textX - 3;
            int bgTop = textY - 3;
            int bgRight = textX + font.width(text) + 3;
            int bgBottom = textY + font.lineHeight + 3;

            guiGraphics.fill(bgLeft, bgTop, bgRight, bgBottom, 0x80000000);
            guiGraphics.drawString(font, text, textX, textY, 0xFFFFFFFF);

            break;
        }
    }

    private void renderSidedButtonStrings(GuiGraphics guiGraphics) {
        if (sidedMenuButtons.isEmpty()) {
            return;
        }

        for (PowerButton button : sidedMenuButtons) {
            if (!button.highlighted) {
                continue;
            }

            final String text = I18n.get(button.manifestationType.getName());

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

    private void renderSpiritwebButtonContainerStrings(GuiGraphics guiGraphics,
                                                       List<PowerButtonContainer> spiritwebButtonContainers,
                                                       double middle_x, double middle_y) {
        if (spiritwebButtonContainers != null && !spiritwebButtonContainers.isEmpty()) {
            PowerButtonContainer spiritwebButtonContainer = spiritwebButtonContainers.get(0);
            int y_offset = spiritwebButtonContainer.getContainWidth() - 1;
            final String text = I18n.get(spiritwebButtonContainer.item.getDescriptionId());
            int xCoord = (int) (middle_x - font.width(text) * 0.5);
            int yCoord =
                    (int) ((middle_y - font.lineHeight - spiritwebButtonContainer.getHeight() / 2) + (60 * y_offset) -
                            1);

            guiGraphics.drawString(font, text, xCoord, yCoord, 0xffffffff);
        }
    }

    private void renderSidedButtonIcons(GuiGraphics guiGraphics, double middleX, double middleY) {
        Minecraft mc = getMinecraft();
        final StringBuilder stringBuilder = new StringBuilder();
        for (final PowerButton button : sidedMenuButtons) {
            stringBuilder.setLength(0);
            final double x = (button.x1 + button.x2) / 2 + 0.01;
            final double y = (button.y1 + button.y2) / 2 + 0.01;

            stringBuilder
                    .append("textures/icon/")
                    .append(button.manifestationType.getName())
                    .append(".png");

            ResourceLocation tex = new ResourceLocation(button.manifestationType.getName(), stringBuilder.toString());
            try {
                mc.getResourceManager().getResourceOrThrow(tex);
                guiGraphics.blit(tex, (int) (x - 8), (int) (y - 8),
                        16, 16, 0, 0, 18, 18, 18, 18);
            } catch (Exception ignored) {
                // No icon? Just don't draw it.
            }

        }
    }

    private void renderSpiritwebPowerButtonIcons(GuiGraphics guiGraphics) {
        if (spiritwebPowerButtons.isEmpty()) {
            return;
        }

        Minecraft mc = getMinecraft();

        for (final PowerButton btn : spiritwebPowerButtons) {
            if (btn.attribute == null) {
                continue;
            }

            ResourceLocation tex = getAttributeIconLocation(btn.attribute);
            if (tex == null) {
                continue;
            }

            // Centered around the button's posX / posY
            final double x = btn.posX;
            final double y = btn.posY;

            final double half = 15 * 0.5;
            final int drawX = (int) (x - half);
            final int drawY = (int) (y - half);

            try {
                mc.getResourceManager().getResourceOrThrow(tex);

                RenderSystem.setShaderTexture(0, tex);
                guiGraphics.blit(
                        tex,
                        drawX,
                        drawY,
                        16, 16,
                        0, 0,
                        18, 18,
                        18, 18
                );
            } catch (Exception ignored) {
                // Missing texture: silently skip
            }
        }
    }

    private void renderPlayerSpiritwebButtonIcons(GuiGraphics guiGraphics) {
        if (playerSpiritwebPowerButtons.isEmpty()) {
            return;
        }

        Minecraft mc = getMinecraft();

        for (final PowerButton btn : playerSpiritwebPowerButtons) {
            if (btn.attribute == null) {
                continue;
            }

            // Skip buttons that haven't been laid out yet
            if (btn.centerX == 0 && btn.centerY == 0) {
                continue;
            }

            ResourceLocation tex = getAttributeIconLocation(btn.attribute);
            if (tex == null) {
                continue;
            }

            final double x = btn.centerX;
            final double y = btn.centerY;

            final double half = 15 * 0.5;
            final int drawX = (int) (x - half);
            final int drawY = (int) (y - half);

            try {
                mc.getResourceManager().getResourceOrThrow(tex);
                RenderSystem.setShaderTexture(0, tex);
                guiGraphics.blit(
                        tex,
                        drawX,
                        drawY,
                        16, 16,
                        0, 0,
                        18, 18,
                        18, 18
                );
            } catch (Exception ignored) {
                // Missing texture: skip
            }
        }
    }

    private void renderSidedButtons(BufferBuilder buffer, double mouseX, double mouseY, double middle_x,
                                    double middle_y) {
        if (sidedMenuButtons.isEmpty()) {
            return;
        }

        final int size = sidedMenuButtons.size();
        final double spacing = 25.0;
        final double half = (size - 1) / 2.0;

        final double centerY = middle_y - 90;

        for (int i = 0; i < size; i++) {
            final PowerButton button = sidedMenuButtons.get(i);

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

            if (button.highlighted) {
                f = 1;
            } else {
                f = selectedPowerType.getID() == button.manifestationType.getID() ? 1 : 0;
            }

            buffer.vertex(button.x1, button.y1, 0).color(f, f, f, a).endVertex();
            buffer.vertex(button.x1, button.y2, 0).color(f, f, f, a).endVertex();
            buffer.vertex(button.x2, button.y2, 0).color(f, f, f, a).endVertex();
            buffer.vertex(button.x2, button.y1, 0).color(f, f, f, a).endVertex();
        }
    }

    private void renderSpiritwebMenuContainer(BufferBuilder buffer, ArrayList<PowerButtonContainer> spiritwebContainers,
                                              double mouseVecX, double mouseVecY, double middleX, double middleY) {
        for (int i = 0; i < spiritwebContainers.size(); i++) {
            double yOffset = spiritwebContainers.get(i).getContainWidth() - 1;
            double height = spiritwebContainers.get(i).getHeight();

            double xOffset = ((spiritwebContainers.size() * spiritwebContainers.get(i).getWidth()) +
                    ((spiritwebContainers.size() - 1) * 20)) / spiritwebContainers.size();

            spiritwebContainers.get(i).renderContainer(buffer, mouseVecX, mouseVecY,
                    middleX + (xOffset * (i - ((spiritwebContainers.size() - 1) / 2f))),
                    middleY + (yOffset * (height + 30)));
        }
    }

    private List<AttributeInstance> getAvailableAttributes() {
        final List<AttributeInstance> spiritwebPowers = new ArrayList<>();
        spiritweb.getSubmodules().forEach((manifestationType, submodule) ->
        {
            List<AttributeInstance> powers = submodule.getEntityPowers(spiritweb.getLiving());
            powers.removeIf(attributeInstance -> attributeInstance.getAttribute() ==
                    CosmereAttributeUtils.getAttribute(ManifestationTypes.FERUCHEMY,
                            Metals.MetalType.NICROSIL.getID()));
            spiritwebPowers.addAll(powers);
        });

        return spiritwebPowers;
    }

    static class PowerButton {
        public Attribute attribute;
        public Integer strength;
        public boolean highlighted;
        public boolean isPlayer;

        public double x1 = 0, y1 = 0;
        public double x2 = 0, y2 = 0;
        public double x3 = 0, y3 = 0;
        public double x4 = 0, y4 = 0;
        public double posX = 0, posY = 0;
        public boolean matchesIdentity;
        public int red = 205, green = 205, blue = 205, opacity = 100;
        double width = 20, height = 20;

        ManifestationTypes manifestationType;
        byte slotIndex;
        PowerButtonContainer container;

        public double centerX;
        public double centerY;

        private PowerButton() {
        }

        public PowerButton(double posX, double posY, ManifestationTypes manifestationType,
                           PowerButtonContainer container, byte slotIndex, boolean matchesIdentity) {
            this.posX = posX;
            this.posY = posY;
            this.manifestationType = manifestationType;
            this.container = container;
            this.slotIndex = slotIndex;
            this.matchesIdentity = matchesIdentity;
            this.isPlayer = false;

            recomputeCorners();
            this.red = 205;
            this.blue = 205;
            this.green = 205;
            this.opacity = 100;
        }

        public static PowerButton createSideMenuButton(ManifestationTypes type) {
            PowerButton p = new PowerButton();
            p.isPlayer = false;
            p.manifestationType = type;
            p.width = 18;
            p.height = 18;
            return p;
        }

        public static PowerButton createPlayerButton(Attribute attribute, Integer strength) {
            PowerButton p = new PowerButton();
            p.isPlayer = true;
            p.attribute = attribute;
            p.strength = strength;
            p.highlighted = false;
            return p;
        }

        public void setPosition(double sPosX, double sPosY) {
            this.posX = sPosX;
            this.posY = sPosY;
            recomputeCorners();
        }

        private void recomputeCorners() {
            double halfW = width / 2.0;
            double halfH = height / 2.0;
            // Left-top
            x1 = posX - halfW;
            y1 = posY - halfH;
            // Left-bottom
            x2 = posX - halfW;
            y2 = posY + halfH;
            // Right-bottom
            x3 = posX + halfW;
            y3 = posY + halfH;
            // Right-top
            x4 = posX + halfW;
            y4 = posY - halfH;
        }

        public void highlightAction(double mouseX, double mouseY, double middle_x, double middle_y) {
            if (isPlayer) {
                return;
            }
            highlighted = (MathHelper.inTriangle(x1, y1, x2, y2, x3, y3, mouseX, mouseY)
                    || MathHelper.inTriangle(x1, y1, x4, y4, x3, y3, mouseX, mouseY));
        }

        public void renderButton(BufferBuilder buffer, double mouseX, double mouseY) {
            if (isPlayer) {
                return;
            }
            int r = red;
            int g = green;
            int b = blue;

            if (highlighted) {
                r += 30;
                g += 30;
                b += 30;
            }
            if (!matchesIdentity) {
                g -= 100;
                b -= 100;
            }

            buffer.vertex(x1, y1, 0).color(r, g, b, opacity).endVertex();
            buffer.vertex(x2, y2, 0).color(r, g, b, opacity).endVertex();
            buffer.vertex(x3, y3, 0).color(r, g, b, opacity).endVertex();
            buffer.vertex(x4, y4, 0).color(r, g, b, opacity).endVertex();
        }

        public Attribute getAttribute() {
            return attribute;
        }

        public void setAttribute(Attribute attribute) {
            this.attribute = attribute;
        }

        public Integer getStrength() {
            return strength;
        }

        public void setStrength(Integer strength) {
            this.strength = strength;
        }

        public PowerButtonContainer getContainer() {
            return container;
        }

        public byte getSlotIndex() {
            return slotIndex;
        }
    }

    public static class PowerButtonContainer {
        public Integer curioItemSlot;
        public Item item;

        protected double centerX;
        protected double centerY;

        // Refers to how many items can fit in a row and column
        protected int containWidth;
        protected int containHeight;

        protected double width;
        protected double height;

        public int red;
        public int green;
        public int blue;
        public int opacity;

        // Left top
        protected double x1;
        protected double y1;

        // left bottom
        protected double x2;
        protected double y2;

        // right bottom
        protected double x3;
        protected double y3;

        // right top
        protected double x4;
        protected double y4;

        // width * height
        protected int size;

        public boolean highlight;

        public ArrayList<PowerButton> menuButtons = new ArrayList<>();

        public PowerButtonContainer(double x, double y, int containWidth, int containHeight, int curioItemSlot,
                                    Item item) {
            this.centerX = x;
            this.centerY = y;

            red = 125;
            green = 125;
            blue = 125;
            opacity = 125;

            updateDimensions(containWidth, containHeight, centerX, centerY);

            this.red = 0;
            this.green = 0;
            this.blue = 0;
            this.curioItemSlot = curioItemSlot;
            this.item = item;
        }

        public void addButton(PowerButton button) {
            if (menuButtons.size() < size) {
                menuButtons.add(button);
                button.container = this;
                // assign slot index if applicable
                button.slotIndex = (byte) (menuButtons.size() - 1);
                arrangeButtons();
            } else {
                // container is full - ignore extra buttons
            }
        }

        public double getWidth() {
            return width;
        }

        public double getHeight() {
            return height;
        }

        public int getContainWidth() {
            return containWidth;
        }

        public int getContainHeight() {
            return containHeight;
        }

        public void arrangeButtons() {
            if (menuButtons.isEmpty()) {
                return;
            }

            final double spacing = 25.0;
            double totalWidth = (containWidth - 1) * spacing;
            double totalHeight = (containHeight - 1) * spacing;
            double startX = centerX - totalWidth / 2.0;
            double startY = centerY - totalHeight / 2.0;

            for (int i = 0; i < menuButtons.size(); i++) {
                int column = i % containWidth;
                int row = i / containWidth;
                double x = startX + column * spacing;
                double y = startY + row * spacing;
                menuButtons.get(i).setPosition(x, y);
            }
        }

        public void updateDimensions(int containWidth, int containHeight, double centerX, double centerY) {
            this.containWidth = Math.max(1, containWidth);
            this.containHeight = Math.max(1, containHeight);

            this.centerX = centerX;
            this.centerY = centerY;

            final double spacing = 25.0;
            this.width = (this.containWidth - 1) * spacing + 20.0;
            this.height = (this.containHeight - 1) * spacing + 20.0;

            size = this.containHeight * this.containWidth;

            int padding = 5;

            x1 = centerX - (width / 2.0) - padding;
            y1 = centerY - (height / 2.0) - padding;
            x2 = centerX - (width / 2.0) - padding;
            y2 = centerY + (height / 2.0) + padding;
            x3 = centerX + (width / 2.0) + padding;
            y3 = centerY + (height / 2.0) + padding;
            x4 = centerX + (width / 2.0) + padding;
            y4 = centerY - (height / 2.0) - padding;

            this.width = Math.abs(x1 - x4);
            this.height = Math.abs(y1 - y2);

            arrangeButtons();
        }

        public void highlightButtons(double mouseX, double mouseY, double middle_x, double middle_y) {
            for (PowerButton button : menuButtons) {
                button.highlightAction(mouseX, mouseY, middle_x, middle_y);
            }
            highlight = menuButtons.stream().anyMatch(b -> b.highlighted);
        }

        public void renderContainer(BufferBuilder buffer, double mouseX, double mouseY, double centerX,
                                    double centerY) {
            updateDimensions(containWidth, containHeight, centerX, centerY);

            highlightButtons(mouseX, mouseY, centerX, centerY);

            int rr = Math.max(0, red);
            int gg = Math.max(0, green);
            int bb = Math.max(0, blue);
            int aa = Math.max(0, opacity);

            buffer.vertex(x1, y1, 0).color(rr, gg, bb, aa).endVertex();
            buffer.vertex(x2, y2, 0).color(rr, gg, bb, aa).endVertex();
            buffer.vertex(x3, y3, 0).color(rr, gg, bb, aa).endVertex();
            buffer.vertex(x4, y4, 0).color(rr, gg, bb, aa).endVertex();

            for (PowerButton button : menuButtons) {
                button.renderButton(buffer, mouseX, mouseY);
            }
        }
    }

    private static final double PLAYER_BTN_HALF_SIZE = 10.0; // 20px square
    private static final double PLAYER_BTN_SPACING = 30.0;
    private static final double PLAYER_BTN_LABEL_GAP = 6.0; // gap above button

    private void renderPlayerSpiritwebButtons(BufferBuilder buffer, double mouseX, double mouseY, double middle_x,
                                              double middle_y) {
        if (playerSpiritwebPowerButtons.isEmpty()) {
            return;
        }

        double sidedBottomY = sidedMenuButtons.stream()
                .mapToDouble(b -> b.y2)
                .max()
                .orElse(middle_y - 90 + 9);
        if (sidedBottomY == 0) {
            return;
        }

        final double verticalGap = 30.0;
        final double rowCenterY = sidedBottomY + verticalGap;

        final int n = playerSpiritwebPowerButtons.size();
        final double halfSpan = (n - 1) * PLAYER_BTN_SPACING / 2.0;

        for (int i = 0; i < n; i++) {
            PowerButton btn = playerSpiritwebPowerButtons.get(i);
            if (heldButton != null && heldButton == btn) {
                continue; // held button rendered separately
            }

            double screenX = middle_x + (i * PLAYER_BTN_SPACING - halfSpan);
            double screenY = rowCenterY;

            btn.centerX = screenX;
            btn.centerY = screenY;

            double x1 = screenX - PLAYER_BTN_HALF_SIZE;
            double y1 = screenY - PLAYER_BTN_HALF_SIZE;
            double x2 = screenX - PLAYER_BTN_HALF_SIZE;
            double y2 = screenY + PLAYER_BTN_HALF_SIZE;
            double x3 = screenX + PLAYER_BTN_HALF_SIZE;
            double y3 = screenY + PLAYER_BTN_HALF_SIZE;
            double x4 = screenX + PLAYER_BTN_HALF_SIZE;
            double y4 = screenY - PLAYER_BTN_HALF_SIZE;

            boolean showHighlight =
                    MathHelper.inTriangle(x1, y1, x2, y2, x3, y3, mouseX, mouseY) ||
                            MathHelper.inTriangle(x1, y1, x4, y4, x3, y3, mouseX, mouseY);

            btn.highlighted = showHighlight;

            float brightness = showHighlight ? 0.25f : 0f;
            float r = brightness;
            float g = brightness;
            float b = brightness;
            float a = 0.5f;

            buffer.vertex(x1, y1, 0).color(r, g, b, a).endVertex();
            buffer.vertex(x2, y2, 0).color(r, g, b, a).endVertex();
            buffer.vertex(x3, y3, 0).color(r, g, b, a).endVertex();
            buffer.vertex(x4, y4, 0).color(r, g, b, a).endVertex();
        }
    }

    private void renderHeldButton(BufferBuilder buffer, double mouseX, double mouseY) {
        if (heldButton == null) {
            return;
        }

        PowerButton btn = heldButton;

        btn.centerX = mouseX;
        btn.centerY = mouseY;

        double x1 = btn.centerX - PLAYER_BTN_HALF_SIZE;
        double y1 = btn.centerY - PLAYER_BTN_HALF_SIZE;
        double x2 = btn.centerX - PLAYER_BTN_HALF_SIZE;
        double y2 = btn.centerY + PLAYER_BTN_HALF_SIZE;
        double x3 = btn.centerX + PLAYER_BTN_HALF_SIZE;
        double y3 = btn.centerY + PLAYER_BTN_HALF_SIZE;
        double x4 = btn.centerX + PLAYER_BTN_HALF_SIZE;
        double y4 = btn.centerY - PLAYER_BTN_HALF_SIZE;

        btn.highlighted = MathHelper.inTriangle(x1, y1, x2, y2, x3, y3, mouseX, mouseY) ||
                MathHelper.inTriangle(x1, y1, x4, y4, x3, y3, mouseX, mouseY);

        float r = 0.25f;
        float g = 0.25f;
        float b = 0.25f;
        float a = 0.5f;

        buffer.vertex(x1, y1, 0).color(r, g, b, a).endVertex();
        buffer.vertex(x2, y2, 0).color(r, g, b, a).endVertex();
        buffer.vertex(x3, y3, 0).color(r, g, b, a).endVertex();
        buffer.vertex(x4, y4, 0).color(r, g, b, a).endVertex();
    }


    public SpiritwebCapability getSpiritweb() {
        return spiritweb;
    }

    private ResourceLocation getAttributeIconLocation(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        String[] idParts = attribute.getDescriptionId().split("\\.");

        if (idParts.length < 3) {
            return null;
        }

        String modid = idParts[1];
        String name = idParts[2];

        ManifestationTypes type = CosmereAttributeUtils.getManifestationType(attribute);
        switch (type) {
            case ALLOMANCY:
            case FERUCHEMY:
            case SURGEBINDING:
            case SANDMASTERY:
                // supported
                break;
            default:
                // No icon for other types
                return null;
        }

        if (type == ManifestationTypes.SANDMASTERY) {
            int temp = 5;
        }

        // assets/<modid>/textures/icon/<modid>/<name>.png
        String path = "textures/icon/" + modid + "/" + name + ".png";

        return new ResourceLocation(modid, path);
    }


}
