package leaf.cosmere.sandmastery.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.List;

// Hey look, a locator bar backport lmao <3
public class CheckpointBarHUD
{
	private static final int BAR_WIDTH = 182;
	// checkpoints more than this many blocks above or below get an arrow
	private static final double ARROW_HEIGHT = 3;

	private static final int BACKGROUND = 0x80000000;
	private static final int NEXT_COLOUR = 0xFFFFD700;
	private static final int LATER_COLOUR = 0xFF8A6A20;

	private static List<Vec3> checkpoints = List.of();
	// index to highlight, or -1 when every checkpoint counts
	private static int next = -1;

	public static void setCheckpoints(List<Vec3> points, int nextIndex)
	{
		checkpoints = points;
		next = nextIndex;
	}

	public static boolean isShowing()
	{
		return !checkpoints.isEmpty();
	}

	public static void render(GuiGraphics graphics)
	{
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (!isShowing() || player == null || mc.options.hideGui)
		{
			return;
		}

		// same place as the vanilla XP bar
		int left = graphics.guiWidth() / 2 - BAR_WIDTH / 2;
		int top = graphics.guiHeight() - 29;
		graphics.fill(left, top, left + BAR_WIDTH, top + 5, BACKGROUND);

		// the bar spans the screen's width of view; the FOV setting is vertical, so widen it by the window's aspect ratio
		double aspect = (double) mc.getWindow().getWidth() / mc.getWindow().getHeight();
		double halfViewTan = Math.tan(Math.toRadians(mc.options.fov().get() / 2.0)) * aspect;
		float halfViewDegrees = (float) Math.toDegrees(Math.atan(halfViewTan));

		for (int i = 0; i < checkpoints.size(); i++)
		{
			Vec3 point = checkpoints.get(i);
			double dx = point.x - player.getX();
			double dz = point.z - player.getZ();
			double dy = point.y - player.getEyeY();

			// angle between where the player looks and the checkpoint: 0 is straight ahead, negative is to the left.
			// Off-screen checkpoints sit at the edge; on-screen ones line up with where they appear (tan matches the perspective).
			float yawToPoint = (float) Math.toDegrees(Math.atan2(-dx, dz));
			float relative = Mth.clamp(Mth.wrapDegrees(yawToPoint - player.getYRot()), -halfViewDegrees, halfViewDegrees);
			double offset = Math.tan(Math.toRadians(relative)) / halfViewTan;
			int x = left + BAR_WIDTH / 2 + (int) Math.round(offset * (BAR_WIDTH / 2F - 3));

			boolean highlighted = next == -1 || i == next;
			graphics.fill(x - 2, top, x + 3, top + 5, highlighted ? NEXT_COLOUR : LATER_COLOUR);

			if (dy > ARROW_HEIGHT)
			{
				graphics.drawCenteredString(mc.font, "^", x + 1, top - 9, NEXT_COLOUR);
			}
			else if (dy < -ARROW_HEIGHT)
			{
				graphics.drawCenteredString(mc.font, "v", x + 1, top + 6, NEXT_COLOUR);
			}
		}
	}
}
