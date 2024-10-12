/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.allomancy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.coinpouch.CoinPouchContainerMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


public class CoinPouchContainerScreen extends AbstractContainerScreen<CoinPouchContainerMenu>
{
	final ResourceLocation resourceLocation = new ResourceLocation(Allomancy.MODID, "textures/gui/coin_pouch.png");

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
	public void render(GuiGraphics guiGraphics, int x, int y, float partialTicks)
	{
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, x, y, partialTicks);
		this.renderTooltip(guiGraphics, x, y);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, resourceLocation);

		int xPos = (width - imageWidth) / 2;
		int yPos = (height / 2) - (imageHeight / 2);
		guiGraphics.blit(resourceLocation, xPos, yPos, 0, 0, imageWidth, imageHeight);

	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
	{
		//write the name of the itemstack
		guiGraphics.drawString(this.font, this.title.toString(), this.titleLabelX, this.titleLabelY, 4210752);
	}

}
