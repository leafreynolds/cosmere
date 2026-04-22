package leaf.cosmere.common.registration.impl;

import com.mojang.serialization.MapCodec;
import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;

public class GlobalLootModifierRegistryObject<GLOBAL_LOOT_MOD extends MapCodec<? extends IGlobalLootModifier>> extends WrappedRegistryObject<GLOBAL_LOOT_MOD>
{
	public GlobalLootModifierRegistryObject(DeferredHolder<?, GLOBAL_LOOT_MOD> registryObject)
	{
		super(registryObject);
	}
}
