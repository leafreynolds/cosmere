/*
 * File updated ~ 26 - 4 - 2026 ~ Leaf
 */

package leaf.cosmere.surgebinding.loottables;

import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.loottables.BaseEntityLootTables;
import leaf.cosmere.surgebinding.common.registries.SurgebindingEntityTypes;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class SurgebindingEntityLootTableGen extends BaseEntityLootTables
{
	public SurgebindingEntityLootTableGen(HolderLookup.Provider provider)
	{
		super(provider);
	}

	@Override
	public void generate()
	{
		final LootPool.Builder lootPool = LootPool.lootPool()
				.name("gems")
				.setRolls(ConstantValue.exactly(1))
				.when(LootItemKilledByPlayerCondition.killedByPlayer())
				.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.1F, 0.05F));

		//gems aren't guaranteed to drop, generally.
		//The concept that the gems may be damaged while fighting or harvesting.
		for (var gemType : EnumUtils.GEMSTONE_TYPES)
		{
			lootPool.add(
					LootItem.lootTableItem(SurgebindingItems.GEMSTONE_BROAMS.get(gemType))
							.apply(SetItemCountFunction.setCount(UniformGenerator.between(-1.0F, 1.0F)))
			);
			lootPool.add(
					LootItem.lootTableItem(SurgebindingItems.GEMSTONE_MARKS.get(gemType))
							.apply(SetItemCountFunction.setCount(UniformGenerator.between(-1.0F, 2.0F)))
							.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
			);
			//chips are small enough that surely you'll get at least some
			lootPool.add(
					LootItem.lootTableItem(SurgebindingItems.GEMSTONE_CHIPS.get(gemType))
							.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
							.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
			);
		}

		add(
				SurgebindingEntityTypes.CHULL,
				LootTable.lootTable().withPool(lootPool)
		);
	}
}
