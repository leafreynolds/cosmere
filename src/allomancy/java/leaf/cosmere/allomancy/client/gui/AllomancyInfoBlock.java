package leaf.cosmere.allomancy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import leaf.cosmere.allomancy.common.capabilities.AllomancySpiritwebSubmodule;
import leaf.cosmere.allomancy.common.manifestation.AllomancyManifestation;
import leaf.cosmere.allomancy.common.manifestation.AllomancyPewter;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.client.gui.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class AllomancyInfoBlock extends AbstractWidget
{
	private final ISpiritweb spiritweb;
	private final AllomancyManifestation manifestation;

	public AllomancyInfoBlock(int pX, int pY, int pWidth, int pHeight, ISpiritweb spiritweb, AllomancyManifestation manifestation)
	{
		super(pX, pY, pWidth, pHeight, CommonComponents.EMPTY);

		this.spiritweb = spiritweb;
		this.manifestation = manifestation;
	}

	@Override
	protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		Font font = Minecraft.getInstance().font;
		float delayedDamagePewter = 0.f;
		float scale = 0.8f;
		int x = getX()+5;
		int y = getY();
		int width = getWidth();
		int height = getHeight();
		if (manifestation.getMetalType() == Metals.MetalType.PEWTER)
		{
			delayedDamagePewter = AllomancySpiritwebSubmodule.getSubmodule(spiritweb).getPewterDelayedDamage();
			if (delayedDamagePewter > 0)
			{
				height += font.lineHeight;
			}
		}
		int color = 0x99333333;

		RenderSystem.disableCull();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		pGuiGraphics.fill(x, y, x + width,  y + height, color);

		String text = I18n.get(manifestation.getTranslationKey());
		GuiUtils.drawScaledString(font, pGuiGraphics, text, x, y+10, scale, 0xFFFFFFFF);

		if (delayedDamagePewter > 0)
		{
			String delayedDamageText = String.format("Delayed damage: %.1f", delayedDamagePewter);
			GuiUtils.drawScaledString(font, pGuiGraphics, delayedDamageText, x, y+12+font.lineHeight*2, scale, 0xFFFFFFFF);
		}

		int seconds = manifestation.getInvestitureRemaining(spiritweb);
		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		seconds = seconds % 60;

		if (hours > 0)
			text = String.format("%d:%02d:%02d", hours, minutes, seconds);
		else if (minutes > 0)
			text = String.format("%d:%02d", minutes, seconds);
		else if (seconds > 0)
			text = String.format("%02d", seconds);
		else
			text = "Empty";

		GuiUtils.drawScaledString(font, pGuiGraphics, text, x, y+12+font.lineHeight*scale, scale, 0xFFFFFFFF);
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
	{

	}
}
