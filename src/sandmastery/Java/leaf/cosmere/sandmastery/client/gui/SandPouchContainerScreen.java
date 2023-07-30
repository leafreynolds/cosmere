package leaf.cosmere.sandmastery.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.items.sandpouch.SandPouchContainerMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SandPouchContainerScreen extends AbstractContainerScreen<SandPouchContainerMenu>
{
    public SandPouchContainerScreen(SandPouchContainerMenu container, Inventory playerInv, Component title)
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
        RenderSystem.setShaderTexture(0, new ResourceLocation(Sandmastery.MODID, "textures/gui/sand_pouch.png"));

        int xPos = (width - imageWidth) / 2;
        int yPos = (height / 2) - (imageHeight / 2);
        blit(matrixStack, xPos, yPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY)
    {
        //write the name of the itemstack
        this.font.draw(matrixStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
//        this.font.draw(matrixStack, "Number of layers available: " + MiscHelper.intToAbbreviatedStr(this.menu.slots.get(2).getItem().getCount()), this.titleLabelX, this.titleLabelY + 32, 4210752);
    }
}
