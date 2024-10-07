/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.loottables;

import leaf.cosmere.api.Roshar;
import leaf.cosmere.loottables.BaseEntityLootTables;
import leaf.cosmere.surgebinding.common.registries.SurgebindingEntityTypes;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class SurgebindingEntityLootTableGen extends BaseEntityLootTables
{

	@Override
	public void generate()
	{
		final LootPool.Builder lootPool = LootPool.lootPool()
				.name("gems")
				.setRolls(ConstantValue.exactly(1))
				.when(LootItemKilledByPlayerCondition.killedByPlayer())
				.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.1F, 0.05F));

		//gems aren't guaranteed to drop, generally.
		//The concept that the gems may be damaged while fighting or harvesting.
		for (var gemType : Roshar.Gemstone.values())
		{
			lootPool.add(
					LootItem.lootTableItem(SurgebindingItems.GEMSTONE_BROAMS.get(gemType))
							.apply(SetItemCountFunction.setCount(UniformGenerator.between(-1.0F, 1.0F)))
			);
			lootPool.add(
					LootItem.lootTableItem(SurgebindingItems.GEMSTONE_MARKS.get(gemType))
							.apply(SetItemCountFunction.setCount(UniformGenerator.between(-1.0F, 2.0F)))
							.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
			);
			//chips are small enough that surely you'll get at least some
			lootPool.add(
					LootItem.lootTableItem(SurgebindingItems.GEMSTONE_CHIPS.get(gemType))
							.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
							.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
			);
		}

		add(
				SurgebindingEntityTypes.CHULL,
				LootTable.lootTable().withPool(lootPool)
		);
	}
}