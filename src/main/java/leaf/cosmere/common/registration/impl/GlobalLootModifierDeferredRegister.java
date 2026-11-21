package leaf.cosmere.common.registration.impl;

import com.mojang.serialization.MapCodec;
import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class GlobalLootModifierDeferredRegister<T extends MapCodec<? extends IGlobalLootModifier>> extends WrappedDeferredRegister<MapCodec<? extends IGlobalLootModifier>>
{

	public GlobalLootModifierDeferredRegister(String modid)
	{
		super(modid, NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS);
	}

	public <I extends T> GlobalLootModifierRegistryObject<I> register(String name, final Supplier<? extends I> sup)
	{
		return register(name, sup, GlobalLootModifierRegistryObject::new);
	}

}
