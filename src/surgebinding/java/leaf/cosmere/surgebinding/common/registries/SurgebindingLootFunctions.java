package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.common.registration.impl.LootFunctionDeferredRegister;
import leaf.cosmere.common.registration.impl.LootItemFunctionTypeRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.loot.SetupShardDataLootFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class SurgebindingLootFunctions
{
	public static final LootFunctionDeferredRegister LOOT_FUNCTIONS = new LootFunctionDeferredRegister(Surgebinding.MODID);

	public static final LootItemFunctionTypeRegistryObject<LootItemFunctionType> SETUP_DATA = LOOT_FUNCTIONS.registerType("setup_data", SetupShardDataLootFunction.Serializer::new);

}
