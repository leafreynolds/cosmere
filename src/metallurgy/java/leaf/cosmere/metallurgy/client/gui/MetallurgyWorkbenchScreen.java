package leaf.cosmere.metallurgy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import leaf.cosmere.metallurgy.common.Metallurgy;
import leaf.cosmere.metallurgy.common.items.AlloyFragmentItem;
import leaf.cosmere.metallurgy.common.menus.MetallurgyWorkbenchMenu;
import leaf.cosmere.metallurgy.common.network.packets.CutIngotPacket;
import leaf.cosmere.metallurgy.common.util.AlloyComposition;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class MetallurgyWorkbenchScreen extends AbstractContainerScreen<MetallurgyWorkbenchMenu> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(Metallurgy.MODID,
			"textures/gui/metallurgy_workbench.png");
	private Button cutButton;

	private static final int CUT_BOX_X = 12;
	private static final int CUT_BOX_Y = 38;
	private static final int CUT_BOX_MAX_WIDTH = 152; // Maximum width for a full ingot
	private static final int CUT_BOX_HEIGHT = 45;

	private static final int MIN_CUT_PERCENTAGE = 5;
	private static final int MAX_CUT_PERCENTAGE = 95;

	private boolean isDraggingSlider = false;
	private ItemStack lastInputStack = ItemStack.EMPTY; // Track input changes

	public MetallurgyWorkbenchScreen(MetallurgyWorkbenchMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
		this.imageHeight = 192;
		this.imageWidth = 176;
	}

	@Override
	protected void init() {
		super.init();

		int xZero = (width - imageWidth) / 2;
		int yZero = (height - imageHeight) / 2;

		cutButton = Button.builder(Component.literal("Cut"), button -> {
			Metallurgy.packetHandler().sendToServer(new CutIngotPacket(menu.getBlockPos(), menu.getCutPercentage()));
		})
				.bounds(xZero + 26, yZero + 89, 124, 16)
				.build();

		addRenderableWidget(cutButton);
	}

	@Override
	protected void containerTick() {
		super.containerTick();

		if (cutButton != null) {
			cutButton.active = canCut();
		}

		ItemStack currentInput = menu.getSlot(0).getItem();
		if (!ItemStack.isSameItemSameTags(lastInputStack, currentInput)) {
			menu.clearCutLine();
			lastInputStack = currentInput.copy();
		}
	}

	/**
	 * Check if cutting is possible based on current fragment size
	 * 
	 * @return true if the fragment is large enough to cut
	 */
	private boolean canCut() {
		ItemStack inputStack = menu.getSlot(0).getItem();
		if (inputStack.isEmpty()) {
			return false;
		}

		if (!menu.getSlot(1).getItem().isEmpty()) {
			return false;
		}

		ItemStack chiselStack = menu.getSlot(2).getItem();
		if (chiselStack.isEmpty()) {
			return false;
		}

		double currentPercentage = getInputFragmentPercentage();

		int cutPercentage = menu.getCutPercentage();

		double piece1 = currentPercentage * (cutPercentage / 100.0);
		double piece2 = currentPercentage * ((100 - cutPercentage) / 100.0);

		return piece1 >= 0.05 && piece2 >= 0.05;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		ItemStack inputStack = menu.getSlot(0).getItem();
		if (inputStack.isEmpty()) {
			return super.mouseClicked(mouseX, mouseY, button);
		}
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;

		int currentBarWidth = getCurrentBarWidth();

		int sliderX = x + CUT_BOX_X;
		int sliderY = y + CUT_BOX_Y;

		if (mouseX >= sliderX && mouseX <= sliderX + currentBarWidth &&
				mouseY >= sliderY && mouseY <= sliderY + CUT_BOX_HEIGHT) {
			isDraggingSlider = true;
			updateSliderFromMouse(mouseX, x);
			return true;
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (isDraggingSlider) {
			isDraggingSlider = false;
			return true;
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (isDraggingSlider) {
			int x = (width - imageWidth) / 2;
			updateSliderFromMouse(mouseX, x);
			return true;
		}
		return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}

	private void updateSliderFromMouse(double mouseX, int guiX) {
		int currentBarWidth = getCurrentBarWidth();
		int sliderX = guiX + CUT_BOX_X;
		double relativeX = mouseX - sliderX;
		double percentage = (relativeX / currentBarWidth) * 100.0;

		percentage = Mth.clamp(percentage, MIN_CUT_PERCENTAGE, MAX_CUT_PERCENTAGE);
		menu.setCutPercentage((int) Math.round(percentage));
	}

	/**
	 * Gets the current fragment percentage of the input item
	 * 
	 * @return percentage from 0.0 to 1.0, or 1.0 if no item or full ingot
	 */
	private double getInputFragmentPercentage() {
		ItemStack inputStack = menu.getSlot(0).getItem();
		if (inputStack.isEmpty()) {
			return 1.0;
		}

		if (inputStack.getItem() instanceof AlloyFragmentItem) {
			return AlloyComposition.getFragmentPercentage(inputStack);
		}

		return 1.0; // Full ingot
	}

	/**
	 * Calculate the current bar width based on the fragment percentage
	 * 
	 * @return the scaled width of the bar
	 */
	private int getCurrentBarWidth() {
		double fragmentPercentage = getInputFragmentPercentage();
		return (int) (CUT_BOX_MAX_WIDTH * fragmentPercentage);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
		renderCutBox(guiGraphics, x, y);
	}

	private void renderCutBox(GuiGraphics guiGraphics, int guiX, int guiY) {
		ItemStack inputStack = menu.getSlot(0).getItem();
		if (inputStack.isEmpty()) {
			return;
		}
		int currentBarWidth = getCurrentBarWidth();
		int percentage = menu.getCutPercentage();

		int splitX = guiX + CUT_BOX_X + (int) ((percentage / 100.0) * currentBarWidth);

		int xZero = (width - imageWidth) / 2;
		int yZero = (height - imageHeight) / 2;
		guiGraphics.blit(TEXTURE, xZero + CUT_BOX_X, yZero + CUT_BOX_Y, 0, this.imageHeight, currentBarWidth,
				CUT_BOX_HEIGHT);

		if (menu.hasCutLineSet()) {
			guiGraphics.fill(
					splitX - 1,
					guiY + CUT_BOX_Y - 2,
					splitX + 1,
					guiY + CUT_BOX_Y + CUT_BOX_HEIGHT + 2,
					0xFFCCCCCC);
		}
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, delta);
		renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
	}
}
