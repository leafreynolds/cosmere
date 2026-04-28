package leaf.cosmere.surgebinding.common.worldgen;

import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class SurgebindingOrePlacement
{
	public static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_)
	{
		return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
	}

	public static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange)
	{
		return orePlacement(CountPlacement.of(pCount), pHeightRange);
	}

	public static List<PlacementModifier> rareOrePlacement(int pChance, PlacementModifier pHeightRange)
	{
		return orePlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange);
	}

	public static List<PlacementModifier> geodePlacement(int chance, PlacementModifier pHeightRange)
	{
		return List.of(RarityFilter.onAverageOnceEvery(chance), InSquarePlacement.spread(), pHeightRange);
	}
}
