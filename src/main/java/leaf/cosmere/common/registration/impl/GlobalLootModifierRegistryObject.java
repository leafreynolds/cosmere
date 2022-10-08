package leaf.cosmere.common.registration.impl;

import com.mojang.serialization.Codec;
import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.RegistryObject;

public class GlobalLootModifierRegistryObject<GLOBAL_LOOT_MOD extends Codec<? extends IGlobalLootModifier>> extends WrappedRegistryObject<GLOBAL_LOOT_MOD>
{
	public GlobalLootModifierRegistryObject(RegistryObject<GLOBAL_LOOT_MOD> registryObject)
	{
		super(registryObject);
	}
}