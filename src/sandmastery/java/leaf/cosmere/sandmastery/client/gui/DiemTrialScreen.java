package leaf.cosmere.sandmastery.client.gui;

import leaf.cosmere.sandmastery.api.trial.SandmasteryTrialAPI;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.diem.DiemRank;
import leaf.cosmere.sandmastery.common.network.packets.OpenDiemTrialScreenMessage;
import leaf.cosmere.sandmastery.common.network.packets.StartDiemTrialMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class DiemTrialScreen extends Screen
{
	private static final int ROW_HEIGHT = 44;
	private static final int FIRST_ROW_Y = 64;
	private static final int TEXT_WIDTH = 250;

	private final OpenDiemTrialScreenMessage data;
	private final DiemRank nextRank;

	private DiemTrialScreen(OpenDiemTrialScreenMessage data)
	{
		super(Component.translatable("gui.sandmastery.diem_trials.title"));
		this.data = data;
		this.nextRank = DiemRank.values()[data.nextRank()];
	}

	public static void open(OpenDiemTrialScreenMessage data)
	{
		Minecraft.getInstance().setScreen(new DiemTrialScreen(data));
	}

	private boolean hasEnoughRibbons()
	{
		return data.ribbons() >= nextRank.getMinRibbons();
	}

	private boolean onCooldown()
	{
		return data.cooldownSeconds() > 0;
	}

	private int left()
	{
		return width / 2 - (TEXT_WIDTH + 130) / 2;
	}

	@Override
	protected void init()
	{
		int buttonX = left() + TEXT_WIDTH + 10;
		for (int i = 0; i < data.trials().size(); i++)
		{
			OpenDiemTrialScreenMessage.Entry trial = data.trials().get(i);
			int y = FIRST_ROW_Y + i * ROW_HEIGHT;

			Button start = Button.builder(Component.translatable("gui.sandmastery.diem_trials.start"), button -> send(trial, false))
					.bounds(buttonX, y, 60, 20)
					.build();
			start.active = hasEnoughRibbons() && !onCooldown() && trial.unavailable().isEmpty();
			trial.unavailable().ifPresent(reason -> start.setTooltip(Tooltip.create(reason)));
			addRenderableWidget(start);

			Button scout = Button.builder(Component.translatable("gui.sandmastery.diem_trials.scout"), button -> send(trial, true))
					.bounds(buttonX + 64, y, 56, 20)
					.tooltip(Tooltip.create(Component.translatable("gui.sandmastery.diem_trials.scout.tooltip")))
					.build();
			scout.active = trial.canScout() && trial.unavailable().isEmpty();
			addRenderableWidget(scout);
		}
	}

	private void send(OpenDiemTrialScreenMessage.Entry trial, boolean scoutOnly)
	{
		Sandmastery.packetHandler().sendToServer(new StartDiemTrialMessage(trial.id(), data.stone(), scoutOnly));
		onClose();
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
	{
		super.render(graphics, mouseX, mouseY, partialTick);

		int left = left();
		graphics.drawCenteredString(font, Component.translatable("gui.sandmastery.diem_trials.heading", nextRank.getDisplayName()), width / 2, 16, 0xFFFFFF);

		Component status;
		if (!hasEnoughRibbons())
		{
			status = Component.translatable("gui.sandmastery.diem_trials.need_ribbons", data.ribbons(), nextRank.getMinRibbons()).withStyle(ChatFormatting.RED);
		}
		else if (onCooldown())
		{
			status = Component.translatable("gui.sandmastery.diem_trials.cooldown", data.cooldownSeconds() / 60 + 1).withStyle(ChatFormatting.RED);
		}
		else
		{
			status = Component.translatable("gui.sandmastery.diem_trials.ready", data.ribbons()).withStyle(ChatFormatting.GREEN);
		}
		graphics.drawCenteredString(font, status, width / 2, 32, 0xFFFFFF);

		if (data.trials().isEmpty())
		{
			graphics.drawCenteredString(font, Component.translatable("gui.sandmastery.diem_trials.none"), width / 2, FIRST_ROW_Y, 0xAAAAAA);
		}

		for (int i = 0; i < data.trials().size(); i++)
		{
			OpenDiemTrialScreenMessage.Entry trial = data.trials().get(i);
			int y = FIRST_ROW_Y + i * ROW_HEIGHT;

			Component name = Component.translatable(SandmasteryTrialAPI.nameKey(trial.id()))
					.append(Component.literal("  " + trial.timeSeconds() + "s").withStyle(ChatFormatting.GRAY));
			graphics.drawString(font, name, left, y, 0xFFFFFF);

			// the description, or why the trial can't be attempted here
			Component detail = trial.unavailable()
					.map(reason -> reason.copy().withStyle(ChatFormatting.RED))
					.orElse(Component.translatable(SandmasteryTrialAPI.descriptionKey(trial.id())).withStyle(ChatFormatting.GRAY));
			List<FormattedCharSequence> lines = font.split(detail, TEXT_WIDTH);
			for (int line = 0; line < Math.min(2, lines.size()); line++)
			{
				graphics.drawString(font, lines.get(line), left, y + 11 + line * 10, 0xFFFFFF);
			}
		}
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
}
