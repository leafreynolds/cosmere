package leaf.cosmere.sandmastery.api.trial;

import leaf.cosmere.sandmastery.common.entities.DiemTargetEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;

/**
 * One rule of a trial. A piece is a stateless definition; {@link #start} makes the {@link Instance} that holds one attempt's state.
 * Combine pieces by listing several in a {@link DiemTrial}.
 */
public interface TrialPiece
{
	Instance start(ITrialContext context);

	@Nullable
	default Component unavailableReason(ServerLevel level, BlockPos stone)
	{
		return null;
	}

	default String toJava()
	{
		return "/* " + this + " */";
	}

	interface Instance
	{
		default void tick()
		{
		}

		default void onShot()
		{
		}

		default void onTargetHit(DiemTargetEntity target)
		{
		}

		default void onSandPlaced(BlockPos pos)
		{
		}

		default void onSandSpent(int charge)
		{
		}

		default boolean isComplete()
		{
			return true;
		}

		/**
		 * @return why the trial failed, or null while this piece is still fine
		 */
		@Nullable
		default Component failReason()
		{
			return null;
		}

		/**
		 * @return ticks this piece adds to the trial's time limit, read once after every piece has started
		 */
		default int extraTimeTicks()
		{
			return 0;
		}

		/**
		 * @return short progress text for the boss bar (e.g. "3/7 targets"), or null for none
		 */
		@Nullable
		default Component progress()
		{
			return null;
		}

		default void cleanup()
		{
		}
	}
}
