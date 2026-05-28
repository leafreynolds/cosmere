package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class LootItemFunctionTypeRegistryObject<LOOT_ITEM_FUNCTION extends LootItemFunction> extends WrappedRegistryObject<LootItemFunctionType<LOOT_ITEM_FUNCTION>>
{

	public LootItemFunctionTypeRegistryObject(DeferredHolder<? super LootItemFunctionType<LOOT_ITEM_FUNCTION>, LootItemFunctionType<LOOT_ITEM_FUNCTION>> registryObject)
	{
		super(registryObject);
	}
}
