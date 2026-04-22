package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import java.util.function.Supplier;

public class LootFunctionDeferredRegister extends WrappedDeferredRegister<LootItemFunctionType<?>>
{

	public LootFunctionDeferredRegister(String modid)
	{
		super(modid, Registries.LOOT_FUNCTION_TYPE);
	}

	public <LOOT_ITEM_FUNCTION_TYPE extends LootItemFunctionType<?>> LootItemFunctionTypeRegistryObject<LOOT_ITEM_FUNCTION_TYPE> register(String name, Supplier<LOOT_ITEM_FUNCTION_TYPE> sup)
	{
		return register(name, sup, LootItemFunctionTypeRegistryObject::new);
	}
}
