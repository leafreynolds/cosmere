package leaf.cosmere.sandmastery.api.trial.pieces;

import leaf.cosmere.sandmastery.api.trial.ITrialContext;
import leaf.cosmere.sandmastery.api.trial.TrialPiece;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Once the player has left the ground, landing on anything but mastered sand fails the trial.
 */
public record NoGround() implements TrialPiece
{
	@Override
	public TrialPiece.Instance start(ITrialContext context)
	{
		return new Instance(context);
	}

	@Override
	public String toJava()
	{
		return "new NoGround()";
	}

	private static class Instance implements TrialPiece.Instance
	{
		private final ITrialContext context;
		private boolean leftGround = false;
		private boolean touchedGround = false;

		Instance(ITrialContext context)
		{
			this.context = context;
		}

		@Override
		public void tick()
		{
			ServerPlayer player = context.player();
			if (!player.onGround())
			{
				leftGround = true;
			}
			else if (leftGround && !player.level().getBlockState(player.getOnPos()).is(SandmasteryBlocks.TEMPORARY_SAND_BLOCK.getBlock()))
			{
				touchedGround = true;
			}
		}

		@Override
		public Component failReason()
		{
			return touchedGround ? Component.translatable("diem.sandmastery.trial.fail.touched_ground") : null;
		}
	}
}
