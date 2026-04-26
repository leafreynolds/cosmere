/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.registries;

import leaf.cosmere.common.registration.impl.LootFunctionDeferredRegister;
import leaf.cosmere.common.registration.impl.LootItemFunctionTypeRegistryObject;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.loot.InvestSpikeLootFunction;

public class HemalurgyLootFunctions
{
	public static final LootFunctionDeferredRegister LOOT_FUNCTIONS = new LootFunctionDeferredRegister(Hemalurgy.MODID);

	public static final LootItemFunctionTypeRegistryObject<InvestSpikeLootFunction> INVEST_SPIKE =
			LOOT_FUNCTIONS.register("invest_spike", () -> InvestSpikeLootFunction.CODEC);

}
