package leaf.cosmere.common.registration.impl;

import com.mojang.serialization.Codec;
import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;

public class BiomeModifierSerializerRegistryObject<T extends BiomeModifier> extends WrappedRegistryObject<Codec<T>>
{

	public BiomeModifierSerializerRegistryObject(DeferredHolder<? super Codec<T>, Codec<T>> registryObject)
	{
		super(registryObject);
	}
}
