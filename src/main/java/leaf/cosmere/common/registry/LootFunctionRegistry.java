/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to the curio mod for providing the example of how to set up fortune bonus for non-tool related things.
 */

package leaf.cosmere.common.registry;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.loot.RandomiseMetalTypeLootFunction;
import leaf.cosmere.common.registration.impl.LootFunctionDeferredRegister;
import leaf.cosmere.common.registration.impl.LootItemFunctionTypeRegistryObject;

public class LootFunctionRegistry
{
	public static final LootFunctionDeferredRegister LOOT_FUNCTIONS = new LootFunctionDeferredRegister(Cosmere.MODID);

	public static final LootItemFunctionTypeRegistryObject<RandomiseMetalTypeLootFunction> RANDOMISE_METALTYPE =
			LOOT_FUNCTIONS.register("randomise_metaltype", () -> RandomiseMetalTypeLootFunction.CODEC);

}
