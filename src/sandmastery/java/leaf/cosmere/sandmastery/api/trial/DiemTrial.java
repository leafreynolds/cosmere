package leaf.cosmere.sandmastery.api.trial;

import leaf.cosmere.sandmastery.common.diem.DiemRank;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A trial for the given rank: passed when every piece is complete before the time runs out.
 * The time limit is {@code timeLimitSeconds} plus whatever the pieces add (see {@link TrialPiece.Instance#extraTimeTicks()}).
 */
public record DiemTrial(DiemRank rank, int timeLimitSeconds, List<TrialPiece> pieces)
{
	public DiemTrial
	{
		pieces = List.copyOf(pieces);
	}

	// Java source for this trial, used by /cosmere diem trial draft export
	public String toJava()
	{
		return "new DiemTrial(DiemRank." + rank.name() + ", " + timeLimitSeconds + ", List.of(\n\t\t"
				+ pieces.stream().map(TrialPiece::toJava).collect(Collectors.joining(",\n\t\t"))
				+ "))";
	}
}
