// /*
//  * File updated ~ 10 - 8 - 2024 ~ Leaf
//  */
package leaf.cosmere.sandmastery.client.gui;

import leaf.cosmere.sandmastery.common.items.sandpouch.SandPouchContainerMenu;
import leaf.cosmere.sandmastery.common.Sandmastery;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SandPouchContainerScreen extends AbstractContainerScreen<SandPouchContainerMenu>
{
	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Sandmastery.MODID, "textures/gui/sand_pouch.png");

	private static final int TOGGLE_X = 118;
	private static final int TOGGLE_Y = 17;
	private static final int TOGGLE_WIDTH = 36;
	private static final int TOGGLE_HEIGHT = 18;

	public SandPouchContainerScreen(SandPouchContainerMenu menu, Inventory playerInv, Component title)
	{
		super(menu, playerInv, title);
		this.imageWidth = 176;
		this.imageHeight = 131;
		this.inventoryLabelX = 8;
		this.inventoryLabelY = 37;
	}

	// two slots wide and one tall, directly right of the output slots (which end at x 116)
	@Override
	protected void init()
	{
		super.init();
		addRenderableWidget(Button.builder(Component.empty(), button ->
				      minecraft.gameMode.handleInventoryButtonClick(menu.containerId, SandPouchContainerMenu.BUTTON_TOGGLE_COLOUR))
		                          .bounds(leftPos + TOGGLE_X, topPos + TOGGLE_Y, TOGGLE_WIDTH, TOGGLE_HEIGHT)
		                          .build());
	}

	// Screen.render already draws the blurred background; calling renderBackground again doubles it.
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
	{
		// the label is read from the synced menu data, so it follows the server
		for (var widget : renderables)
		{
			if (widget instanceof Button button)
			{
				button.setMessage(Component.translatable(menu.isShowingBlack()
				                                         ? "gui.sandmastery.sand_pouch.taking_black"
				                                         : "gui.sandmastery.sand_pouch.taking_white"));
			}
		}
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		if (menu.shouldHighlightToggle())
		{
			guiGraphics.renderOutline(leftPos + TOGGLE_X - 1, topPos + TOGGLE_Y - 1, TOGGLE_WIDTH + 2, TOGGLE_HEIGHT + 2, 0xFFCE0A4A);
		}
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY)
	{
		guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
	}
}