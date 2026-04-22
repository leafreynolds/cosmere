package leaf.cosmere.common.registration.impl;

import com.mojang.serialization.MapCodec;
import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class BiomeModifierSerializerDeferredRegister extends WrappedDeferredRegister<MapCodec<? extends BiomeModifier>>
{

	public BiomeModifierSerializerDeferredRegister(String modid)
	{
		super(modid, NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS);
	}

	public <T extends BiomeModifier> BiomeModifierSerializerRegistryObject<T> register(String name, Supplier<MapCodec<T>> sup)
	{
		return register(name, sup, BiomeModifierSerializerRegistryObject::new);
	}
}
