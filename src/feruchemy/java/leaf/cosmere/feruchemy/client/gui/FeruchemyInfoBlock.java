package leaf.cosmere.feruchemy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
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

public class FeruchemyInfoBlock extends AbstractWidget
{
	private final ISpiritweb spiritweb;
	private final Manifestation manifestation;

	public FeruchemyInfoBlock(int pX, int pY, int pWidth, int pHeight, ISpiritweb spiritweb, Manifestation manifestation)
	{
		super(pX, pY, pWidth, pHeight, CommonComponents.EMPTY);

		this.spiritweb = spiritweb;
		this.manifestation = manifestation;
	}

	@Override
	protected void renderWidget(GuiGraphics pGuiGraphics, int i, int i1, float v)
	{
		Font font = Minecraft.getInstance().font;
		int x = getX();
		int y = getY();
		int width = getWidth();
		int height = getHeight();
		int color = 0x99333333;

		RenderSystem.disableCull();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		pGuiGraphics.fill(x, y, x + width,  y + height, color);

		int mode = manifestation.getMode(spiritweb);
		String text = I18n.get(manifestation.getTranslationKey());
		if (mode != 0)
		{
			text +=  ((mode > 0) ? " +" + mode : " " + (mode));
		}
		float scale = 0.8f;
		GuiUtils.drawScaledString(font, pGuiGraphics, text, x+5, y+10, scale, 0xFFFFFFFF);

		if (manifestation instanceof IHasMetalType metalMani && metalMani.getMetalType() != Metals.MetalType.NICROSIL)
		{
			int seconds = manifestation.getInvestitureRemaining(spiritweb);
			int hours = seconds / 3600;
			int minutes = (seconds % 3600) / 60;
			seconds = seconds % 60;

			if (hours > 0)
			{
				text = String.format("%d:%02d:%02d", hours, minutes, seconds);
			}
			else if (minutes > 0)
			{
				text = String.format("%d:%02d", minutes, seconds);
			}
			else if (seconds > 0)
			{
				text = String.format("%02d", seconds);
			}
			else
			{
				text = "Empty";
			}

			GuiUtils.drawScaledString(font, pGuiGraphics, text, x+5, y+12+font.lineHeight*scale, scale, 0xFFFFFFFF);
		}
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
	{

	}
}
