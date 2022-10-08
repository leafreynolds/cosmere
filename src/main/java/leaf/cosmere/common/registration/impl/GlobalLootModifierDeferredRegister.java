package leaf.cosmere.common.registration.impl;

import com.mojang.serialization.Codec;
import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class GlobalLootModifierDeferredRegister<T extends Codec<? extends IGlobalLootModifier>> extends WrappedDeferredRegister<Codec<? extends IGlobalLootModifier>>
{

	public GlobalLootModifierDeferredRegister(String modid)
	{
		super(modid, ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS);
	}

	public <I extends T> GlobalLootModifierRegistryObject<I> register(String name, final Supplier<? extends I> sup)
	{
		return register(name, sup, GlobalLootModifierRegistryObject::new);
	}

}