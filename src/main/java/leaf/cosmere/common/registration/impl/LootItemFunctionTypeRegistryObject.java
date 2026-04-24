package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class LootItemFunctionTypeRegistryObject<T extends LootItemFunction> extends WrappedRegistryObject<LootItemFunctionType<T>>
{

	public LootItemFunctionTypeRegistryObject(DeferredHolder<? super LootItemFunctionType<T>, LootItemFunctionType<T>> registryObject)
	{
		super(registryObject);
	}
}
