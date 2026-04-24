package leaf.cosmere.common.registration.impl;

import com.mojang.serialization.MapCodec;
import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import java.util.function.Supplier;

public class LootFunctionDeferredRegister extends WrappedDeferredRegister<LootItemFunctionType<?>>
{

	public LootFunctionDeferredRegister(String modid)
	{
		super(modid, Registries.LOOT_FUNCTION_TYPE);
	}

	public <T extends LootItemFunction> LootItemFunctionTypeRegistryObject<T> register(String name, Supplier<MapCodec<T>> codec)
	{
		return register(name, () -> new LootItemFunctionType<>(codec.get()), LootItemFunctionTypeRegistryObject::new);
	}
}
