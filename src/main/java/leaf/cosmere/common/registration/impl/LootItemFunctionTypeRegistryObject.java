package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class LootItemFunctionTypeRegistryObject<LOOT_ITEM_FUNCTION_TYPE extends LootItemFunctionType<?>> extends WrappedRegistryObject<LOOT_ITEM_FUNCTION_TYPE>
{

	public LootItemFunctionTypeRegistryObject(DeferredHolder<? super LOOT_ITEM_FUNCTION_TYPE, LOOT_ITEM_FUNCTION_TYPE> registryObject)
	{
		super(registryObject);
	}
}
