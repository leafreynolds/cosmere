package leaf.cosmere.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.config.CosmereConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.awt.*;

public class SpiritwebHud extends AbstractWidget
{
	private Player player;

	public SpiritwebHud(Player player)
	{
		super(CosmereConfigs.CLIENT_CONFIG.hudXCoordinate.get(),
				CosmereConfigs.CLIENT_CONFIG.hudYCoordinate.get(),
				CosmereConfigs.CLIENT_CONFIG.hudSizeX.get(),
				CosmereConfigs.CLIENT_CONFIG.hudSizeY.get(),
				Component.literal("Spiritweb HUD"));
		this.player = player;
	}

	@Override
	protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		SpiritwebCapability.get(player).ifPresent( (spiritweb -> {
			if (!CosmereConfigs.CLIENT_CONFIG.disableSelectedManifestationHud.get()
					&& spiritweb.getSelectedManifestation().getManifestationType() != Manifestations.ManifestationTypes.NONE)
			{
				renderBackground(pGuiGraphics);
				renderUsage(pGuiGraphics);
				renderIcon(pGuiGraphics);
				renderText(pGuiGraphics);
			}
		}));
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput)
	{
		// no
	}

	protected void renderBackground(GuiGraphics pGuiGraphics)
	{
		pGuiGraphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xCC333333);
	}

	protected void renderUsage(GuiGraphics pGuiGraphics)
	{
		SpiritwebCapability.get(player).ifPresent(spiritweb ->
		{
			Manifestation manifestation = spiritweb.getSelectedManifestation();

			int mode = manifestation.getMode(spiritweb);
			float r, g, b, a;
			// yes this is strange, but I'm min-maxxing contrast when a power mode is 0
			if (mode != 0)
			{
				r = GuiUtils.BACKGROUND_COLOR.getRed() / 256.f;
				g = GuiUtils.BACKGROUND_COLOR.getGreen() / 256.f;
				b = GuiUtils.BACKGROUND_COLOR.getBlue() / 256.f;
				a = 0.8f;
			}
			else
			{
				r = g = b = 0.6f;
				a = 0.2f;
			}

			if (manifestation.getManifestationType() == Manifestations.ManifestationTypes.FERUCHEMY)
			{
				float intensity = 0;
				if (mode < 0)
					intensity = Math.min(Math.abs(mode) * (1f/16f), 1.0f);
				if (mode > 0)
					intensity = Math.min(Math.abs(mode) * (1f/5f), 1.0f);

				if (mode > 0)
				{
					r = lerp(r, 1.0f, intensity);
					g = lerp(g, 0.0f, intensity);
					b = lerp(b, 0.0f, intensity);
				}
				else if (mode < 0) {
					r = lerp(r, 0.0f, intensity);
					g = lerp(g, 0.0f, intensity);
					b = lerp(b, 1.0f, intensity);
				}
			}
			else if (manifestation.getManifestationType() == Manifestations.ManifestationTypes.ALLOMANCY)
			{
				float intensity = Math.min(Math.abs(mode) * 0.2f, 1.0f);

				if (mode > 0)
				{
					r = lerp(r, 1.0f, intensity);
					g = lerp(g, 0.0f, intensity);
					b = lerp(b, 0.0f, intensity);
				}
				else if (mode < 0)
				{
					r = lerp(r, 0.0f, intensity);
					g = lerp(g, 0.0f, intensity);
					b = lerp(b, 1.0f, intensity);
				}
			}

			int color = toHex(new Color(r, g, b, a));

			float width = getWidth();
			float usagePercentage = spiritweb.getSelectedManifestation().getInvestitureHud(spiritweb);

			width = width * usagePercentage;
			pGuiGraphics.fill(getX(), getY(), (int) (getX() + width), getY() + getHeight(), color);
		});
	}

	protected void renderIcon(GuiGraphics pGuiGraphics)
	{
		SpiritwebCapability.get(player).ifPresent(spiritweb ->
		{
			Manifestation selectedManifestation = spiritweb.getSelectedManifestation();
			if (selectedManifestation != null)
			{
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.setLength(0);
				stringBuilder.append("textures/icon/")
						.append(selectedManifestation.getManifestationType().getName())
						.append("/");

				switch (selectedManifestation.getManifestationType())
				{
					case ALLOMANCY:
					case FERUCHEMY:
						if (selectedManifestation instanceof IHasMetalType metalType)
						{
							stringBuilder.append(metalType.getMetalType().getName());
						}
						break;
					case SURGEBINDING:
						stringBuilder.append(selectedManifestation.getName());
						break;
					case AON_DOR:
						break;
					case AWAKENING:
						break;
				}

				stringBuilder.append(".png");
				final ResourceLocation resourceLocation = new ResourceLocation(selectedManifestation.getRegistryName().getNamespace(), stringBuilder.toString());
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();

				pGuiGraphics.blit(resourceLocation,
						getX(),
						getY() + 2,
						getHeight()-4,
						getHeight()-4,
						0,
						0,
						18,
						18,
						18,
						18);
			}
		});
	}

	protected void renderText(GuiGraphics pGuiGraphics)
	{
		SpiritwebCapability.get(player).ifPresent(spiritweb ->
		{
			Font font = Minecraft.getInstance().font;
			String text = I18n.get(spiritweb.getSelectedManifestation().getTranslationKey());
			float scale = 0.8f;

			float targetX = getX() + getHeight() + 2;
			float targetY = getY() + (getHeight() / 2f) - ((font.lineHeight * scale) / 2f);

			GuiUtils.drawScaledString(font, pGuiGraphics, text, targetX, targetY, scale, 0xFFDDDDDD);
		});
	}

	protected int toHex(Color color)
	{
		return (color.getAlpha() << 24) |
				(color.getRed()   << 16) |
				(color.getGreen() << 8)  |
				color.getBlue();
	}

	public float lerp(float start, float end, float pct) {
		return start + pct * (end - start);
	}
}
