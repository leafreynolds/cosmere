/*
 * File updated ~ 7 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.loottables;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;

public class LootTableGen extends BaseLootProvider
{
	public LootTableGen(PackOutput output)
	{
		super(output, List.of(
				new SubProviderEntry(BlockLootTableGen::new, LootContextParamSets.BLOCK),
				new SubProviderEntry(EntityLootTableGen::new, LootContextParamSets.ENTITY)
		));
	}
}
