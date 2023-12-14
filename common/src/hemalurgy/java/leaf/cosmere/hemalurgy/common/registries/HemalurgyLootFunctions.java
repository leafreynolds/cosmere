/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.registries;

import leaf.cosmere.common.registration.impl.LootFunctionDeferredRegister;
import leaf.cosmere.common.registration.impl.LootItemFunctionTypeRegistryObject;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.loot.InvestSpikeLootFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class HemalurgyLootFunctions
{
	public static final LootFunctionDeferredRegister LOOT_FUNCTIONS = new LootFunctionDeferredRegister(Hemalurgy.MODID);

	public static final LootItemFunctionTypeRegistryObject<LootItemFunctionType> INVEST_SPIKE = LOOT_FUNCTIONS.registerType("invest_spike", InvestSpikeLootFunction.Serializer::new);

}
