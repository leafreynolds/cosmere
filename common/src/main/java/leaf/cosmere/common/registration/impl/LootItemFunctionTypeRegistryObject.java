package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.RegistryObject;

public class LootItemFunctionTypeRegistryObject<LOOT_ITEM_FUNCTION_TYPE extends LootItemFunctionType> extends WrappedRegistryObject<LOOT_ITEM_FUNCTION_TYPE>
{

	public LootItemFunctionTypeRegistryObject(RegistryObject<LOOT_ITEM_FUNCTION_TYPE> registryObject)
	{
		super(registryObject);
	}
}