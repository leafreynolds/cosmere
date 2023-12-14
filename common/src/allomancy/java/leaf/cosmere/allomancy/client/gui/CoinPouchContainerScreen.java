/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.coinpouch.CoinPouchContainerMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


public class CoinPouchContainerScreen extends AbstractContainerScreen<CoinPouchContainerMenu>
{
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
	public void render(PoseStack matrixStack, int x, int y, float partialTicks)
	{
		this.renderBackground(matrixStack);
		super.render(matrixStack, x, y, partialTicks);
		this.renderTooltip(matrixStack, x, y);
	}

	@Override
	protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, new ResourceLocation(Allomancy.MODID, "textures/gui/coin_pouch.png"));

		int xPos = (width - imageWidth) / 2;
		int yPos = (height / 2) - (imageHeight / 2);
		blit(matrixStack, xPos, yPos, 0, 0, imageWidth, imageHeight);

	}

	@Override
	protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY)
	{
		//write the name of the itemstack
		this.font.draw(matrixStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
	}

}
