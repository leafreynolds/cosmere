/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.allomancy.client.gui;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.coinpouch.CoinPouchContainerMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


public class CoinPouchContainerScreen extends AbstractContainerScreen<CoinPouchContainerMenu>
{
	final ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(Allomancy.MODID, "textures/gui/coin_pouch.png");

	public CoinPouchContainerScreen(CoinPouchContainerMenu container, Inventory playerInv, Component title)
	{
		super(container, playerInv, title);
		minecraft = Minecraft.getInstance();
	}

	@Override
	protected void init()
	{
		this.imageHeight = 140;
		this.imageWidth = 193;

		super.init();
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY)
	{
		int xPos = (width - imageWidth) / 2;
		int yPos = (height / 2) - (imageHeight / 2);
		guiGraphics.blit(resourceLocation, xPos, yPos, 0, 0, imageWidth, imageHeight);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
	{
		//write the name of the itemstack
		guiGraphics.drawString(this.font, this.title.getString(), this.titleLabelX, this.titleLabelY, 4210752, false);
	}

}
