/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.registries;

import leaf.cosmere.common.registration.impl.LootFunctionDeferredRegister;
import leaf.cosmere.common.registration.impl.LootItemFunctionTypeRegistryObject;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.loot.InvestMetalmindLootFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class FeruchemyLootFunctions
{
	public static final LootFunctionDeferredRegister LOOT_FUNCTIONS = new LootFunctionDeferredRegister(Feruchemy.MODID);

	public static final LootItemFunctionTypeRegistryObject<LootItemFunctionType> INVEST_METALMIND = LOOT_FUNCTIONS.registerType("invest_metalmind", InvestMetalmindLootFunction.Serializer::new);

}
