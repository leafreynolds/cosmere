package leaf.cosmere.sandmastery.api.trial.pieces;

import leaf.cosmere.sandmastery.api.trial.ITrialContext;
import leaf.cosmere.sandmastery.api.trial.TrialPiece;
import net.minecraft.network.chat.Component;

/**
 * Spending more than {@code charge} sand charge fails the trial. A layer of sand is 10 charge.
 */
public record SandBudget(int charge) implements TrialPiece
{
	@Override
	public TrialPiece.Instance start(ITrialContext context)
	{
		return new Instance();
	}

	@Override
	public String toJava()
	{
		return "new SandBudget(" + charge + ")";
	}

	private class Instance implements TrialPiece.Instance
	{
		private int spent = 0;

		@Override
		public void onSandSpent(int amount)
		{
			spent += amount;
		}

		@Override
		public Component failReason()
		{
			return spent > charge ? Component.translatable("diem.sandmastery.trial.fail.sand_budget") : null;
		}

		@Override
		public Component progress()
		{
			return Component.translatable("diem.sandmastery.trial.progress.sand", Math.max(0, charge - spent));
		}
	}
}
