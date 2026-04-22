package leaf.cosmere.common.registration.impl;

import com.mojang.serialization.MapCodec;
import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;

public class BiomeModifierSerializerRegistryObject<T extends BiomeModifier> extends WrappedRegistryObject<MapCodec<T>>
{

	public BiomeModifierSerializerRegistryObject(DeferredHolder<?, MapCodec<T>> registryObject)
	{
		super(registryObject);
	}
}
