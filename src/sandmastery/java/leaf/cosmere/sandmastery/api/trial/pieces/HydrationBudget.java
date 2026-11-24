package leaf.cosmere.sandmastery.api.trial.pieces;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.sandmastery.api.trial.ITrialContext;
import leaf.cosmere.sandmastery.api.trial.TrialPiece;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import net.minecraft.network.chat.Component;

/**
 * Losing more than {@code hydration} hydration during the trial fails it.
 */
public record HydrationBudget(int hydration) implements TrialPiece
{
	@Override
	public TrialPiece.Instance start(ITrialContext context)
	{
		return new Instance(context);
	}

	@Override
	public String toJava()
	{
		return "new HydrationBudget(" + hydration + ")";
	}

	private class Instance implements TrialPiece.Instance
	{
		private final ITrialContext context;
		private final int startingHydration;

		Instance(ITrialContext context)
		{
			this.context = context;
			this.startingHydration = currentHydration();
		}

		private int currentHydration()
		{
			return SpiritwebCapability.get(context.player())
					.map(spiritweb -> SandmasterySpiritwebSubmodule.get(spiritweb).getHydrationLevel())
					.orElse(0);
		}

		private int lost()
		{
			return startingHydration - currentHydration();
		}

		@Override
		public Component failReason()
		{
			return lost() > hydration ? Component.translatable("diem.sandmastery.trial.fail.hydration_budget") : null;
		}

		@Override
		public Component progress()
		{
			return Component.translatable("diem.sandmastery.trial.progress.hydration", Math.max(0, hydration - lost()));
		}
	}
}
